package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ControlsProxyCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ControlsProxyPingPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.MyInfoPacketOut;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.denizenscript.denizen.utilities.debugging.Debug;
import org.bukkit.Bukkit;

public class BungeeClientHandler extends ChannelInboundHandlerAdapter {

    public static final byte[] FAKE_HANDSHAKE = new byte[] {
            5 + 5 + 3, // Packet length prefix
            0, // Packet ID 0
            20, // "Protocol 20"
            1 + 1 + 5, // 1-byte host name, 1 byte null, 5 bytes 'depen'
            68, // Host name is the letter 'D'
            0, // null byte to sneak in our identifier
            (byte) 'd', (byte) 'e', (byte) 'p', (byte) 'e', (byte) 'n', // Special identifier 'depen'
            80, 0, // "We're connecting to port 80"
            1 // We request protocol 1: ping status (to avoid uneeded proxy functionality)
    };

    public ByteBuf packetBuffer;

    public void fail(String reason) {
        Debug.echoError("Depenizen-Bungee connection failed: " + reason);
        channel.close();
        BungeeBridge.instance.connected = false;
    }

    public enum Stage {
        AWAIT_HEADER,
        AWAIT_DATA
    }

    public Channel channel;

    public int waitingLength;

    public int packetId;

    public Stage currentStage = Stage.AWAIT_HEADER;

    public void reallocateBuf(ChannelHandlerContext ctx) {
        ByteBuf newBuf = ctx.alloc().buffer(32);
        if (packetBuffer != null) {
            newBuf.writeBytes(packetBuffer);
            packetBuffer.release();
        }
        packetBuffer = newBuf;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if (packetBuffer != null) {
            packetBuffer.release();
        }
        packetBuffer = ctx.alloc().buffer(32);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Debug.log("Depenizen-Bungee connection ended.");
        packetBuffer.release();
        packetBuffer = null;
        BungeeBridge.instance.connected = false;
        BungeeBridge.instance.reconnect();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Send a fake minecraft handshake to get us in
        ByteBuf handshake = ctx.alloc().buffer(FAKE_HANDSHAKE.length);
        handshake.writeBytes(FAKE_HANDSHAKE);
        ctx.writeAndFlush(handshake); // Will release handshake
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
            @Override
            public void run() {
                Debug.log("Depenizen now connected to Bungee server.");
                BungeeBridge.instance.lastPacketReceived = System.currentTimeMillis();
                BungeeBridge.instance.sendPacket(new MyInfoPacketOut(Bukkit.getPort()));
                BungeeBridge.instance.sendPacket(new ControlsProxyPingPacketOut(BungeeBridge.instance.controlsProxyPing));
                BungeeBridge.instance.sendPacket(new ControlsProxyCommandPacketOut(BungeeBridge.instance.controlsProxyCommand));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeBridge.instance.connected = true;
                    }
                }, 10);
            }
        }, 30);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        packetBuffer.writeBytes(m);
        m.release();
        if (currentStage == Stage.AWAIT_HEADER) {
            if (packetBuffer.readableBytes() >= 8) {
                waitingLength = packetBuffer.readInt();
                packetId = packetBuffer.readInt();
                currentStage = Stage.AWAIT_DATA;
                    if (!BungeeBridge.instance.packets.containsKey(packetId)) {
                        fail("Invalid packet id: " + packetId);
                        return;
                    }
            }
        }
        if (currentStage == Stage.AWAIT_DATA) {
            if (packetBuffer.readableBytes() >= waitingLength) {
                try {
                    BungeeBridge.instance.lastPacketReceived = System.currentTimeMillis();
                    PacketIn packet = BungeeBridge.instance.packets.get(packetId);
                    packet.process(packetBuffer);
                    currentStage = Stage.AWAIT_HEADER;
                    reallocateBuf(ctx);
                }
                catch (Throwable ex) {
                    ex.printStackTrace();
                    fail("Internal exception.");
                    return;
                }
            }
        }
    }
}
