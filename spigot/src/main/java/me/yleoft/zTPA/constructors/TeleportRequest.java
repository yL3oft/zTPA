package me.yleoft.zTPA.constructors;

import com.zTPA.api.event.player.AcceptTeleportRequestEvent;
import com.zTPA.api.event.player.CancelTeleportRequestEvent;
import com.zTPA.api.event.player.DenyTeleportRequestEvent;
import com.zTPA.api.event.player.SendTeleportRequestEvent;
import me.yleoft.zAPI.folia.FoliaRunnable;
import me.yleoft.zAPI.utils.SchedulerUtils;
import me.yleoft.zTPA.utils.LanguageUtils;
import me.yleoft.zTPA.utils.TpaUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static me.yleoft.zTPA.zTPABukkit.targetRequestMap;
import static me.yleoft.zTPA.zTPABukkit.tpaRequests;

public class TeleportRequest extends TpaUtils {

    private static LanguageUtils.Tpa lang = new LanguageUtils.Tpa();
    private static LanguageUtils.Tpaccept lang2 = new LanguageUtils.Tpaccept();
    private static LanguageUtils.Tpdeny lang3 = new LanguageUtils.Tpdeny();
    private static LanguageUtils.Tpacancel lang4 = new LanguageUtils.Tpacancel();

    public static void setTpaLang(LanguageUtils.Tpa lang) {
        TeleportRequest.lang = lang;
    }
    public static void setTpacceptLang(LanguageUtils.Tpaccept lang) {
        TeleportRequest.lang2 = lang;
    }public static void setTpdenyLang(LanguageUtils.Tpdeny lang) {
        TeleportRequest.lang3 = lang;
    }
    public static void setTpacancelLang(LanguageUtils.Tpacancel lang) {
        TeleportRequest.lang4 = lang;
    }

    protected UUID senderUUID;
    protected UUID targetUUID;

    protected Player sender;
    protected Player target;
    protected long requestTime;
    protected FoliaRunnable runnable;

    public TeleportRequest(Player sender, Player target) {
        SendTeleportRequestEvent event = new SendTeleportRequestEvent(sender, target);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        target = event.getTarget();

        this.sender = sender;
        this.target = target;
        this.senderUUID = sender.getUniqueId();
        this.targetUUID = target.getUniqueId();
        this.requestTime = System.currentTimeMillis();
        lang.sendMsg(sender, lang.getOutput(target.getName()));
        lang.sendMsg(target, lang.getRequestReceived(sender.getName()));
        tpaRequests.computeIfAbsent(senderUUID, k -> Collections.synchronizedList(new ArrayList<>())).add(this);
        targetRequestMap.computeIfAbsent(targetUUID, k -> Collections.synchronizedList(new ArrayList<>())).add(this);
        buildRunnable();
    }

    public TeleportRequest(UUID sender, UUID target) {
        this.senderUUID = sender;
        this.targetUUID = target;
        this.requestTime = System.currentTimeMillis();
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }
    public void setTarget(Player target) {
        this.target = target;
    }

    public Player getSender() {
        return sender;
    }
    public Player getTarget() {
        return target;
    }
    public UUID getSenderUUID() {
        return senderUUID;
    }
    public UUID getTargetUUID() {
        return targetUUID;
    }
    public long getRequestTime() {
        return requestTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - requestTime > (tpaExpireTime()*1000L);
    }

    public void acceptRequest() {
        AcceptTeleportRequestEvent event = new AcceptTeleportRequestEvent(target, sender);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        teleportPlayer(sender, target);
        lang2.sendMsg(target, lang2.getOutput(sender.getName()));
        deleteRequest();
    }

    public void denyRequest() {
        DenyTeleportRequestEvent event = new DenyTeleportRequestEvent(target, sender);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        lang3.sendMsg(target, lang3.getOutput(sender.getName()));
        deleteRequest();
    }

    public void cancelRequest() {
        CancelTeleportRequestEvent event = new CancelTeleportRequestEvent(sender, target);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        deleteRequest();
    }

    public void deleteRequest() {
        runnable.cancel();
        List<TeleportRequest> list = tpaRequests.get(senderUUID);
        if (list != null) {
            list.remove(this);
            if (list.isEmpty()) tpaRequests.remove(senderUUID);
        }
        List<TeleportRequest> targetList = targetRequestMap.get(targetUUID);
        if (targetList != null) {
            targetList.remove(this);
            if (targetList.isEmpty()) targetRequestMap.remove(targetUUID);
        }
    }

    public void buildRunnable() {
        runnable = new FoliaRunnable() {
            @Override
            public void run() {
                if (isExpired()) {
                    deleteRequest();
                }
            }
        };
        SchedulerUtils.runTaskTimerAsynchronously(runnable, 1L, 20L);
    }

}
