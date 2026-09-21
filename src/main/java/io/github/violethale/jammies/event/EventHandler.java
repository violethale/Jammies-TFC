package io.github.violethale.jammies.event;

import io.github.violethale.jammies.Jammies;
import io.github.violethale.jammies.network.ModDataManagerSyncPacket;
import io.github.violethale.jammies.common.data.ModDataManagers;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class EventHandler {

    public static void addReloadListeners(AddReloadListenerEvent event) {
        ModDataManagers.REGISTRY.forEach(event::addListener);
    }

    public static void onDataPackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            PacketDistributor.sendToAllPlayers(new ModDataManagerSyncPacket());
        } else {
            PacketDistributor.sendToPlayer(event.getPlayer(), new ModDataManagerSyncPacket());
        }
    }

    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Jammies.MOD_ID);

        registrar.playToClient(ModDataManagerSyncPacket.TYPE, ModDataManagerSyncPacket.CODEC, (packet, context) -> context.enqueueWork(() -> packet.handle(context.connection().isMemoryConnection())));
    }

}
