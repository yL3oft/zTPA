package me.yleoft.zTPA.tabcompleters;

import me.yleoft.zTPA.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCompleter extends ConfigUtils implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (!(s instanceof Player))
            return completions;
        Player p = (Player)s;
        if (args.length == 1) {
            if (p.hasPermission(CmdMainReloadPermission()))
                commands.add("reload");
            if (p.hasPermission(CmdMainVersionPermission()))
                commands.add("version");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "reload":
                case "rl": {
                    if (p.hasPermission(CmdMainReloadPermission())) {
                        commands.add("all");
                        commands.add("commands");
                        commands.add("config");
                        commands.add("languages");
                    }
                    break;
                }
                case "version":
                case "ver": {
                    if (p.hasPermission(CmdMainVersionUpdatePermission())) {
                        commands.add("update");
                    }
                    break;
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(commands);
        return completions;
    }

}
