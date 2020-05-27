package com.stockify.stockservicev2.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AddStockTest{

    @Test
    fun initialise() {
        val addStock = AddStock(
                "A",
                "Anger",
                123,
                100000
        )

        assertEquals("Anger", addStock.name)
        assertEquals("A", addStock.symbol)
        assertEquals(123, addStock.value)
        assertEquals(100000, addStock.volume)
    }
}