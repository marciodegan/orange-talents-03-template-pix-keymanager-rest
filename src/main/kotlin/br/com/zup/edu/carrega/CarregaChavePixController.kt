package br.com.zup.edu.carrega

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.KeyManagerCarregaGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController(
    val keyManagerCarregaChavePix: KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        val chaveResponse = keyManagerCarregaChavePix.carrega(
            CarregaChavePixRequest.newBuilder()
                .setPixId(
                    CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClienteId(clienteId.toString())
                        .setPixId(pixId.toString())
                        .build()
                )
                .build()
        )

        LOGGER.info("${chaveResponse.chave}")

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }
}