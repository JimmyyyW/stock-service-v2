package com.stockify.stockservicev2.model

import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StockTest {

    @Test
    fun initialise() {
        val stock = Stock(
                ObjectId(),
                123.0,
                true,
                "C",
                "Citadel",
                1233.20,
                20000,
                213.0,
                321.0,
                "today :)"
        )

        assertEquals(stock.gains, 123.0)
        assertEquals(stock.isPositive, true)
        assertEquals(stock.value, 1233.20)
        assertEquals(stock.volume, 20000)
        assertEquals(stock.open, 213.0)
        assertEquals(stock.close, 321.0)
    }

}