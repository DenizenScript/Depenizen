package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.bungee.packets.in.*;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ControlsProxyPingPacketOut;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeRunCommand;
import com.denizenscript.depenizen.bukkit.events.bungee.*;
import com.denizenscript.depenizen.bukkit.properties.bungee.BungeePlayerProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BungeeBridge {

    public static BungeeBridge instance;

    public Channel channel;

    public NioEventLoopGroup workerGroup;

    public BungeeClientHandler handler;

    public String address;

    public int port;

    public String serverName = "<Unknown>";

    public List<String> knownServers = new ArrayList<>();

    public HashMap<Integer, PacketIn> packets = new HashMap<>();

    public boolean connected = false;

    public boolean controlsProxyPing = false;

    public void checkBroadcastProxyPing() {
        if (connected) {
            sendPacket(new ControlsProxyPingPacketOut(controlsProxyPing));
        }
    }

    public void registerPackets() {
        packets.put(50, new YourInfoPacketIn());
        packets.put(51, new AddServerPacketIn());
        packets.put(52, new RemoveServerPacketIn());
        packets.put(53, new PlayerJoinPacketIn());
        packets.put(54, new PlayerQuitPacketIn());
        packets.put(55, new PlayerSwitchServerPacketIn());
        packets.put(56, new ProxyPingPacketIn());
        packets.put(57, new RunScriptPacketIn());
    }

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
        registerPackets();
        connect();
        successInit();
    }

    public void connect() {
        knownServers.clear();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                handler = new BungeeClientHandler();
                handler.channel = ch;
                ch.pipeline().addLast(handler).addLast(new NettyExceptionHandler());
                channel = ch;
            }
        });
        b.connect(address, port);
    }

    public void successInit() {
        instance = this;
        ScriptEvent.registerScriptEvent(new BungeePlayerJoinsScriptEvent());
        ScriptEvent.registerScriptEvent(new BungeePlayerQuitsScriptEvent());
        ScriptEvent.registerScriptEvent(new BungeePlayerServerSwitchScriptEvent());
        ScriptEvent.registerScriptEvent(new BungeeProxyServerListPingScriptEvent());
        ScriptEvent.registerScriptEvent(new BungeeServerConnectScriptEvent());
        ScriptEvent.registerScriptEvent(new BungeeServerDisconnectScriptEvent());
        PropertyParser.registerProperty(BungeePlayerProperties.class, dPlayer.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(BungeeRunCommand.class, "BUNGEERUN",
                "bungeerun [<server>|...] [<script name>] (def:<definition>|...)", 1);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "bungee");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <bungee.server>
        // @returns Element
        // @description
        // Returns the name of the current server (according to the Bungee proxy config).
        // @Plugin Depenizen, BungeeCord
        // -->
        if (attribute.startsWith("server")) {
            event.setReplacedObject(new Element(serverName)
                    .getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <bungee.list_servers>
        // @returns dList
        // @description
        // Returns a list of known bungee server names.
        // @Plugin Depenizen, BungeeCord
        // -->
        if (attribute.startsWith("list_servers")) {
            event.setReplacedObject(new dList(knownServers)
                    .getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
