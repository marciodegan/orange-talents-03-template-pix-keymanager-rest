package br.com.zup.edu.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@MicronautTest
internal class GlobalExceptionHandlerTest{

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    companion object{
        val genericRequest = HttpRequest.GET<Any>("/")
    }

    @Test
    internal fun `deve retornar 404 se statusException for NOT_FOUND`(){

        val mensagem = "qualquer mensagem"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))
        LOGGER.info("${notFoundException.message}")

        val handledExcetion = GlobalExceptionHandler().handle(genericRequest, notFoundException)
        LOGGER.info("RESPONSE => ${handledExcetion}")

        assertEquals(HttpStatus.NOT_FOUND, handledExcetion.status)
        assertNotNull(handledExcetion.body()) // existe um body?
        assertEquals(mensagem, (handledExcetion.body() as JsonError).message) // body contém "nao encontrado"?
    }

    @Test
    internal fun `deve retornar 400 SE statusException for INVALID_ARGUMENT`() {

        val mensagem = "Dados da sua requisição são inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT)

        val handledException = GlobalExceptionHandler().handle(genericRequest, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, handledException.status)
        assertNotNull(handledException.body())
        assertEquals(mensagem, (handledException.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 422 se statusException for ALREADY_EXISTS`() {

        val mensagem = "qualquer mensagem"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        val handledException = GlobalExceptionHandler().handle(genericRequest, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, handledException.status) // 422
        assertNotNull(handledException.body())
        assertEquals(mensagem, (handledException.body() as JsonError).message)
    }


    @Test
    internal fun `deve retornar 500 SE qualquer outro erro for lancado`() {

        val internalException = StatusRuntimeException(Status.INTERNAL)

        val handledException = GlobalExceptionHandler().handle(genericRequest, internalException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handledException.status)
        assertNotNull(handledException.body())
        assertTrue((handledException.body() as JsonError).message.contains("INTERNAL"))
    }
}