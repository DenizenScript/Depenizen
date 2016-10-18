package com.denizenscript.depenizen.common.socket;

import java.util.HashMap;
import java.util.Map;

public abstract class Packet {

    public void serialize(DataSerializer serializer) {
        throw new UnsupportedOperationException("Cannot serialize an inbound packet.");
    }

    public void deserialize(DataDeserializer deserializer) {
        throw new UnsupportedOperationException("Cannot deserialize an outbound packet.");
    }

    public enum ServerBound {

        REGISTER(0),
        PING(1),
        PONG(2),
        SCRIPT(3),
        RUN_SCRIPT(4),
        TAG(5),
        PARSED_TAG(6),
        EVENT_SUBSCRIPTION(7),
        EVENT_RESPONSE(8),
        SEND_PLAYER(9);

        private final int id;

        ServerBound(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        private static final Map<Integer, ServerBound> byId = new HashMap<Integer, ServerBound>();

        static {
            for (ServerBound serverBound : values()) {
                byId.put(serverBound.getId(), serverBound);
            }
        }

        public static ServerBound getById(int id) {
            return byId.containsKey(id) ? byId.get(id) : null;
        }
    }

    public enum ClientBound {

        ACCEPT_REGISTER(0),
        PING(1),
        PONG(2),
        UPDATE_SERVER(3),
        SCRIPT(4),
        RUN_SCRIPT(5),
        TAG(6),
        PARSED_TAG(7),
        EVENT(8);

        private final int id;

        ClientBound(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        private static final Map<Integer, ClientBound> byId = new HashMap<Integer, ClientBound>();

        static {
            for (ClientBound serverBound : values()) {
                byId.put(serverBound.getId(), serverBound);
            }
        }

        public static ClientBound getById(int id) {
            return byId.containsKey(id) ? byId.get(id) : null;
        }
    }
}
