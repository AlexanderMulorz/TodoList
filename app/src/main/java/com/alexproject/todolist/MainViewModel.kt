package com.alexproject.todolist

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SharedUiState(
    val items: List<Item> = emptyList()
)

class MainViewModel: ViewModel() {
    private var itemList:List<Item> = mutableListOf()
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState

    fun addItem(item: Item) {
        _uiState.update { state ->
            state.copy(items = state.items + item)
        }
    }
    fun removeItem(item: Item) {
        _uiState.update { state ->
            state.copy(items = state.items - item)
        }
    }
    fun initFromJson(fileName:String, context: Context){
        val f = FileHandler()

        _uiState.update { state ->
            state.copy(items = f.readJsonList(context, "jsonFile.txt"))
        }
    }
}
