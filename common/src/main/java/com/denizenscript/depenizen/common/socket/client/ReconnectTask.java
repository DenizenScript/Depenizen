package com.denizenscript.depenizen.common.socket.client;

import com.denizenscript.depenizen.common.Depenizen;

import java.io.IOException;

public class ReconnectTask implements Runnable {

    private SocketClient client;
    private int attemptsLeft;
    private long reconnectDelay;

    public ReconnectTask(SocketClient client, int attempts, long reconnectDelay) {
        this.client = client;
        this.attemptsLeft = attempts;
        this.reconnectDelay = reconnectDelay;
    }

    @Override
    public void run() {
        while (attemptsLeft > 0 && !client.isConnected()) {
            try {
                Thread.sleep(reconnectDelay);
            }
            catch (InterruptedException e) {
                Depenizen.getImplementation().debugError("BungeeCord Socket failed to reconnect due to an exception.");
                Depenizen.getImplementation().debugException(e);
            }
            try {
                attemptsLeft--;
                client.connect();
            }
            catch (IOException e) {
                if (attemptsLeft <= 0) {
                    Depenizen.getImplementation().debugError("Failed to reconnect to BungeeCord Socket.");
                    client.fireReconnectFailEvent();
                }
            }
        }
    }
}
