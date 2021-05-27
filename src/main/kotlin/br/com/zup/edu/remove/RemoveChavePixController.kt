package br.com.zup.edu.remove

import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.NovaChaveRequest
import br.com.zup.edu.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(
    private val removeChavePixGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun remove(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("\n REQUEST => clientid: ${clienteId} | chave ${pixId} ")

        removeChavePixGrpc.remove(
            RemoveChavePixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )
        LOGGER.info("\n Chave removida com sucesso => clientid: ${clienteId} | chave ${pixId} ")

        return HttpResponse.ok()
    }
}