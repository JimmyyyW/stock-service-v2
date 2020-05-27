package com.stockify.stockservicev2.model

data class AddStock(
        val symbol: String,
        val name: String,
        val value: Number,
        val volume: Number
)