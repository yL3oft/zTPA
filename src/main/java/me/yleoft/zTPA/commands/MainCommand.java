package me.yleoft.zTPA.commands;

import com.zTPA.api.event.player.ExecuteMainCommandEvent;
import me.yleoft.zAPI.managers.FileManager;
import me.yleoft.zAPI.managers.PluginYAMLManager;
import me.yleoft.zAPI.utils.FileUtils;
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

import static me.yleoft.zTPA.zTPA.checker;
import static me.yleoft.zTPA.zTPA.needsUpdate;
import static me.yleoft.zTPA.utils.LanguageUtils.loadzAPIMessages;

public class MainCommand extends ConfigUtils implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, String[] args) {
        String subcmd2;
        Player p = null;
        LanguageUtils.CommandsMSG cmdm = new LanguageUtils.CommandsMSG();
        if (s instanceof Player) {
            p = (Player) s;

            if (!p.hasPermission(CmdMainPermission())) {
                cmdm.sendMsg(p, cmdm.getNoPermission());
                return false;
            }

            ExecuteMainCommandEvent event = new ExecuteMainCommandEvent(p);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return false;
        }

        LanguageUtils.MainCMD lang = new LanguageUtils.MainCMD();
        LanguageUtils.MainCMD.MainReload lang2 = new LanguageUtils.MainCMD.MainReload();
        LanguageUtils.MainCMD.MainVersion lang3 = new LanguageUtils.MainCMD.MainVersion();
        LanguageUtils.MainCMD.MainVersion.MainVersionUpdate langvu = new LanguageUtils.MainCMD.MainVersion.MainVersionUpdate();
        LanguageUtils.MainCMD.MainHelp lang4 = new LanguageUtils.MainCMD.MainHelp();

        //<editor-fold desc="Checks">
        if (args.length == 0) {
            lang.sendMsg(s, getUsage(s, lang4));
            return false;
        }
        //</editor-fold>

        String subcmd = args[0];
        switch (subcmd) {
            case "help":
            case "?":
                lang.sendMsg(s, getUsage(s, lang4));
                return false;
            case "reload":
            case "rl":
                //<editor-fold desc="Checks">
                if (p != null && !p.hasPermission(CmdMainReloadPermission())) {
                    lang.sendMsg(s, cmdm.getNoPermission());
                    return false;
                }
                //</editor-fold>
                if (args.length == 1) {
                    lang.sendMsg(s, lang2.getOutput(reload("all")));
                    return true;
                }
                subcmd2 = args[1];
                switch (subcmd2) {
                    case "all":
                        lang.sendMsg(s, lang2.getOutput(reload("all")));
                        return false;
                    case "commands":
                        lang.sendMsg(s, lang2.getOutputCommands(reload("commands")));
                        return false;
                    case "config":
                        lang.sendMsg(s, lang2.getOutputConfig(reload("config")));
                        return false;
                    case "languages":
                        lang.sendMsg(s, lang2.getOutputLanguages(reload("languages")));
                        return false;
                }
                lang.sendMsg(s, lang2.getUsage());
                return false;
            case "version":
            case "ver":
                if (p != null && !p.hasPermission(CmdMainVersionPermission())) {
                    lang.sendMsg(s, cmdm.getNoPermission());
                    return false;
                }
                if(args.length >= 2) {
                    subcmd2 = args[1];
                    switch (subcmd2) {
                        case "--update":
                        case "update": {
                            if (p != null && !p.hasPermission(CmdMainVersionUpdatePermission())) {
                                lang.sendMsg(s, cmdm.getNoPermission());
                                return false;
                            }
                            if(!needsUpdate) {
                                lang.sendMsg(s, langvu.getNoUpdate());
                                return false;
                            }
                            try {
                                checker.update();
                                lang.sendMsg(s, langvu.getOutput(zTPA.getInstance().updateVersion));
                            }catch (Exception e) {
                                e.printStackTrace();
                                return false;
                            }
                            return true;
                        }
                    }
                }
                lang.sendMsg(s, lang3.getOutput());
                if(needsUpdate) {
                    cmdm.sendMsg(s, "%prefix%&6You are using an outdated version of zHomes! Please update to the latest version.");
                    cmdm.sendMsg(s, "%prefix%&6New version: &a" + zTPA.getInstance().updateVersion);
                    cmdm.sendMsg(s, "%prefix%&6Your version: &c" + zTPA.getInstance().getDescription().getVersion());
                    cmdm.sendMsg(s, "%prefix%&6You can update your plugin here: &e" + zTPA.getInstance().site);
                }
                return false;
        }
        lang.sendMsg(s, getUsage(s, lang4));
        return false;
    }

    public String getUsage(CommandSender s, LanguageUtils.MainCMD.MainHelp lang) {
        if (s instanceof Player) {
            Player p = (Player)s;
            if (p.hasPermission(CmdMainHelpPermission()))
                return lang.getUsageWithPerm();
            return lang.getUsage();
        }
        return lang.getUsageWithPerm();
    }

    public long reload(String which) {
        long now = System.currentTimeMillis();
        switch (which) {
            case "all":
                reload("commands");
                reload("languages");
                break;
            case "commands":
                reload("config");
                zTPA.getInstance().loadCommands();
                PluginYAMLManager.syncCommands();
                break;
            case "config":
                zTPA.getInstance().reloadConfig();
                zTPA.cfgu = new ConfigUtils();
                break;
            case "languages":
                for(FileUtils fu : FileManager.getFiles()) {
                    fu.reloadConfig(false);
                }
                loadzAPIMessages();
                TeleportRequest.setTpaLang(new LanguageUtils.Tpa());
                TeleportRequest.setTpacceptLang(new LanguageUtils.Tpaccept());
                TeleportRequest.setTpdenyLang(new LanguageUtils.Tpdeny());
                TeleportRequest.setTpacancelLang(new LanguageUtils.Tpacancel());
                break;
        }
        return System.currentTimeMillis() - now;
    }
}
