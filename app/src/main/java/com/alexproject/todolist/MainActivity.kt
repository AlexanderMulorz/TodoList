package com.alexproject.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexproject.todolist.ui.theme.TodoListTheme
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class Item(
    val text: String,
    val done: Boolean
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonString = """
    [
        { "text": "App Fertigstellen", "done": true },
        { "text": "Auf github hochladen",  "done": false }
    ]
"""
        val itemsList: List<Item> = Json.decodeFromString(jsonString)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListExample(modifier = Modifier.fillMaxSize(), itemsList)
                }
            }
        }
    }
}


@Composable
fun ListExample(modifier: Modifier = Modifier, itemsList: List<Item>){


    var count = remember { mutableStateOf(0)}
    Column() {
        itemsList.forEach { bulletpointItem ->
            BulletPoint(bulletpointItem.text)
        }
    }
}

@Composable
fun BulletPoint(bulletpointText: String){
    Row{
        var imageDone = remember { mutableStateOf(Icons.Default.Close) }
        Button(onClick = {imageDone.value= Icons.Default.Done}) {
            Icon(
                imageVector = imageDone.value,
                contentDescription = "Click if you are done"
            )
        }
        Text(text = bulletpointText)
        Button(onClick = {}) {
            Image(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete the bullet point")
        }
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
        Greeting("Android")
    }
}