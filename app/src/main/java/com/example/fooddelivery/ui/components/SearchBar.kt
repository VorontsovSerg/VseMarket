package com.example.fooddelivery.ui.components

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.data.Persistence

/**
 * Компонент верхней панели.
 * Здесь поле ввода для поиска товаров.
 */

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 24.dp
) {
    val context = LocalContext.current
    var showHistory by remember { mutableStateOf(false) }
    var searchHistory by remember { mutableStateOf(Persistence.loadSearchHistory(context)) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    val searchRunnable = remember { Runnable { if (searchQuery.isNotEmpty()) onSearch(searchQuery) } }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    onQueryChange(it)
                    handler.removeCallbacks(searchRunnable)
                    handler.postDelayed(searchRunnable, 2000L)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .onFocusChanged { focusState ->
                        showHistory = focusState.isFocused
                        onFocusChange(focusState.isFocused)
                    },
                placeholder = { Text("Поиск продуктов...", color = MaterialTheme.colorScheme.onSurface) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (searchQuery.isNotEmpty()) {
                        val updatedHistory = listOf(searchQuery) + searchHistory.filter { it != searchQuery }
                        Persistence.saveSearchHistory(context, updatedHistory)
                        searchHistory = updatedHistory
                        onSearch(searchQuery)
                        showHistory = false
                    }
                }),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            )

            if (showHistory && searchHistory.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    items(searchHistory) { historyItem ->
                        Text(
                            text = historyItem,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    onQueryChange(historyItem)
                                    showHistory = false
                                    onSearch(historyItem)
                                }
                        )
                    }
                }
                Button(
                    onClick = {
                        Persistence.clearSearchHistory(context)
                        searchHistory = emptyList()
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Очистить историю")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacks(searchRunnable)
        }
    }
}
