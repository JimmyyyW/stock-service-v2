package com.stockify.stockservicev2.service

import com.stockify.stockservicev2.model.Stock
//import com.stockify.stockservicev2.model.StockHistory
//import com.stockify.stockservicev2.repository.StockHistoryRepository
import com.stockify.stockservicev2.repository.StockRepository
import com.stockify.stockservicev2.repository.StockRepositoryList
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class StockService constructor(private val stockRepository: StockRepository,
                               private val stockRepositoryList: StockRepositoryList) {

    fun getAllStocks(): Flux<Stock> {
        return stockRepository.findAll()
    }

    fun getSingle(symbol: String): Flux<Stock> {
        return stockRepository.findBySymbol(symbol)
    }

    fun getAllStocksAsList(): Flux<List<Stock>> {
        return stockRepositoryList.findAll()
    }

    fun getStockValue(symbol: String): Mono<Number> {
        return stockRepository.findTopBySymbol(symbol)
                .map { it.value }
    }

    fun updateLatestTrade(symbol: String, time: String): Mono<String> {
        return stockRepository.findDistinctBySymbol(symbol)
                .map {
                    it.latestTrade = time
                    stockRepository.save(it).subscribe()
                    println(it)
                }
                .flatMap {Mono.just("{ \"outcome\": \"success\" }") }
    }
}

//@Service
//class StockHistoryService constructor(private val stockHistoryRepository: StockHistoryRepository) {
//
//    fun getStockHistory(): Flux<StockHistory> {
//        return stockHistoryRepository.findAll()
//    }
//}