package br.com.zup.edu.remove

import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.edu.RemoveChavePixResponse
import br.com.zup.edu.shared.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest{

    @field:Inject
    lateinit var keyManagerGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject @field:Client("/")
    lateinit var client: HttpClient

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Test
    internal fun `deve deletar uma chave existente`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoveChavePixResponse.newBuilder()
            .setClienteId(clientId)
            .setPixId(pixId)
            .build()

        BDDMockito.given(keyManagerGrpc.remove(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clientId/pix/$pixId")

        LOGGER.info("\n TEST REQUEST => ${request}")

        val response = client.toBlocking().exchange(request, Any::class.java)

        LOGGER.info("\n TEST RESPONSE STATUS CODE => ${response.code()} ${response.status}")

        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.status)

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
    }

}