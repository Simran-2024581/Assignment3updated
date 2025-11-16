package com.example.assignment2brewbyte

data class CartItem(
    val name: String,
    val price: Double
)

object Cart {

    // public list of items
    val items = mutableListOf<CartItem>()

    // add item
    fun addItem(name: String, price: Double) {
        items.add(CartItem(name, price))
    }

    // total price
    fun getTotal(): Double {
        return items.sumOf { it.price }
    }

    // clear cart
    fun clear() {
        items.clear()
    }
}
