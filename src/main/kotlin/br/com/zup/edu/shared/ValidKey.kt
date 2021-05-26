package br.com.zup.edu

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidKey(
    val message: String = "chave pix inválida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidKey, NovaChaveRequest> { // lembrar: este ConstraintValidator vem do pacote do Micronaut
    override fun isValid(
        value: NovaChaveRequest?,
        annotationMetadata: AnnotationValue<ValidKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value?.tipoChave == null) {
            return false
        }
        return value.tipoChave.valida(value.chave) // chama o método valida de acordo com o tipo passado
    }
}