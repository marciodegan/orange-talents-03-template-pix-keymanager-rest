package br.com.zup.edu.registra

import br.com.zup.edu.TipoDeConta

enum class TipoDeContaEnum(val atributoGrpc: TipoDeConta) {
    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)

}