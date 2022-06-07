package org.valkyrienskies.mod.forge.common

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.simple.SimpleChannel
import org.valkyrienskies.mod.common.VSNetworking
import org.valkyrienskies.mod.common.ValkyrienSkiesMod

object VSForgeNetworking {

    private const val protocolVersion = "1"
    val vsForgeChannel: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation(ValkyrienSkiesMod.MOD_ID, "vs_packet"),
        { protocolVersion },
        { anObject: String? ->
            protocolVersion == anObject
        },
        { anObject: String? ->
            protocolVersion == anObject
        }
    )

    internal fun registerForgeNetworking() {
        registerClientPacketHandlers()
        injectForgePacketSenders()
    }

    private fun registerClientPacketHandlers() {
        // This gibberish is brought to you by forge
        @Suppress("INACCESSIBLE_TYPE")
        vsForgeChannel.registerMessage(
            0,
            MessageVSPacket::class.java,
            { messageVSPacket, packetBuffer ->
                run {
                    VSNetworking.writeVSPacket(messageVSPacket.vsPacket, packetBuffer)
                }
            },
            { packetBuffer: FriendlyByteBuf -> MessageVSPacket(VSNetworking.readVSPacket(packetBuffer)) },
            { vsPacket, contextSupplier ->
                run {
                    contextSupplier.get().enqueueWork {
                        VSNetworking.handleVSPacketClient(vsPacket.vsPacket)
                    }
                    contextSupplier.get().packetHandled = true
                }
            }
        )
    }

    private fun injectForgePacketSenders() {
        VSNetworking.shipDataPacketToClientSender = VSForgeServerToClientPacketSender
    }
}
