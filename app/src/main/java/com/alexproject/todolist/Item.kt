package com.alexproject.todolist

@kotlinx.serialization.Serializable
data class Item(
    val text: String,
    var done: Boolean
)