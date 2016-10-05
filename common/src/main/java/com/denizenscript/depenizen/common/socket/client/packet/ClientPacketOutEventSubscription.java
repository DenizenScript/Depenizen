package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutEventSubscription extends Packet {

    private String event;
    private boolean subscribed;

    public ClientPacketOutEventSubscription(String event, boolean subscribed) {
        this.event = event;
        this.subscribed = subscribed;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.EVENT_SUBSCRIPTION.getId());
        serializer.writeString(event);
        serializer.writeBoolean(subscribed);
    }
}
