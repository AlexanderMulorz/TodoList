package com.alexproject.todolist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexproject.todolist.ui.theme.TodoListTheme
import kotlinx.serialization.json.Json
import kotlin.collections.contains


class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createJsonFile("jsonFile.txt", this.baseContext)

        //var bulletPointList = Json.decodeFromString<List<Item>>(jsonString) as MutableList<Item>

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
                        NavScreens(innerPadding)
                }
            }
        }
    }
}

@Composable
fun NavScreens(innerPadding: PaddingValues){
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    viewModel.initFromJson("jsonFile.txt",LocalContext.current)
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Column() {
                ListExample(innerPadding, viewModel)
                Button(onClick = { navController.navigate("addscreen") }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add element to TODO list"
                    )
                }
            }
        }
        composable("addscreen") {
            AddBulletPointScreen(innerPadding, navController, viewModel)
        }
    }
}


@Composable
fun ListExample(innerPadding: PaddingValues, viewModel: MainViewModel) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(
            items = state.value.items,
            key = { it.id } // IMPORTANT: stable key
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
                    viewModel.removeItem(item)
                    viewModel.saveToJson("jsonFile.txt",context)
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
fun AddBulletPointScreen(innerPadding: PaddingValues, navController: NavController, viewModel: MainViewModel) {
    val text = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(modifier = Modifier.padding(innerPadding)) {
        Text(
            text = "Add Bulletpoint",
        )
        TextField(value=text.value,
            onValueChange = { text.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Row{
            Button(onClick = {
            navController.navigate("home")
        }){
            Text(
                text= "Go Back"
            )
        }
            Button(onClick = {
                if(!text.value.equals("")){
                    navController.navigate("home")
                    viewModel.addItem(Item(viewModel.uiState.value.items.size,text.value, false))
                    viewModel.saveToJson("jsonFile.txt",context)
                }

            }){
                Text(
                    text= "Save Bulletpoint"
                )
            }
        }
    }
}
fun createJsonFile(fileName:String, context: Context){
    val jsonString = """
    [
        { "id": "0","text": "App Fertigstellen", "done": true },
        { "id": "1","text": "Auf github hochladen",  "done": false },
        { "id": "2","text": "Was anderes",  "done": false }
    ]
"""

    var writeBulletPointList = Json.decodeFromString<List<Item>>(jsonString)
    val f = FileHandler()
    if(!context.fileList().contains(fileName)) {
        f.writeToFile(context, fileName, writeBulletPointList)
    }
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