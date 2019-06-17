package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.properties.bungee.BungeePlayerProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;

public class BungeeBridge {

    public static BungeeBridge instance;

    public Channel channel;

    public NioEventLoopGroup workerGroup;

    public String address;

    public int port;

    public void sendPacket(PacketOut packet) {
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
                BungeeClientHandler handler = new BungeeClientHandler();
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
