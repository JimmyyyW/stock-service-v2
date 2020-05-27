package com.stockify.stockservicev2.service

import com.stockify.stockservicev2.model.AddStock
import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.repository.StockRepository
import com.stockify.stockservicev2.repository.StockRepositoryList
import groovy.util.GroovyTestCase.assertEquals
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    @Test
    fun serviceInvokesRepositoryAndReturnsStubbedStock() {
        val expected: Flux<Stock> = Flux.just(
                Stock(ObjectId("5e6a62d83bd67d1ea7b8c335"), 13, true, "ABC",
                        "stub stock", 123, 120000, 24, 25, "N/A")
        )

        `when`(mockStockRepository.findBySymbol("ABC")).thenReturn(expected)

        val actual: Flux<Stock> = unit.getSingle("ABC")

        assertEquals(actual, expected)
    }

    @Test
    fun serviceInvokesRepositoryAndReturnsValueForSingleStock() {
        val expected: Mono<Stock> = Mono.just(
                Stock(ObjectId("5e6a62d83bd67d1ea7b8c335"), 13, true, "ABC",
                        "stub stock", 123, 120000, 24, 25, "N/A")
        )
        `when`(mockStockRepository.findTopBySymbol("C")).thenReturn(expected)

        val actual: Mono<Number> = unit.getStockValue("C")

        assertEquals(123, actual.block())
    }

    @Test
    fun updateLatestTradeReturnsSuccessWhenNoError() {
        val expected: Mono<Stock> = Mono.just(
                Stock(ObjectId("5e6a62d83bd67d1ea7b8c335"), 13, true, "C",
                        "stub stock", 123, 120000, 24, 25, "N/A")
        )

        `when`(mockStockRepository.findDistinctBySymbol("C")).thenReturn(expected)
        `when`(mockStockRepository.save(expected.block()!!)).thenReturn(expected)

        val actual: Mono<String> = unit.updateLatestTrade("C", "hello")

        assertEquals("{ \"outcome\": \"success\" }", actual.block())
    }

    @Test
    fun createsNewStock() {

        val stock = Stock(ObjectId("5e6a62d83bd67d1ea7b8c335"), 13, true, "C",
                "stub stock", 123, 120000, 24, 25, "N/A")


        `when`(mockStockRepository.save(stock)).thenReturn(Mono.just(stock))



        //assertEquals(actual.block(), expected.block())
    }
}
