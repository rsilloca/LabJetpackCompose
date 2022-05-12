package com.example.labjetpackcompose

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.labjetpackcompose.Models.Department
import com.example.labjetpackcompose.Models.Dish
import com.example.labjetpackcompose.Utils.getJsonDataFromAsset
import com.example.labjetpackcompose.ui.theme.LabJetpackComposeTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DishesActivity : ComponentActivity() {

    private lateinit var dishes: List<Dish>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = intent.extras
        val idDepartment = bundle?.getInt("id_department")
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
                    BuildDishesList(dishes)
                }
            }
        }
    }
}

@Composable
fun BuildDishesList(dishes: List<Dish>) {
    Text(text = "Hello!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    LabJetpackComposeTheme {
        BuildDishesList(listOf())
    }
}