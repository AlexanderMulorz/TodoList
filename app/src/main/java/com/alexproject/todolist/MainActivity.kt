package com.alexproject.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexproject.todolist.ui.theme.TodoListTheme
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class Item(
    val text: String,
    val done: Boolean
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonString = """
    [
        { "text": "App Fertigstellen", "done": true },
        { "text": "Auf github hochladen",  "done": false },
        { "text": "Was anderes",  "done": false }
    ]
"""

        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Todo Liste")
                            }
                        )
                    }) { innerPadding ->
                        NavScreens(innerPadding, jsonString)
                }
            }
        }
    }
}

@Composable
fun NavScreens(innerPadding: PaddingValues, jsonString: String){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Column() {
                ListExample(innerPadding, jsonString)
                Button(onClick = { navController.navigate("addscreen") }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add element to TODO list"
                    )
                }
            }
        }
        composable("addscreen") {
            AddBulletPointScreen(innerPadding)
        }
    }
}


@Composable
fun ListExample(innerPadding: PaddingValues, jsonString: String) {

    val itemsList = remember {
        Json.decodeFromString<List<Item>>(jsonString)
            .toMutableStateList()
    }

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(
            items = itemsList,
            key = { it.text } // IMPORTANT: stable key
        ) { item ->
            Row {
                var imageDone = remember { mutableStateOf(Icons.Default.Close) }

                Button(onClick = { imageDone.value = Icons.Default.Done }) {
                    Icon(
                        imageVector = imageDone.value,
                        contentDescription = "Done"
                    )
                }

                Text(text = item.text)

                Button(onClick = {
                    itemsList.remove(item) // ✅ triggers recomposition
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

@Composable
fun BulletPoint(bulletPointItemList: MutableList<Item>, item:Item){
    Row{
        var imageDone = remember { mutableStateOf(Icons.Default.Close) }
        Button(onClick = {imageDone.value= Icons.Default.Done}) {
            Icon(
                imageVector = imageDone.value,
                contentDescription = "Click if you are done"
            )
        }
        Text(text = item.text)
        Button(onClick = { bulletPointItemList.remove(item)}) {
            Image(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete the bullet point")
        }
    }
}

@Composable
fun AddBulletPointScreen(innerPadding: PaddingValues) {
    Text(
        text = "AddBulletPointScreen",
        modifier = Modifier.padding(innerPadding)
    )


}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoListTheme {

    }
}