package com.example.vsemarket.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vsemarket.data.CartItem
import com.example.vsemarket.viewmodel.CartViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import android.content.Intent
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import com.example.vsemarket.PaymentActivity

/**
 * Фрагмент отображения корзины пользователя.
 * Отвечает за отображение списка товаров в корзине, изменение количества и удаление товаров.
 */

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val cartItems = viewModel.cartItems.collectAsState().value
    val totalPrice by viewModel.totalPrice.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Корзина",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text(
                text = "Корзина пуста",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(cartItems) { index, item ->
                        CartItemRow(item, viewModel, cartItems.size > 1, index + 1)
                        if (index < cartItems.size - 1 && cartItems.size > 1) {
                            DashedDivider(
                                color = MaterialTheme.colorScheme.onSurface,
                                thickness = 1.dp,
                                dashLength = 4.dp,
                                gapLength = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Итого: $totalPrice ₽",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, PaymentActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Оплатить",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun CartItemRow(cartItem: CartItem, viewModel: CartViewModel, showQuantityButtons: Boolean, itemNumber: Int) {
    var quantity by remember { mutableStateOf(cartItem.quantity) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$itemNumber. ${cartItem.product.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${cartItem.product.price} ₽",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (showQuantityButtons) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "−",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (quantity > 1) {
                                quantity--
                                viewModel.updateQuantity(cartItem.product.id, -1)
                            } else {
                                viewModel.removeFromCart(cartItem.product.id)
                                quantity = 0
                            }
                        }
                )
                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "+",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            quantity++
                            viewModel.updateQuantity(cartItem.product.id, 1)
                        }
                )
            }
        } else {
            Text(
                text = "$quantity шт.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                DropdownMenuItem(
                    text = { Text("Удалить", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        viewModel.removeFromCart(cartItem.product.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DashedDivider(
    color: Color,
    thickness: Dp,
    dashLength: Dp,
    gapLength: Dp,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    Canvas(modifier = modifier) {
        val path = Path()
        val width = size.width
        val dash = dashLength.value * density
        val gap = gapLength.value * density
        var x = 0f
        while (x < width) {
            path.moveTo(x, size.height / 2)
            x += dash
            path.lineTo(x, size.height / 2)
            x += gap
        }
        drawPath(path, color, style = Stroke(width = thickness.value * density))
    }
}