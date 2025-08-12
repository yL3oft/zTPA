package com.zTPA.api.examples;

import com.zTPA.api.event.player.AcceptTeleportRequestEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ExampleEvent implements Listener {

    @EventHandler
    public void onAcceptTeleportRequestEvent(AcceptTeleportRequestEvent event) {
        Player player = event.getPlayer();
        Player target = event.getTarget();
        // Checks if the player and target are specific players
        if(player.getName().equals("yLeoft") && target.getName().equals("yLeoft2")) {
            event.setCancelled(true);
        }
    }

}
