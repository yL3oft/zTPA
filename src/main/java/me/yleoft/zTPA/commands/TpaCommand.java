package me.yleoft.zTPA.commands;

import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.utils.ConfigUtils;
import me.yleoft.zTPA.utils.LanguageUtils;
import me.yleoft.zTPA.zTPA;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpaCommand extends ConfigUtils implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, String[] args) {
        LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
        if (!(s instanceof Player)) {
            cmdm.sendMsg(s, cmdm.getOnlyExecutableByPlayers());
            return false;
        }

        Player p = (Player) s;
        if (!p.hasPermission(CmdTpaPermission())) {
            cmdm.sendMsg(p, cmdm.getNoPermission());
            return false;
        }
        LanguageUtils.Tpa lang = new LanguageUtils.Tpa();

        //<editor-fold desc="Checks">
        if (args.length == 0) {
            lang.sendMsg(s, lang.getUsage());
            return false;
        }
        //</editor-fold>

        String player = args[0];
        final Player t = Bukkit.getPlayer(player);
        if (t == null) {
            lang.sendMsg(p, cmdm.getCantFindPlayer());
            return false;
        }

        if(p.getUniqueId() == t.getUniqueId()) {
            lang.sendMsg(p, lang.getYourself());
            return false;
        }

        List<TeleportRequest> requests = zTPA.tpaRequests.get(p.getUniqueId());
        if(requests != null && requests.stream().anyMatch(req -> req.getTargetUUID() == t.getUniqueId())) {
            lang.sendMsg(p, lang.getAlreadyRequested(t.getName()));
            return false;
        }

        if (cfguExtras.canAfford(p, CmdTpaPermission(), CmdTpaCost())) {
            new TeleportRequest(p, t);
        }
        return false;
    }
}
