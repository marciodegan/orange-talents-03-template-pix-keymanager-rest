package br.com.zup.edu.carrega

import br.com.zup.edu.CarregaChavePixResponse
import br.com.zup.edu.KeyManagerCarregaGrpcServiceGrpc
import br.com.zup.edu.TipoDeChave
import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.shared.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CarregaChavePixControllerTest {

    @field:Inject
    lateinit var carregaChaveStub: KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CHAVE_EMAIL = "joao@joa.com"
    val CONTA_CORRENTE = TipoDeConta.CONTA_CORRENTE
    val TIPO_DE_CHAVE_EMAIL = TipoDeChave.EMAIL
    val INSTITUICAO = "ITAU"
    val TITULAR = "João da Silva"
    val DOCUMENTO_DO_TITULAR = "1223123123"
    val AGENCIA = "0001"
    val NUMERO_DA_CONTA = "1000-1"


    @Test
    internal fun `deve carregar uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(carregaChaveStub.carrega(Mockito.any())).willReturn(carregaChavePixResponse(clienteId, pixId))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
    }

    private fun carregaChavePixResponse(clienteId: String, pixId: String) : CarregaChavePixResponse {
        val chaveCarregada = CarregaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChave(
                CarregaChavePixResponse.ChavePix.newBuilder()
                    .setTipo(TIPO_DE_CHAVE_EMAIL)
                    .setChave(CHAVE_EMAIL)
                    .setConta(
                        CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                            .setInstituicao(INSTITUICAO)
                            .setNomeDoTitular(TITULAR)
                            .setCpfDoTitular(DOCUMENTO_DO_TITULAR)
                            .setAgencia(AGENCIA)
                            .setTipo(CONTA_CORRENTE)
                            .setNumeroDaConta(NUMERO_DA_CONTA)
                            .build()
                    )
            ).build()
        return chaveCarregada
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class) // faz replace da factory em producao
    internal class MockitoStubFactory {

        @Singleton
        fun stubCarrega() = Mockito.mock(KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub::class.java)
    }
}