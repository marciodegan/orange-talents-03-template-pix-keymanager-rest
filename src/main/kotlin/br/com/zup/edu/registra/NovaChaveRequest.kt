package br.com.zup.edu

import br.com.zup.edu.registra.TipoDeChaveEnum
import br.com.zup.edu.registra.TipoDeContaEnum
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ValidKey
@Introspected
data class NovaChaveRequest(@field:Size(max=77) val chave: String?,
                            @field:NotBlank val tipoChave: TipoDeChaveEnum?,
                            @field:NotBlank val tipoConta: TipoDeContaEnum?
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

