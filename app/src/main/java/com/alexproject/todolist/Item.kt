package com.alexproject.todolist

@kotlinx.serialization.Serializable
data class Item(
    val text: String,
    val done: Boolean
)