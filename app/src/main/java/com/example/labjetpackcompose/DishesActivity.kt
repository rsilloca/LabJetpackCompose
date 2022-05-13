package com.example.labjetpackcompose

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.labjetpackcompose.Models.Department
import com.example.labjetpackcompose.Models.Dish
import com.example.labjetpackcompose.Utils.getJsonDataFromAsset
import com.example.labjetpackcompose.ui.theme.LabJetpackComposeTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DishesActivity : ComponentActivity() {

    private lateinit var dishes: List<Dish>
    private var titleDep:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = intent.extras
        val idDepartment = bundle?.getInt("id_department")
        val nameDepartment = bundle?.getString ("name_department").toString()
        titleDep= "Comidas de $nameDepartment"
        if (idDepartment != null) {
            Log.e("department id", idDepartment.toString())
            val jsonFileString = getJsonDataFromAsset(applicationContext, "dish.json")
            val gson = Gson()
            val listDepartmentType = object : TypeToken<List<Dish>>() {}.type
            val allDishes: List<Dish> = gson.fromJson(jsonFileString, listDepartmentType)
            dishes = allDishes.filter { d -> d.department_id == idDepartment }
            dishes.forEachIndexed { idx, dish -> Log.i("data", "> Item $idx:\n$dish") }
        }
        setContent {
            LabJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BuildDishesList(titleDep,dishes)
                }
            }
        }
    }
}

@Composable
fun BuildDishesList(titleDep:String ,dishes: List<Dish>) {
    //Text(text = "Hello!")
    Scaffold(
        topBar = { TopAppBar(
            title = { Text(titleDep) },
            backgroundColor = MaterialTheme.colors.primary
        )
        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(dishes) { dish ->
                    DishCard(dish)
                }
            }
        }
    )
}

@Composable
fun DishCard(dish: Dish) {
    val localContext = LocalContext.current
    Card (
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 4.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)

    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(dish.url),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = dish.name,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DefaultPreview2() {
    LabJetpackComposeTheme {
        BuildDishesList(titleDep = "Comidas de un Dep",dishes=listOf(
            Dish(
                food_id = 1,
                department_id = 1,
                name = "Carne Arrollada",
                url = "https://3.bp.blogspot.com/-gqj-ukMW8Vo/W-_06Hr2RII/AAAAAAAABO0/9AqJAQJdFjo7nxFDxZTd8W-wUg_sjwH_wCLcBGAs/s1600/descarga.jpg"
            ),
            Dish(
                food_id = 2,
                department_id = 1,
                name = "Purtumute",
                url = "https://4.bp.blogspot.com/-IeWZc9ko7b8/W-_snDvy0iI/AAAAAAAABNE/l7lwrS5rndgnbTzYSTLETaI8lyKhufEhgCLcBGAs/s1600/descarga%2B%25283%2529.jpg"
            )

        ))
    }
}