package br.com.zup.edu

import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidKey
@Introspected
data class NovaChaveRequest(@field:Size(max=77) val chave: String?,
                            @field:NotBlank val tipoChave: TipoDeChaveRequest?,
                            @field:NotBlank val tipoConta: TipoDeContaRequest?
) {
    fun toGrpcModel(clienteId: UUID): RegistraChavePixRequest? {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setChave(chave ?: "")
            .setTipoDeChave(tipoChave?.atributoGrpc ?: TipoDeChave.UNKNOWN_TIPO_CHAVE) //converte para o enum do keymanager
            .setTipoDeConta(tipoConta?.atributoGrpc ?: TipoDeConta.UNKNOWN_TIPO_CONTA) //converte para o enum do keymanager
            .build()
    }
}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChave) {

    CPF(TipoDeChave.CPF) {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()) return false

            if(!chave.matches(regex = Regex("^[0-9]{11}\$"))) return false

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CELULAR(TipoDeChave.CELULAR) {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(TipoDeChave.EMAIL) {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()){
                return false
            }
            return  EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },

    ALEATORIA(TipoDeChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank()
    };

    /* método abstrato que recebe a chave e retorna se é valida ou não.
    obriga cada enum implementá-lo, permitindo uma regra específica p/ cada */
    abstract fun valida(chave: String?): Boolean
}


enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta) {
    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)

}