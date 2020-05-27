package com.stockify.stockservicev2.controller

import com.stockify.stockservicev2.model.AddStock
import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.service.StockService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

@WebFluxTest
class StockRestControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var stockService: StockService

    //private val mockService: StockService = Mockito.mock(StockService::class.java)

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

        val stockFlux = Flux.just(stock, stock2)
        Mockito.`when`(stockService.getAllStocks()).thenReturn(stockFlux.take(2))

        val result: FluxExchangeResult<String> = this.webTestClient.get()
                .uri("/api/v2/stocks").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .returnResult(String::class.java)


        StepVerifier.create( result.responseBody.take(2) )
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(2))
                .expectNextCount(1)
                .thenAwait(Duration.ofSeconds(2))
                .expectNextCount(1)
                .verifyComplete()

//        val stockListFlux = Flux.just(stocks, stocks2)
    }

    @Test
    fun testStocksXMLReturnsXMLListOfStocks() {
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

        val stockFlux = Flux.just(stock, stock2)
        Mockito.`when`(stockService.getAllStocks()).thenReturn(stockFlux.take(2))

        this.webTestClient.get()
                .uri("/api/v2/stocks/xml").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$[0].symbol").isEqualTo("C")
                .jsonPath("$[0].gains").isEqualTo("123.0")
                .jsonPath("$[1].gains").isEqualTo("123.0")
                .jsonPath("$[1].symbol").isEqualTo("C")
    }

    @Test
    fun getSingleReturnsChosenStock() {
        val result = this.webTestClient.get()
                .uri("/api/v2/stocks/C}").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .returnResult(String::class.java)

        StepVerifier.create( result.responseBody)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(2))
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun getStockValueFromStockSymbol() {

        Mockito.`when`(stockService.getStockValue("C")).thenReturn(Mono.just(1))

        val result = this.webTestClient.get()
                .uri("/api/v2/stocks/value/C").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Number::class.java)

        StepVerifier.create( result.responseBody)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun updateLatestReturnsFromController() {

        Mockito.`when`(stockService.updateLatestTrade("time", "C")).thenReturn(Mono.just("a"))

        val result = this.webTestClient.put()
                .uri("/api/v2/stocks/update/C").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(""))
                .exchange()
                .returnResult(String::class.java)

        StepVerifier.create( result.responseBody)
                .expectSubscription()
                .verifyComplete()
    }

    @Test
    fun createNewStockReturnsFromController() {
        val stockAfter = Stock(
                ObjectId(),
                100.0,
                true,
                "FDM",
                "FDMGroup",
                100,
                100000,
                213.0,
                321.0,
                "today :)"
        )

        val stock: AddStock = AddStock("FDM", "FDMGroup", 100, 100000)

        Mockito.`when`(stockService.createStock(stock)).thenReturn(Mono.just(stockAfter))

        this.webTestClient.post()
                .uri("/api/v2/stocks/new")
                .body(BodyInserters.fromValue(stock))
                .exchange()
                .expectBody()
                .jsonPath("$.symbol").isEqualTo("FDM")

    }
}