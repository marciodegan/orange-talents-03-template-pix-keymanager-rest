package br.com.zup.edu.shared

import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keymanager") val channel: ManagedChannel) {

        @Singleton
        fun registraChave() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

}