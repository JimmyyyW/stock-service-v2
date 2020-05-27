package com.stockify.stockservicev2.config

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class CorsFilter : WebFilter {
    override fun filter(serverWebExchange: ServerWebExchange, webFilterChain: WebFilterChain): Mono<Void> {
        serverWebExchange.response.headers.add("Access-Control-Allow-Origin", "*")
        serverWebExchange.response.headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
        serverWebExchange.response.headers.add("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
        return if (serverWebExchange.request.method == HttpMethod.OPTIONS) {
            serverWebExchange.response.headers.add("Access-Control-Max-Age", "1728000")
            serverWebExchange.response.statusCode = HttpStatus.NO_CONTENT
            Mono.empty()
        } else {
            serverWebExchange.response.headers.add("Access-Control-Expose-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
            webFilterChain.filter(serverWebExchange)
        }
    }
}