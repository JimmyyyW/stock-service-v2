package com.stockify.stockservicev2.service

import com.stockify.stockservicev2.model.AddStock
import com.stockify.stockservicev2.model.Stock
//import com.stockify.stockservicev2.model.StockHistory
//import com.stockify.stockservicev2.repository.StockHistoryRepository
import com.stockify.stockservicev2.repository.StockRepository
import com.stockify.stockservicev2.repository.StockRepositoryList
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono



@Service
class StockService constructor(private val stockRepository: StockRepository,
                               private val emailBuddyService: EmailBuddyService) {

    fun getAllStocks(): Flux<Stock> {
        return stockRepository.findAll()
    }

    fun getSingle(symbol: String): Flux<Stock> {
        return stockRepository.findBySymbol(symbol)
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

    fun getStockValue(symbol: String): Mono<Number> {
        return stockRepository.findTopBySymbol(symbol)
                .map { it.value }
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

    fun updateLatestTrade(symbol: String, time: String): Mono<String> {
        return stockRepository.findDistinctBySymbol(symbol)
                .map {
                    it.latestTrade = time
                    stockRepository.save(it).subscribe()
                }
                .flatMap {Mono.just("{ \"outcome\": \"success\" }") }
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

    fun createStock(stock: AddStock): Mono<Stock> {
        val newStock = Stock(ObjectId(), 0, false, stock.symbol, stock.name,
                stock.value, stock.volume, 0, 0, "N/A")
        return stockRepository.save(newStock)
                .onErrorMap { throwable ->
                    throwable.message?.let { emailBuddyService.sendErrorEmail(it)
                        Exception(throwable.message)
                    }
                }
    }

}

//@Service
//class StockHistoryService constructor(private val stockHistoryRepository: StockHistoryRepository) {
//
//    fun getStockHistory(): Flux<StockHistory> {
//        return stockHistoryRepository.findAll()
//    }
//}