package br.com.zup.edu.registra

import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.RegistraChavePixResponse
import br.com.zup.edu.shared.GrpcClientFactory
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest {

    @field:Inject
    lateinit var keyManagerGrpc: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub // injetando o mock do mockito com o @replaces abaixo. Não o objeto real.

    @field:Inject @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChavePixResponse
            .newBuilder()
            .setClienteId(clientId)
            .setPixId(pixId)
            .build()

        given(keyManagerGrpc.registra(Mockito.any())).willReturn(respostaGrpc)
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java)

    }

}