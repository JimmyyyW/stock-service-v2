package com.stockify.stockservicev2.controller

import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.service.StockService
import junit.framework.Assert.assertEquals
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier
import java.time.Duration

@SpringBootTest
internal class StockRestControllerTest {

    @Autowired
    private lateinit var stockService: StockService

    var mockService = Mockito.mock(StockService::class.java)

    @Test
    fun whenMongoMethodCalled_controllerContactsService_thenReturnsFluxOfStock() {
        //given
        val stock: Stock? = stockService.getAllStocks().blockFirst()
        val actualSymbol: String? = stock?.symbol
        val actualName: String? = stock?.name
        //when
        val expectedSymbol = "C"
        val expectedName = "Citigroup"
        //then
        assertEquals(expectedName, actualName)
        assertEquals(expectedSymbol, actualSymbol)
    }

    @Test
    fun getStocks_take47() {
        StepVerifier.withVirtualTime { -> stockService.getAllStocks().take(47) }
                .thenAwait(Duration.ofMinutes(5))
                .expectNextCount(47)
                .verifyComplete()
    }

    @Test
    fun getAllStocksCallsServiceAndReturnsStocks() {
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
        val stock2 = Stock(
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
        val stocks = Flux.just()
    }
}