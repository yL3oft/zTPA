package me.yleoft.zTPA.utils;

import me.yleoft.zAPI.utils.ActionbarUtils;
import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.zTPA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TpaUtils extends ConfigUtils {

    public static HashMap<UUID, BukkitRunnable> warmups = new HashMap<>();

    public List<TeleportRequest> getRequests(UUID uuid) {
        return zTPA.tpaRequests.get(uuid);
    }

    public List<TeleportRequest> getTargetRequests(UUID uuid) {
        return zTPA.targetRequestMap.get(uuid);
    }

    public void teleportPlayer(Player p, Player t) {
        LanguageUtils.Tpaccept lang = new LanguageUtils.Tpaccept();
        Runnable task = () -> {
            Sound sound = getTeleportSound();
            if (zTPA.getInstance().getServer().getName().contains("Folia")) {
                try {
                    Method teleportAsyncMethod = Player.class.getMethod("teleportAsync", Location.class);
                    teleportAsyncMethod.invoke(p, t.getLocation());
                    if (sound != null && playSound()) p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Unable to teleport player to home", e);
                }
                return;
            }
            Bukkit.getScheduler().runTaskLater(zTPA.getInstance(), () -> {
                p.teleport(t.getLocation());
                if (sound != null && playSound()) p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
            }, 1L);
        };
        if(doWarmup() && !p.hasPermission(PermissionBypassWarmup()) && warmupTime() > 0) {
            LanguageUtils.TeleportWarmupMSG langWarmup = new LanguageUtils.TeleportWarmupMSG();
            startWarmup(p, langWarmup, lang, t.getName(), task);
            return;
        }
        task.run();
    }

    public static Sound getTeleportSound() {
        try {
            return Sound.valueOf("ENTITY_ENDERMAN_TELEPORT");
        } catch (Throwable ignored1) {
            try {
                return Sound.valueOf("ENDERMAN_TELEPORT");
            } catch (Throwable ignored2) {
                return null;
            }
        }
    }

    public void startWarmup(Player p, LanguageUtils.TeleportWarmupMSG lang, LanguageUtils.Tpaccept lang2, String target, Runnable task) {
        UUID uuid = p.getUniqueId();
        if (warmups.containsKey(uuid)) {
            warmups.get(uuid).cancel();
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            int counter = warmupTime();

            @Override
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    warmups.remove(uuid);
                    return;
                }

                if (counter >= 1) {
                    if(warmupShowOnActionbar()) {
                        ActionbarUtils.send(p, LanguageUtils.Helper.getText(p, lang.getWarmupActionbar(counter)));
                    }
                    counter--;
                } else {
                    task.run();
                    warmups.remove(uuid);
                    cancel();
                }
            }
        };

        warmups.put(uuid, runnable);
        runnable.runTaskTimer(zTPA.getInstance(), 0L, 20L);
    }

}
