package com.alexproject.todolist

@kotlinx.serialization.Serializable
data class Item(
    val id:Int,
    val text: String,
    val done: Boolean
)