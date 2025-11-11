package me.yleoft.zTPA;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

@Plugin(
    id = "ztpa",
    name = "zTPA",
    version = "1.0"
    ,authors = {"yLeoft"}
)
public class zTPAVelocity {

    @Inject private Logger logger;
    @Inject private ProxyServer proxyServer;

    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("ztpa");

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getChannelRegistrar().register(IDENTIFIER);
        logger.info("Registered plugin message channel: " + IDENTIFIER);
    }
}
