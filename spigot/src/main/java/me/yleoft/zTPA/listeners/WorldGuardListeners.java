package me.yleoft.zTPA.listeners;

import com.zTPA.api.event.player.AcceptTeleportRequestEvent;
import com.zTPA.api.event.player.DenyTeleportRequestEvent;
import com.zTPA.api.event.player.SendTeleportRequestEvent;
import me.yleoft.zTPA.utils.LanguageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static me.yleoft.zTPA.zTPABukkit.*;

public class WorldGuardListeners extends me.yleoft.zTPA.utils.WorldGuardUtils implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSendTeleportRequestEvent(SendTeleportRequestEvent e) {
        Player p = e.getPlayer();
        LanguageUtils.HooksMSG hooks = new LanguageUtils.HooksMSG();

        if (!getFlagStateAtPlayer(p, sendTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardSendTpa());
        }else if (!getFlagStateAtPlayer(p, useTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardUseTpa());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAcceptTeleportRequestEvent(AcceptTeleportRequestEvent e) {
        Player p = e.getPlayer();
        LanguageUtils.HooksMSG hooks = new LanguageUtils.HooksMSG();

        if (!getFlagStateAtPlayer(p, acceptTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardAcceptTpa());
        }else if (!getFlagStateAtPlayer(p, useTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardUseTpa());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDenyTeleportRequestEvent(DenyTeleportRequestEvent e) {
        Player p = e.getPlayer();
        LanguageUtils.HooksMSG hooks = new LanguageUtils.HooksMSG();

        if (!getFlagStateAtPlayer(p, denyTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardDenyTpa());
        }else if (!getFlagStateAtPlayer(p, useTPAFlag)) {
            e.setCancelled(true);
            hooks.sendMsg(p, hooks.getWorldGuardUseTpa());
        }
    }

}
