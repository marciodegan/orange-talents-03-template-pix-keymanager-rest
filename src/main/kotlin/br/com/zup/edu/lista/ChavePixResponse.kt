package br.com.zup.edu.lista

import br.com.zup.edu.ListaChavesPixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chavePix: ListaChavesPixResponse.ChavePix) { // retorna a ChavePix do ListaChavesPixResponse do protobuf

    val id = chavePix.pixId
    val chave = chavePix.chave
    val tipo = chavePix.tipo
    val tipoDeConta = chavePix.tipoDeConta
    val criadaEm = chavePix.criadaEm.let { it->
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}