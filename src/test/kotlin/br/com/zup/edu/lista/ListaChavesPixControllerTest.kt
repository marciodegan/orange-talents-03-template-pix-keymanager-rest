package br.com.zup.edu.lista

import br.com.zup.edu.*
import org.junit.jupiter.api.Assertions.*

import br.com.zup.edu.shared.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class  ListaChavesPixControllerTest {

    @field:Inject
    lateinit var listaChaveStub: KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CHAVE_EMAIL = "joao@joa.com"
    val CHAVE_CELULAR = "+558511111111"
    val CONTA_CORRENTE = TipoDeConta.CONTA_CORRENTE
    val TIPO_DE_CHAVE_EMAIL = TipoDeChave.EMAIL
    val TIPO_DE_CHAVE_CELULAR = TipoDeChave.CELULAR
    val INSTITUICAO = "ITAU"
    val TITULAR = "João da Silva"
    val DOCUMENTO_DO_TITULAR = "1223123123"
    val AGENCIA = "0001"
    val NUMERO_DA_CONTA = "1000-1"
    val CHAVE_CRIADA_EM = LocalDateTime.now()


    @Test
    internal fun `deve listar todas as chaves pix existente`() {

        val clienteId = UUID.randomUUID().toString()

        val respostaGrpc = listaChavePixResponse(clienteId)

        given(listaChaveStub.lista(Mockito.any())).willReturn(respostaGrpc)


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
        Assertions.assertEquals(response.body()!!.size, 2)
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
                            .setTipo(CONTA_CORRENTE)
                            .setInstituicao(INSTITUICAO)
                            .setNomeDoTitular(TITULAR)
                            .setCpfDoTitular(DOCUMENTO_DO_TITULAR)
                            .setAgencia(AGENCIA)
                            .setNumeroDaConta(NUMERO_DA_CONTA)
                            .build()
                    )
            ).build()
        return chaveCarregada
    }


    private fun listaChavePixResponse(clienteId: String): ListaChavesPixResponse {
        val chaveEmail = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TIPO_DE_CHAVE_EMAIL)
            .setChave(CHAVE_EMAIL)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCelular = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TIPO_DE_CHAVE_CELULAR)
            .setChave(CHAVE_CELULAR)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()


        return ListaChavesPixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(listOf(chaveEmail, chaveCelular))
            .build()

    }



    @Factory
    @Replaces(factory = GrpcClientFactory::class) // faz replace da factory em producao
    internal class MockitoStubFactory {

        @Singleton
        fun stubLista() = Mockito.mock(KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub::class.java)
    }

}