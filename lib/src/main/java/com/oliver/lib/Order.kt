package com.oliver.lib

import java.util.ArrayList

data class OrderDetail(
    val foodItem: FoodItem,
    val quantity: Int
) {
    val totalPrice: Int get() = foodItem.price * quantity
}

data class Order(
    val id: Int,
    val customerName: String,
    val items: ArrayList<OrderDetail>
) {
    val totalPrice: Int get() = items.sumOf { it.totalPrice }
}