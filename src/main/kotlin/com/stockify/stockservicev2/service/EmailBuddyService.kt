package com.stockify.stockservicev2.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import lombok.AllArgsConstructor
import lombok.Data
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.Disposable

@Service
class EmailBuddyService() {

    private val webClient: WebClient = WebClient
            .builder()
            .baseUrl("http://localhost:5000")
            .build()


    fun sendErrorEmail(message: String): Disposable {
        return webClient.post()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ErrorEmailRequest(target = "fdm0506stockify@gmail.com",
                        message = message)))
                .retrieve()
                .bodyToMono(ErrorEmailResponse::class.java)
                .doOnError { err -> println(err.message + " | Check Email buddy running") }
                .subscribe()
    }

}

@Data
@AllArgsConstructor
data class ErrorEmailResponse(
        @JsonProperty("details")
        @JsonPropertyDescription("result of attempt to send registration email")
        val details: String
)

@Data
@AllArgsConstructor
data class ErrorEmailRequest(
        @JsonProperty("target")
        @JsonPropertyDescription("result of attempt to send registration email")
        val target: String,

        @JsonProperty("message")
        @JsonPropertyDescription("the message of the error")
        val message: String
)

