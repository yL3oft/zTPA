package me.yleoft.zTPA.commands;

import com.zTPA.api.event.player.AcceptTeleportRequestEvent;
import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.utils.LanguageUtils;
import me.yleoft.zTPA.utils.TpaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpacceptCommand extends TpaUtils implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, String[] args) {
        LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
        if (!(s instanceof Player)) {
            cmdm.sendMsg(s, cmdm.getOnlyExecutableByPlayers());
            return false;
        }

        Player p = (Player) s;
        if (!p.hasPermission(CmdTpacceptPermission())) {
            cmdm.sendMsg(p, cmdm.getNoPermission());
            return false;
        }
        LanguageUtils.Tpaccept lang = new LanguageUtils.Tpaccept();

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

    public void code(Player p, Player target, LanguageUtils.Tpaccept lang, LanguageUtils.CommandsMSG cmdm) {
        List<TeleportRequest> requests = getTargetRequests(p.getUniqueId());
        if (requests == null || requests.isEmpty()) {
            lang.sendMsg(p, lang.getNoRequest());
            return;
        }

        if(target == null) {
            if(requests.size() == 1) {
                target = requests.getFirst().getSender();
            }else {
                lang.sendMsg(p, cmdm.getMoreThanOneRequest());
                return;
            }
        }

        for(TeleportRequest request : requests) {
            if (request.getSenderUUID().equals(target.getUniqueId())) {
                if (cfguExtras.canAfford(p, CmdTpacceptPermission(), CmdTpacceptCost())) {
                    request.acceptRequest();
                }
                return;
            }
        }
        lang.sendMsg(p, lang.getNoRequestFrom(target.getName()));
    }
}
