package org.valkyrienskies.mod.fabric.common

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.FriendlyByteBuf
import org.valkyrienskies.mod.common.VSNetworking

object VSClientPlayChannelHandler : ClientPlayNetworking.PlayChannelHandler {

    override fun receive(
        client: Minecraft,
        handler: ClientPacketListener,
        buf: FriendlyByteBuf,
        responseSender: PacketSender
    ) {
        VSNetworking.handleVSPacketClient(buf)
    }
}
