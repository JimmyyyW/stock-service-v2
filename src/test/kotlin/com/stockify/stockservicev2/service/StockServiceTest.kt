package com.stockify.stockservicev2.service

import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.repository.StockRepository
import com.stockify.stockservicev2.repository.StockRepositoryList
import groovy.util.GroovyTestCase.assertEquals
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import reactor.core.publisher.Flux
import java.util.*

internal class StockServiceTest {

    private val mockStockRepository: StockRepository = mock(StockRepository::class.java)
    private val mockStockRepositoryList: StockRepositoryList = mock(StockRepositoryList::class.java)

    private var unit: StockService = StockService(mockStockRepository, mockStockRepositoryList)

    @Test
    fun serviceInvokesRepository_mockReturnsFluxOfStocks() {

        val expected : Flux<Stock> = Flux.just(
                Stock(ObjectId("5e6a62d83bd67d1ea7b8c335"), 13, true, "ABC",
                        "stub stock", 123, 120000, 24, 25, "N/A"),

                Stock(ObjectId("5e6a62d83bd67d1ea7b8c336"), 13, true, "ABC",
                        "stub stock", 123, 120000, 24, 25, "N/A"))

        `when`(mockStockRepository.findAll()).thenReturn(expected)

        val actual : Flux<Stock> = unit.getAllStocks()

        assertEquals(expected, actual)
    }
}
