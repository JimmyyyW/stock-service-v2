package com.stockify.stockservicev2.controller

import com.stockify.stockservicev2.model.AddStock
import com.stockify.stockservicev2.model.Stock
import com.stockify.stockservicev2.service.EmailBuddyService
//import com.stockify.stockservicev2.model.StockHistory
//import com.stockify.stockservicev2.model.StockPrice
//import com.stockify.stockservicev2.service.StockHistoryService
import com.stockify.stockservicev2.service.StockService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom


@RestController
class StockRestController(private val stockService: StockService, private val emailBuddyService: EmailBuddyService
                          /*private val stockHistoryService: StockHistoryService*/) {

    // Reactive streams testing
//    @GetMapping(value = ["api/poc/stocks/{symbol}"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
//    fun prices(@PathVariable symbol: String): Flux<StockPrice> {
//        return Flux.interval(Duration.ofSeconds(2))
//                .map { StockPrice(symbol, randomStockPrice(), LocalDateTime.now()) }
//    }

    //all stocks
    @GetMapping(value = ["api/v2/stocks"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getAllStocks(): Flux<MutableList<Stock>> {
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(2))
                .flatMap { stockService.getAllStocks().collectList() }
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }

    }

    @GetMapping(value = ["api/v2/stocks/xml"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllStocksOrderedXML(): Mono<MutableList<Stock>> {
        return stockService.getAllStocks().collectList()
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

    @GetMapping(value = ["api/v2/stocks/{symbol}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getSingleStock(@PathVariable symbol: String): Flux<Stock> {
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(10))
                .flatMap { stockService.getSingle(symbol) }
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it) }
                    Exception(throwable.message)
                }


    }

    @GetMapping(value = ["api/v2/stocks/value/{symbol}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getValue(@PathVariable symbol: String): Mono<Number> {
        return stockService.getStockValue(symbol)
                .onErrorMap { throwable ->
                         throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                         Exception(throwable.message)
                     }
                }
    }


    @PutMapping(value = ["api/v2/stocks/update/{symbol}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateLatest(@RequestBody time: String, @PathVariable symbol: String): Mono<String> {
        return stockService.updateLatestTrade(symbol, time)
                .onErrorMap { throwable ->
            throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                Exception(throwable.message)
            }
        }
    }

    @PostMapping(value= ["api/v2/stocks/new"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createNewStock(@RequestBody stock: AddStock): Mono<Stock> {
        return stockService.createStock(stock)
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

//    private fun randomStockPrice(): Number {
//        return ThreadLocalRandom.current().nextDouble(550.0)
//    }
//    @GetMapping(value = ["api/v2/stocks/history/all"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
//    fun get(): Flux<StockHistory> {
//        return stockHistoryService.getStockHistory()
//                .onErrorMap { e -> Exception(e.message) }
//    }
}
