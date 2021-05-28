package br.com.zup.edu.shared

import br.com.zup.edu.KeyManagerCarregaGrpcServiceGrpc
import br.com.zup.edu.KeyManagerListaGrpcServiceGrpc
import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keymanager") val channel: ManagedChannel) {

        @Singleton
        fun registraChave() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

        @Singleton
        fun removeChave() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

        @Singleton
        fun carregaChave() = KeyManagerCarregaGrpcServiceGrpc.newBlockingStub(channel)

        @Singleton
        fun listaChaves() = KeyManagerListaGrpcServiceGrpc.newBlockingStub(channel)

}