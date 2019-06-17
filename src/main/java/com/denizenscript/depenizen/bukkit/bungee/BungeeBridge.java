package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.properties.bungee.BungeePlayerProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

import java.net.SocketAddress;

public class BungeeBridge {

    public static BungeeBridge instance;

    public static class NettyExceptionHandler extends ChannelDuplexHandler {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            dB.echoError(cause);
        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            ctx.connect(remoteAddress, localAddress, promise.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (!future.isSuccess()) {
                        dB.echoError(future.cause());
                    }
                }
            }));
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            ctx.write(msg, promise.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (!future.isSuccess()) {
                        dB.echoError(future.cause());
                    }
                }
            }));
        }
    }

    public static class ClientHandler extends ChannelInboundHandlerAdapter {

        public static final byte[] FAKE_HANDSHAKE = new byte[] {
                        5 + 5 + 3, // Packet length prefix
                        0, // Packet ID 0
                        20, // "Protocol 20"
                        1 + 1 + 5, // 1-byte host name, 1 byte null, 5 bytes 'depen'
                        68, // Host name is the letter 'D'
                        0, // null byte to sneak in our identifier
                        (byte)'d', (byte)'e', (byte)'p', (byte)'e', (byte)'n', // Special identifier 'depen'
                        80, 0, // "We're connecting to port 80"
                        1 // We request protocol 1: ping status (to avoid uneeded proxy functionality)
                };

        public ByteBuf tmp;

        public void fail(String reason) {
            dB.echoError("Depenizen-Bungee connection failed: " + reason);
            channel.close();
        }

        public static enum Stage {
            AWAIT_HEADER,
            AWAIT_DATA
        }

        public Channel channel;

        public int waitingLength;

        public int packetId;

        public Stage currentStage = Stage.AWAIT_HEADER;

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            tmp = ctx.alloc().buffer(4);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            dB.log("Depenizen-Bungee connection ended.");
            tmp.release();
            tmp = null;
            // TODO: trigger reconnect (after a delay)
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            dB.log("Depenizen-Bungee bridge sending handshake...");
            // Send a fake minecraft handshake to get us in
            ByteBuf handshake = ctx.alloc().buffer(FAKE_HANDSHAKE.length);
            handshake.writeBytes(FAKE_HANDSHAKE);
            ctx.writeAndFlush(handshake); // Will release handshake
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf m = (ByteBuf) msg;
            tmp.writeBytes(m);
            m.release();
            if (currentStage == Stage.AWAIT_HEADER) {
                if (tmp.readableBytes() >= 8) {
                    waitingLength = tmp.readInt();
                    packetId = tmp.readInt();
                    currentStage = Stage.AWAIT_DATA;
                    /*if (!DepenizenBungee.instance.packets.containsKey(packetId)) {
                        fail("Invalid packet id: " + packetId);
                        return;
                    }*/
                }
            }
            if (currentStage == Stage.AWAIT_DATA) {
                if (tmp.readableBytes() >= waitingLength) {
                    try {
                        //DepenizenBungee.instance.packets.get(packetId).process(this, tmp);
                        currentStage = Stage.AWAIT_HEADER;
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

    public Channel channel;

    public NioEventLoopGroup workerGroup;

    public String address;

    public int port;

    public void sendPacket(Packet packet) {
        ByteBuf buf = channel.alloc().buffer();
        packet.writeTo(buf);
        ByteBuf header = channel.alloc().buffer();
        header.writeInt(buf.writerIndex());
        header.writeInt(packet.getPacketId());
        channel.write(header);
        channel.writeAndFlush(buf);
    }

    public void init(String address, int port) {
        this.address = address;
        this.port = port;
        workerGroup = new NioEventLoopGroup();
        connect();
        successInit();
    }

    public void connect() {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ClientHandler handler = new ClientHandler();
                handler.channel = ch;
                ch.pipeline().addLast(handler).addLast(new NettyExceptionHandler());
                channel = ch;
            }
        });
        b.connect(address, port);
    }

    public void successInit() {
        instance = this;
        PropertyParser.registerProperty(BungeePlayerProperties.class, dPlayer.class);
    }
}
