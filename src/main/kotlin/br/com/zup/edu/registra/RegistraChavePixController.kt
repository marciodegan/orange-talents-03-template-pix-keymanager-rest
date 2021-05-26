package br.com.zup.edu.registra

import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.NovaChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraChavePixController(
    private val registraChavePixClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub
) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun registra(clienteId: UUID, @Body @Valid request: NovaChaveRequest): HttpResponse<Any> {

        LOGGER.info("\n  ${clienteId} | ${request.tipoChave} | ${request.tipoConta} | ${request.chave}")

        val grpcResponse = registraChavePixClient.registra(request.toGrpcModel(clienteId))

        LOGGER.info("\n grpcResponse => pixId: ${grpcResponse.pixId} | clienteId: ${grpcResponse.clienteId}")

        return HttpResponse.created(location(clienteId, grpcResponse.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}