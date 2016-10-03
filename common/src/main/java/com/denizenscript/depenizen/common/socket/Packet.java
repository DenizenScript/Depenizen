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

        PING(0);

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

        PING(0);

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
