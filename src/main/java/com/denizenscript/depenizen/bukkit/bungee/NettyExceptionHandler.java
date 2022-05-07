package com.denizenscript.depenizen.bukkit.bungee;

import io.netty.channel.*;
import com.denizenscript.denizen.utilities.debugging.Debug;

import java.net.SocketAddress;

public class NettyExceptionHandler extends ChannelDuplexHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        BungeeBridge.runOnMainThread(() -> Debug.echoError(cause));
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                BungeeBridge.runOnMainThread(() -> Debug.echoError(future.cause()));
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                BungeeBridge.runOnMainThread(() -> Debug.echoError(future.cause()));
            }
        }));
    }
}
