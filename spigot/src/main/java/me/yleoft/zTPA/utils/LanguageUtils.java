package me.yleoft.zTPA.utils;

import me.yleoft.zAPI.managers.FileManager;
import me.yleoft.zAPI.mutable.Messages;
import me.yleoft.zAPI.utils.FileUtils;
import me.yleoft.zTPA.zTPABukkit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.yleoft.zAPI.utils.ConfigUtils.formPath;
import static me.yleoft.zAPI.utils.StringUtils.transform;

public class LanguageUtils extends ConfigUtils {

    private static final zTPABukkit main = zTPABukkit.getInstance();
    private static FileUtils fuBACKUP = null;

    public static String hooks = "hooks";
    public static String cmds = "commands";
    public static String tpw = "teleport-warmup";
    public static String worldguard = "worldguard";
    public static String vault = "vault";

    public static File f = new File(main.getDataFolder(), "languages/en.yml");

    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);

    public LanguageUtils() {
        cfg = getConfigFile();
    }

    public static YamlConfiguration getConfigFile() {
        List<FileUtils> list = new ArrayList<>();
        list.add(FileManager.getFileUtil("languages/de.yml"));
        list.add(FileManager.getFileUtil("languages/en.yml"));
        list.add(FileManager.getFileUtil("languages/es.yml"));
        list.add(FileManager.getFileUtil("languages/fr.yml"));
        list.add(FileManager.getFileUtil("languages/it.yml"));
        list.add(FileManager.getFileUtil("languages/nl.yml"));
        list.add(FileManager.getFileUtil("languages/pl.yml"));
        list.add(FileManager.getFileUtil("languages/pt-br.yml"));
        list.add(FileManager.getFileUtil("languages/ru.yml"));
        list.add(FileManager.getFileUtil("languages/tr.yml"));
        list.add(FileManager.getFileUtil("languages/zhcn.yml"));
        list.add(fuBACKUP);
        boolean found = false;
        YamlConfiguration returned = cfg;
        String lang = langType();
        for (FileUtils fu : list) {
            if (fu != null) {
                File f = fu.getFile();
                if (f.exists()) {
                    String name = f.getName();
                    String[] nameS = name.split("\\.");
                    String langtype = nameS[0];
                    if (langtype.equals(lang)) {
                        returned = (YamlConfiguration)fu.getConfig();
                        found = true;
                        break;
                    }
                }
            }
        }
        if (!found) {
            String resource = "languages/" + lang + ".yml";
            File f = new File(main.getDataFolder(), resource);
            if (f.exists()) {
                fuBACKUP = new FileUtils(f, resource);
                returned = (YamlConfiguration)fuBACKUP.getConfig();
            }
        }
        return returned;
    }

    public static class Tpa implements Commands {
        public YamlConfiguration cfg;

        public Tpa() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getCmd() {
            return "tpa";
        }

        public String getUsage() {
            String path = formPath(cmds, getCmd(), "usage");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getOutput() {
            return null;
        }

        public String getOutput(String target) {
            String path = formPath(cmds, getCmd(), "output");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }

        public String getYourself() {
            String path = formPath(cmds, getCmd(), "yourself");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getAlreadyRequested(String target) {
            String path = formPath(cmds, getCmd(), "already-requested");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }

        public String getRequestReceived(String sender) {
            String path = formPath(cmds, getCmd(), "request-received");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", sender)
                    .replace("%time%", String.valueOf(zTPABukkit.cfgu.tpaExpireTime()));
        }
    }

    public static class Tpaccept implements Commands {
        public YamlConfiguration cfg;

        public Tpaccept() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getCmd() {
            return "tpaccept";
        }

        public String getUsage() {
            return null;
        }

        public String getOutput() {
            return null;
        }

        public String getOutput(String target) {
            String path = formPath(cmds, getCmd(), "output");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }

        public String getNoRequest() {
            String path = formPath(cmds, getCmd(), "no-request");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getNoRequestFrom(String target) {
            String path = formPath(cmds, getCmd(), "no-request-from");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }
    }

    public static class Tpdeny implements Commands {
        public YamlConfiguration cfg;

        public Tpdeny() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getCmd() {
            return "tpdeny";
        }

        public String getUsage() {
            return null;
        }

        public String getOutput() {
            return null;
        }

        public String getOutput(String target) {
            String path = formPath(cmds, getCmd(), "output");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }

        public String getNoRequest() {
            String path = formPath(cmds, getCmd(), "no-request");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getNoRequestFrom(String target) {
            String path = formPath(cmds, getCmd(), "no-request-from");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }
    }

    public static class Tpacancel implements Commands {
        public YamlConfiguration cfg;

        public Tpacancel() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getCmd() {
            return "tpacancel";
        }

        public String getUsage() {
            return null;
        }

        public String getOutput() {
            String path = formPath(cmds, getCmd(), "output");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getOutputTo(String target) {
            String path = formPath(cmds, getCmd(), "output-to");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }

        public String getNoRequest() {
            String path = formPath(cmds, getCmd(), "no-request");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand());
        }

        public String getNoRequestTo(String target) {
            String path = formPath(cmds, getCmd(), "no-request-to");
            return this.cfg.getString(path)
                    .replace("%command%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%player%", target);
        }
    }

    public static class MainCMD implements Commands {
        public YamlConfiguration cfg;

        public MainCMD() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getCmd() {
            return "main";
        }

        public String getUsage() {
            return null;
        }

        public String getOutput() {
            return null;
        }

        public static class MainHelp implements Commands {
            public YamlConfiguration cfg;

            public MainHelp() {
                this.cfg = LanguageUtils.getConfigFile();
            }

            public String getCmd() {
                return "main.help";
            }

            public String getUsage() {
                String path = formPath(cmds, getCmd(), "help-noperm");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand());
            }

            public String getUsageWithPerm() {
                String path = formPath(cmds, getCmd(), "help-perm");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand());
            }

            public String getOutput() {
                String path = formPath(cmds, getCmd(), "output");
                return this.cfg.getString(path)
                        .replace("%version%", LanguageUtils.main.getDescription().getVersion());
            }
        }

        public static class MainVersion implements Commands {
            public YamlConfiguration cfg;

            public MainVersion() {
                this.cfg = LanguageUtils.getConfigFile();
            }

            public String getCmd() {
                return "main.version";
            }

            public String getUsage() {
                return null;
            }

            public String getOutput() {
                String path = formPath(cmds, getCmd(), "output");
                return this.cfg.getString(path)
                        .replace("%version%", LanguageUtils.main.getDescription().getVersion());
            }

            public static class MainVersionUpdate implements Commands {
                public YamlConfiguration cfg;

                public MainVersionUpdate() {
                    this.cfg = LanguageUtils.getConfigFile();
                }

                public String getCmd() {
                    return "main.version.update";
                }

                public String getUsage() {
                    return null;
                }

                public String getOutput() {
                    return null;
                }

                public String getOutput(String newVersion) {
                    String path = formPath(cmds, getCmd(), "output");
                    return this.cfg.getString(path)
                            .replace("%update%", newVersion);
                }

                public String getNoUpdate() {
                    String path = formPath(cmds, getCmd(), "no-update");
                    return this.cfg.getString(path);
                }
            }
        }

        public static class MainReload implements Commands {
            public YamlConfiguration cfg;

            public MainReload() {
                this.cfg = LanguageUtils.getConfigFile();
            }

            public String getCmd() {
                return "main.reload";
            }

            public String getUsage() {
                String path = formPath(cmds, getCmd(), "usage");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand());
            }

            public String getOutput() {
                return null;
            }

            public String getOutput(long time) {
                String path = formPath(cmds, getCmd(), "output");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand())
                        .replace("%time%", String.valueOf(time));
            }

            public String getOutputCommands(long time) {
                String path = formPath(cmds, getCmd(), "commands.output");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand())
                        .replace("%time%", String.valueOf(time));
            }

            public String getOutputConfig(long time) {
                String path = formPath(cmds, getCmd(), "config.output");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand())
                        .replace("%time%", String.valueOf(time));
            }

            public String getOutputLanguages(long time) {
                String path = formPath(cmds, getCmd(), "languages.output");
                return this.cfg.getString(path)
                        .replace("%command%", zTPABukkit.cfgu.CmdMainCommand())
                        .replace("%time%", String.valueOf(time));
            }
        }
    }

    public static class TeleportWarmupMSG implements Helper {
        public YamlConfiguration cfg;

        public TeleportWarmupMSG() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getWarmup(int time) {
            String path = formPath(tpw, "warmup");
            return this.cfg.getString(path)
                    .replace("%time%", String.valueOf(time));
        }

        public String getWarmupActionbar(int time) {
            String path = formPath(tpw, "warmup-actionbar");
            return this.cfg.getString(path)
                    .replace("%time%", String.valueOf(time));
        }

        public String getCancelled() {
            String path = formPath(tpw, "cancelled");
            return this.cfg.getString(path);
        }

        public String getCancelledActionbar() {
            String path = formPath(tpw, "cancelled-actionbar");
            return this.cfg.getString(path);
        }
    }

    public static class HooksMSG implements Helper {
        public YamlConfiguration cfg;

        public HooksMSG() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getWorldGuardSendTpa() {
            String path = formPath(hooks, worldguard, "send-tpa-flag");
            return this.cfg.getString(path);
        }

        public String getWorldGuardAcceptTpa() {
            String path = formPath(hooks, worldguard, "accept-tpa-flag");
            return this.cfg.getString(path);
        }

        public String getWorldGuardDenyTpa() {
            String path = formPath(hooks, worldguard, "deny-tpa-flag");
            return this.cfg.getString(path);
        }

        public String getWorldGuardCancelTpa() {
            String path = formPath(hooks, worldguard, "cancel-tpa-flag");
            return this.cfg.getString(path);
        }

        public String getWorldGuardUseTpa() {
            String path = formPath(hooks, worldguard, "use-tpa-flag");
            return this.cfg.getString(path);
        }

        public String getVaultCantAfford(Float cost) {
            String path = formPath(hooks, vault, "cant-afford-command");
            return this.cfg.getString(path)
                    .replace("%cost%", Float.toString(cost));
        }
    }

    public static class CommandsMSG implements Helper {
        public YamlConfiguration cfg;

        public CommandsMSG() {
            this.cfg = LanguageUtils.getConfigFile();
        }

        public String getNoPermission() {
            String path = formPath(cmds, "no-permission");
            return this.cfg.getString(path);
        }

        public String getCantFindPlayer() {
            String path = formPath(cmds, "cant-find-player");
            return this.cfg.getString(path);
        }

        public String getOnlyExecutableByPlayers() {
            String path = formPath(cmds, "only-executable-by-players");
            return this.cfg.getString(path);
        }

        public String getMoreThanOneRequest() {
            String path = formPath(cmds, "more-than-one-request");
            return this.cfg.getString(path);
        }
    }

    public static void loadzAPIMessages() {
        YamlConfiguration config = LanguageUtils.getConfigFile();
        Messages.setCooldownExpired(Objects.requireNonNull(Helper.getText(config.getString(formPath(cmds, "in-cooldown")))));
    }

    public interface Commands extends Helper {
        String getCmd();

        String getUsage();

        String getOutput();
    }

    public interface Helper {
        default void sendMsg(Player p, String text) {
            if(!p.isOnline()) return;
            if(text.isEmpty()) return;
            text = getText(p, text);
            p.sendMessage(text);
        }

        default void sendMsg(CommandSender s, String text) {
            if(text.isEmpty()) return;
            text = getText(s, text);
            if (s instanceof Player) {
                Player p = (Player)s;
            }
            s.sendMessage(text);
        }

        default void broadcast(String text) {
            if(text.isEmpty()) return;
            text = getText(null, text);
            Bukkit.getServer().broadcastMessage(text);
        }

        static String getText(CommandSender s, String text) {
            text = transform(text
                    .replace("%prefix%", zTPABukkit.cfgu.prefix())
                    .replace("%command-tpa%", zTPABukkit.cfgu.CmdTpaCommand())
                    .replace("%command-tpaccept%", zTPABukkit.cfgu.CmdTpacceptCommand())
                    .replace("%command-tpdeny%", zTPABukkit.cfgu.CmdTpdenyCommand())
                    .replace("%command-tpacancel%", zTPABukkit.cfgu.CmdTpacancelCommand()));
            if(s instanceof Player) {
                Player p = (Player)s;
                text = transform(p, text);
            }
            return text;
        }
        static String getText(String text) {
            return getText(null, text);
        }

    }

}
