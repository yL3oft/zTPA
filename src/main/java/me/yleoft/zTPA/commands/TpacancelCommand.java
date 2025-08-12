package me.yleoft.zTPA.commands;

import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.utils.LanguageUtils;
import me.yleoft.zTPA.utils.TpaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.yleoft.zTPA.utils.WorldGuardUtils.getFlagStateAtPlayer;
import static me.yleoft.zTPA.zTPA.*;

public class TpacancelCommand extends TpaUtils implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, String[] args) {
        LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
        if (!(s instanceof Player)) {
            cmdm.sendMsg(s, cmdm.getOnlyExecutableByPlayers());
            return false;
        }

        Player p = (Player) s;
        if (!p.hasPermission(CmdTpacancelPermission())) {
            cmdm.sendMsg(p, cmdm.getNoPermission());
            return false;
        }
        LanguageUtils.Tpacancel lang = new LanguageUtils.Tpacancel();

        if(args.length == 0) {
            code(p, null, lang, cmdm);
            return true;
        }
        String player = args[0];
        Player t = Bukkit.getPlayer(player);
        if (t == null) {
            lang.sendMsg(p, cmdm.getCantFindPlayer());
            return false;
        }
        code(p, t, lang, cmdm);
        return true;
    }

    public void code(Player p, Player target, LanguageUtils.Tpacancel lang, LanguageUtils.CommandsMSG cmdm) {
        LanguageUtils.HooksMSG hooks = new LanguageUtils.HooksMSG();
        if (!getFlagStateAtPlayer(p, cancelTPAFlag)) {
            hooks.sendMsg(p, hooks.getWorldGuardCancelTpa());
            return;
        }else if (!getFlagStateAtPlayer(p, useTPAFlag)) {
            hooks.sendMsg(p, hooks.getWorldGuardUseTpa());
            return;
        }

        List<TeleportRequest> requests = getRequests(p.getUniqueId());
        if (requests == null || requests.isEmpty()) {
            lang.sendMsg(p, lang.getNoRequest());
            return;
        }

        if(target == null) {
            if (cfguExtras.canAfford(p, CmdTpacancelPermission(), CmdTpacancelCost())) {
                new ArrayList<>(requests).forEach(TeleportRequest::cancelRequest);
                lang.sendMsg(p, lang.getOutput());
            }
            return;
        }

        for(TeleportRequest request : requests) {
            if (request.getTargetUUID().equals(target.getUniqueId())) {
                if (cfguExtras.canAfford(p, CmdTpacancelPermission(), CmdTpacancelCost())) {
                    request.cancelRequest();
                    lang.sendMsg(p, lang.getOutputTo(target.getName()));
                }
                return;
            }
        }
        lang.sendMsg(p, lang.getNoRequestTo(target.getName()));
    }
}
