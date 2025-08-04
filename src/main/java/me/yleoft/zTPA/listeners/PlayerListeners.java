package me.yleoft.zTPA.listeners;

import me.yleoft.zAPI.folia.FoliaRunnable;
import me.yleoft.zAPI.utils.ActionbarUtils;
import me.yleoft.zAPI.utils.SchedulerUtils;
import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.utils.ConfigUtils;
import me.yleoft.zTPA.utils.TpaUtils;
import me.yleoft.zTPA.zTPA;
import me.yleoft.zTPA.utils.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static me.yleoft.zTPA.zTPA.needsUpdate;

public class PlayerListeners extends ConfigUtils implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(needsUpdate && doAnnounceUpdate() && (p.isOp() || p.hasPermission(CmdMainVersionUpdatePermission()))) {
            LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
            SchedulerUtils.runTaskLater(p.getLocation(), () -> {
                cmdm.sendMsg(p, "%prefix%&6You are using an outdated version of zHomes! Please update to the latest version.");
                cmdm.sendMsg(p, "%prefix%&6New version: &a" + zTPA.getInstance().updateVersion);
                cmdm.sendMsg(p, "%prefix%&6Your version: &c" + zTPA.getInstance().getDescription().getVersion());
                cmdm.sendMsg(p, "%prefix%&6You can update your plugin here: &e" + zTPA.getInstance().site);
            }, 60L);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if(zTPA.tpaRequests.containsKey(uuid)) {
            List<TeleportRequest> requests = zTPA.tpaRequests.get(uuid);
            for (TeleportRequest request : requests) {
                request.deleteRequest();
            }
        }
        if(zTPA.targetRequestMap.containsKey(uuid)) {
            List<TeleportRequest> requests = zTPA.targetRequestMap.get(uuid);
            for (TeleportRequest request : requests) {
                request.deleteRequest();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if(TpaUtils.warmups.containsKey(uuid) && warmupCancelOnMove()) {
            LanguageUtils.TeleportWarmupMSG lang = new LanguageUtils.TeleportWarmupMSG();

            Location from = e.getFrom();
            Location to = e.getTo();
            if(to == null) return;
            if (from.getBlockX() != to.getBlockX()
                    || from.getBlockY() != to.getBlockY()
                    || from.getBlockZ() != to.getBlockZ()) {
                FoliaRunnable runnable = TpaUtils.warmups.remove(uuid);
                if (runnable != null) {
                    runnable.cancel();
                    lang.sendMsg(p, lang.getCancelled());
                    ActionbarUtils.send(p, LanguageUtils.Helper.getText(p, lang.getCancelledActionbar()));
                }
            }
        }
    }

}
