package com.stockify.stockservicev2.controller

import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.service.StockService
import junit.framework.Assert.assertEquals
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

@SpringBootTest
internal class StockRestControllerITTest {

    @Autowired
    private lateinit var stockService: StockService

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


}