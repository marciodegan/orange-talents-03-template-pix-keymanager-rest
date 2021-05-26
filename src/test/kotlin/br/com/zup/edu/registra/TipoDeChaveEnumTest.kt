package br.com.zup.edu.registra

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveEnumTest {


    @Nested
    inner class ChaveCPFTest {

        @Test
        fun `deve ser valido qdo cpf for um numero valido`() {

            val tipoChave = TipoDeChaveEnum.CPF

            assertTrue(tipoChave.valida("81971354015"))
            assertFalse(tipoChave.valida(""))
        }

        @Test
        fun `nao deve ser valido qdo cpf for um numero invalido ou null ou vazio`() {

            val tipoChave = TipoDeChaveEnum.CPF

            assertFalse(tipoChave.valida(""))
            assertFalse(tipoChave.valida("819.713.540-15"))

        }
    }

    @Nested
    inner class ChaveCelularTest {

        @Test
        fun `deve ser valido se chave celular for valida`() {

            val tipoChave = TipoDeChaveEnum.CELULAR

            assertTrue(tipoChave.valida("+558599999999"))
        }

        @Test
        fun `nao deve ser valido se chave celular for invalida ou null`() {

            val tipoChave = TipoDeChaveEnum.CELULAR

            assertFalse(tipoChave.valida("559490"))
            assertFalse(tipoChave.valida("(55)859999-9999"))
            assertFalse(tipoChave.valida(""))
        }
    }

    @Nested
    inner class ChaveEmailTest {

        @Test
        fun `deve ser valido se email for valido`() {

            val tipoChave = TipoDeChaveEnum.EMAIL
            assertTrue(tipoChave.valida("teste@teste.com"))

        }

        @Test
        fun `não deve ser valido se email for invalido ou nulo ou vazio`() {

            val tipoChave = TipoDeChaveEnum.EMAIL

            assertFalse(tipoChave.valida("kopfds.com"))
            assertFalse(tipoChave.valida("teste@teste@teste.com"))
            assertFalse(tipoChave.valida("teste@test,com"))
            assertFalse(tipoChave.valida(""))
            assertFalse(tipoChave.valida(null))
        }

    }

    @Nested
    inner class ChaveAleatoriaTest {

        @Test
        fun `deve ser valido qdo chave aleatoria for null ou vazia`() {

            val tipoChave = TipoDeChaveEnum.ALEATORIA

            assertTrue(tipoChave.valida(null))
            assertTrue(tipoChave.valida(""))
        }

        @Test
        fun `nao deve ser valido qdo chave aleatoria possuir um valor`() {
            val tipoChave = TipoDeChaveEnum.ALEATORIA

            assertFalse(tipoChave.valida("não vazio"))
        }
    }
}