package com.example.todo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.Todo
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.todo.network.TodosApi

sealed interface TodoUiState {
    data class Success(val todos: List<Todo>) : TodoUiState
    data object Error : TodoUiState
    data object Loading : TodoUiState
}

class TodoViewModel : ViewModel() {
    var todoUiState: TodoUiState by mutableStateOf(TodoUiState.Loading)
        private set

    init {
        getTodosList()
    }

    private fun getTodosList() {
        viewModelScope.launch {
            todoUiState = TodoUiState.Loading
            try {
                val todosApi = TodosApi.getInstance()
                val todos = todosApi.getTodos()
                todoUiState = TodoUiState.Success(todos)
            } catch (e: Exception) {
                todoUiState = TodoUiState.Error
            }
        }
    }
}