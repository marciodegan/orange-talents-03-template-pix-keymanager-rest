package br.com.zup.edu.lista

import br.com.zup.edu.*

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class ListaChavesPixController(
    val keyManagerListaChavePix: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/")
    fun lista(clienteId: UUID): HttpResponse<Any> {

        val pix = keyManagerListaChavePix.lista(ListaChavesPixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .build())

        val chaves = pix.chavesList.map { it -> ChavePixResponse(it)} // mapeia e devolve uma lista de chaves pix.

        return HttpResponse.ok(chaves)
    }
}