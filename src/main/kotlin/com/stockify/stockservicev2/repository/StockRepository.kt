package com.stockify.stockservicev2.repository

import com.stockify.stockservicev2.model.Stock
//import com.stockify.stockservicev2.model.StockHistory
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface StockRepository: ReactiveMongoRepository<Stock, String> {

    fun findBySymbol(symbol: String): Flux<Stock>

    fun findTopBySymbol(symbol: String): Mono<Stock>

    fun findDistinctBySymbol(symbol: String): Mono<Stock>
}

interface StockRepositoryList: ReactiveMongoRepository<List<Stock>, String> { }

//interface StockHistoryRepository: ReactiveMongoRepository<StockHistory, String> { }
