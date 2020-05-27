package com.stockify.stockservicev2.model

import lombok.Data
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document(collection = "stocks")
data class Stock (@Id  val _id: ObjectId?,
                   val gains: Number,
                   val isPositive: Boolean,
                   val symbol: String,
                   val name: String,
                   val value: Number,
                   val volume: Number,
                   val open: Number,
                   val close: Number,
                   var latestTrade: String
)
