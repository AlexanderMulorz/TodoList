package com.alexproject.todolist

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json

class FileHandler {
    fun writeToFile(context: Context, fileName: String, list: List<Item>) {
        try {
            val gson = Gson()
            val json = gson.toJson(list)
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
                it.close()
            }
            Log.d("FileExample", "Data written successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readJsonList(
        context: Context,
        fileName: String
    ): List<Item> {
        return try {
            val json = context.openFileInput(fileName).use {
                it.bufferedReader().readText()
            }
            val type = object : TypeToken<List<Item>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}