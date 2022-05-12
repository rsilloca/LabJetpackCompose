package com.example.labjetpackcompose

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import com.example.labjetpackcompose.Utils.getJsonDataFromAsset
import com.example.labjetpackcompose.ui.theme.LabJetpackComposeTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {

    private lateinit var departments: List<Department>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonFileString = getJsonDataFromAsset(applicationContext, "department.json")
        val gson = Gson()
        val listDepartmentType = object : TypeToken<List<Department>>() {}.type
        departments = gson.fromJson(jsonFileString, listDepartmentType)
        departments.forEachIndexed { idx, department -> Log.i("data", "> Item $idx:\n$department") }
        setContent {
            LabJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BuildDepartmentsList(departments)
                }
            }
        }
    }
}

@Composable
fun BuildDepartmentsList(departments: List<Department>) {
    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Departments Activity") },
            backgroundColor = MaterialTheme.colors.primary
        )},
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(departments) { department ->
                    DepartmentCard(department)
                }
            }
        }
    )
}

@Composable
fun DepartmentCard(department: Department) {
    val localContext = LocalContext.current
    Card (
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 4.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                val intentDishes = Intent(localContext, DishesActivity::class.java)
                intentDishes.putExtra("id_department", department.department_id)
                localContext.startActivity(intentDishes)
            }
    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(department.url),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = department.department_name,
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
fun DefaultPreviewDepartments() {
    LabJetpackComposeTheme {
        BuildDepartmentsList(departments = listOf(
            Department(
                department_id = 1,
                department_name = "Arequipa",
                url = "https://c7.alamy.com/compes/3/895b1300e3bb4df7891a76076d61b855/hy547g.jpg"
            ),
            Department(
                department_id = 2,
                department_name = "Lima",
                url = "https://c7.alamy.com/compes/3/895b1300e3bb4df7891a76076d61b855/hy547g.jpg"
            )
        ))
    }
}