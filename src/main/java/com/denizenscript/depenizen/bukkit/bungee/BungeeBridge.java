package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.packets.in.*;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ControlsProxyCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ControlsProxyPingPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeCommand;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeExecuteCommand;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeRunCommand;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeTagCommand;
import com.denizenscript.depenizen.bukkit.events.bungee.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.denizenscript.denizencore.events.ScriptEvent.*;

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

    public boolean controlsProxyCommand = false;

    public int keepAliveTickRate = 10;

    public int ticksTilKeepalive = 10;

    public long lastPacketReceived = 0;

    public void checkBroadcastProxyPing() {
        if (connected) {
            sendPacket(new ControlsProxyPingPacketOut(controlsProxyPing));
        }
    }

    public void checkBroadcastProxyCommand() {
        if (connected) {
            sendPacket(new ControlsProxyCommandPacketOut(controlsProxyCommand));
        }
    }

    public void registerPackets() {
        packets.put(1, new KeepAlivePacketIn());
        packets.put(50, new YourInfoPacketIn());
        packets.put(51, new AddServerPacketIn());
        packets.put(52, new RemoveServerPacketIn());
        packets.put(53, new PlayerJoinPacketIn());
        packets.put(54, new PlayerQuitPacketIn());
        packets.put(55, new PlayerSwitchServerPacketIn());
        packets.put(56, new ProxyPingPacketIn());
        packets.put(57, new RunScriptPacketIn());
        packets.put(58, new RunCommandsPacketIn());
        packets.put(59, new ReadTagPacketIn());
        packets.put(60, new TagResponsePacketIn());
        packets.put(61, new ProxyCommandPacketIn());
    }

    public void sendPacket(PacketOut packet) {
        if (!connected && !packet.canBeFirstPacket) {
            Debug.echoError("BungeeBridge tried to send packet while not connected.");
            return;
        }
        ByteBuf buf = channel.alloc().buffer();
        packet.writeTo(buf);
        ByteBuf header = channel.alloc().buffer();
        header.writeInt(buf.writerIndex());
        header.writeInt(packet.getPacketId());
        channel.writeAndFlush(header);
        channel.writeAndFlush(buf);
        if (connected) {
            ticksTilKeepalive = 0;
        }
    }

    public void init(String address, int port) {
        this.address = address;
        this.port = port;
        workerGroup = new NioEventLoopGroup();
        registerPackets();
        connect();
        successInit();
    }

    public boolean reconnectPending = false;

    public void reconnect() {
        if (reconnectPending || shuttingDown) {
            return;
        }
        connected = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
            @Override
            public void run() {
                reconnectPending = false;
                connect();
            }
        }, 20 * 5);
    }

    public boolean shuttingDown = false;

    public void onShutdown() {
        shuttingDown = true;
        if (connected) {
            try {
                handler.channel.close().await();
                connected = false;
            }
            catch (Throwable ex) {
                Debug.echoError(ex);
            }
        }
    }

    private boolean showedLastError = false;

    private boolean hasConnectionLoading = false;

    public void connect() {
        try {
            ticksTilKeepalive = 100;
            hasConnectionLoading = false;
            knownServers.clear();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    hasConnectionLoading = true;
                    handler = new BungeeClientHandler();
                    handler.channel = ch;
                    ch.pipeline().addLast(handler).addLast(new NettyExceptionHandler());
                    channel = ch;
                }
            });
            b.connect(address, port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                        @Override
                        public void run() {
                            if (!connected && !hasConnectionLoading) {
                                reconnect();
                            }
                        }
                    }, 10);
                }
            });
            showedLastError = false;
        }
        catch (Throwable ex) {
            if (!showedLastError) {
                showedLastError = true;
                Debug.echoError(ex);
            }
            reconnect();
        }
    }

    public void successInit() {
        instance = this;
        registerScriptEvent(new BungeePlayerJoinsScriptEvent());
        registerScriptEvent(new BungeePlayerQuitsScriptEvent());
        registerScriptEvent(new BungeePlayerServerSwitchScriptEvent());
        registerScriptEvent(new BungeeProxyServerCommandScriptEvent());
        registerScriptEvent(new BungeeProxyServerListPingScriptEvent());
        registerScriptEvent(new BungeeServerConnectScriptEvent());
        registerScriptEvent(new BungeeServerDisconnectScriptEvent());
        Denizen.getInstance().getCommandRegistry().registerCommand(BungeeRunCommand.class);
        Denizen.getInstance().getCommandRegistry().registerCommand(BungeeExecuteCommand.class);
        Denizen.getInstance().getCommandRegistry().registerCommand(BungeeCommand.class);
        Denizen.getInstance().getCommandRegistry().registerCommand(BungeeTagCommand.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "bungee");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Depenizen.instance, new Runnable() {
            @Override
            public void run() {
                if (!connected) {
                    return;
                }
                if (System.currentTimeMillis() > lastPacketReceived + 20 * 1000) {
                    // 20 seconds without a packet = connection lost!
                    handler.fail("Connection time out.");
                    return;
                }
                ticksTilKeepalive--;
                if (ticksTilKeepalive <= 0) {
                    sendPacket(new KeepAlivePacketOut());
                    ticksTilKeepalive = keepAliveTickRate;
                }
            }
        }, 1, 1);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <bungee.server>
        // @returns ElementTag
        // @plugin Depenizen, DepenizenBungee, BungeeCord
        // @description
        // Returns the name of the current server (according to the Bungee proxy config).
        // -->
        if (attribute.startsWith("server")) {
            event.setReplacedObject(new ElementTag(serverName)
                    .getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <bungee.list_servers>
        // @returns ListTag
        // @plugin Depenizen, DepenizenBungee, BungeeCord
        // @description
        // Returns a list of known bungee server names.
        // -->
        if (attribute.startsWith("list_servers")) {
            event.setReplacedObject(new ListTag(knownServers)
                    .getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <bungee.connected>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, DepenizenBungee, BungeeCord
        // @description
        // Returns whether this server is currently connected to the BungeeCord proxy server.
        // -->
        if (attribute.startsWith("connected")) {
            event.setReplacedObject(new ElementTag(connected)
                    .getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
