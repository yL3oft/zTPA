package me.yleoft.zTPA.listeners;

import me.yleoft.zTPA.utils.ConfigUtils;
import me.yleoft.zTPA.zTPA;
import me.yleoft.zTPA.utils.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.yleoft.zTPA.zTPA.needsUpdate;

public class PlayerListeners extends ConfigUtils implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(needsUpdate && doAnnounceUpdate() && (p.isOp() || p.hasPermission(CmdMainVersionUpdatePermission()))) {
            LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
            Bukkit.getScheduler().runTaskLater(zTPA.getInstance(), () -> {
                cmdm.sendMsg(p, "%prefix%&6You are using an outdated version of zHomes! Please update to the latest version.");
                cmdm.sendMsg(p, "%prefix%&6New version: &a" + zTPA.getInstance().updateVersion);
                cmdm.sendMsg(p, "%prefix%&6Your version: &c" + zTPA.getInstance().getDescription().getVersion());
                cmdm.sendMsg(p, "%prefix%&6You can update your plugin here: &e" + zTPA.getInstance().site);
            }, 60L);
        }
    }

}
