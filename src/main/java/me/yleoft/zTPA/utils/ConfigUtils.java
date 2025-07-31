package me.yleoft.zTPA.utils;

import me.yleoft.zAPI.utils.StringUtils;
import me.yleoft.zTPA.zTPA;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ConfigUtils {
    private static final zTPA main = zTPA.getInstance();

    protected String cmdPath = "commands.";
    protected String permissionsPath = "permissions.";
    protected String permissionsBypassPath = permissionsPath+"bypass.";
    protected String databasePath = "database.";
    protected String lim = "limits.";
    protected String tpo = "tpa-options.";
    protected String warmup = tpo+"warmup.";

    public ConfigUtilsExtras cfguExtras = new ConfigUtilsExtras();

    public static String langType() {
        return main.getConfig().getString("general.language");
    }
    public Boolean isAutoUpdate() {
        return main.getConfig().getBoolean("general.auto-update");
    }
    public Boolean doAnnounceUpdate() {
        return main.getConfig().getBoolean("general.announce-update");
    }
    public Boolean hasMetrics() {
        return main.getConfig().getBoolean("general.metrics");
    }
    public String prefix() {
        return StringUtils.transform(requireNonNull(main.getConfig().getString("prefix")));
    }

    //<editor-fold desc="Plugin Information">
    public int tpaExpireTime() {
        return main.getConfig().getInt(this.tpo + "expire-time") > 0 ? main.getConfig().getInt(this.tpo + "expire-time") : 120;
    }
    public boolean playSound() {
        return main.getConfig().getBoolean(this.tpo + "play-sound");
    }
    public boolean doWarmup() {
        return main.getConfig().getBoolean(this.warmup + "enable");
    }
    public int warmupTime() {
        return main.getConfig().getInt(this.warmup + "time");
    }
    public boolean warmupCancelOnMove() {
        return main.getConfig().getBoolean(this.warmup + "cancel-on-move");
    }
    public boolean warmupShowOnActionbar() {
        return main.getConfig().getBoolean(this.warmup + "show-on-actionbar");
    }
    //</editor-fold>

    //<editor-fold desc="Main Command">
    public String CmdMainCommand() {
        return main.getConfig().getString(this.cmdPath + "main.command");
    }
    public String CmdMainPermission() {
        return main.getConfig().getString(this.cmdPath + "main.permission");
    }
    public String CmdMainDescription() {
        return main.getConfig().getString(this.cmdPath + "main.description");
    }
    public Double CmdMainCooldown() {
        return main.getConfig().getDouble(this.cmdPath + "main.cooldown");
    }
    public List<String> CmdMainAliases() {
        return main.getConfig().getStringList(this.cmdPath + "main.aliases");
    }
    public String CmdMainHelpPermission() {
        return main.getConfig().getString(this.cmdPath + "main.help.permission");
    }
    public String CmdMainVersionPermission() {
        return main.getConfig().getString(this.cmdPath + "main.version.permission");
    }
    public String CmdMainVersionUpdatePermission() {
        return main.getConfig().getString(this.cmdPath + "main.version.update.permission");
    }
    public String CmdMainReloadPermission() {
        return main.getConfig().getString(this.cmdPath + "main.reload.permission");
    }
    //</editor-fold>
    //<editor-fold desc="Tpa Command">
    public String CmdTpaCommand() {
        return main.getConfig().getString(this.cmdPath + "tpa.command");
    }
    public String CmdTpaPermission() {
        return main.getConfig().getString(this.cmdPath + "tpa.permission");
    }
    public String CmdTpaDescription() {
        return main.getConfig().getString(this.cmdPath + "tpa.description");
    }
    public Double CmdTpaCooldown() {
        return main.getConfig().getDouble(this.cmdPath + "tpa.cooldown");
    }
    public List<String> CmdTpaAliases() {
        return main.getConfig().getStringList(this.cmdPath + "tpa.aliases");
    }
    public Float CmdTpaCost() {
        return (float) main.getConfig().getDouble(this.cmdPath + "tpa.command-cost");
    }
    //</editor-fold>
    //<editor-fold desc="Tpaccept Command">
    public String CmdTpacceptCommand() {
        return main.getConfig().getString(this.cmdPath + "tpaccept.command");
    }
    public String CmdTpacceptPermission() {
        return main.getConfig().getString(this.cmdPath + "tpaccept.permission");
    }
    public String CmdTpacceptDescription() {
        return main.getConfig().getString(this.cmdPath + "tpaccept.description");
    }
    public Double CmdTpacceptCooldown() {
        return main.getConfig().getDouble(this.cmdPath + "tpaccept.cooldown");
    }
    public List<String> CmdTpacceptAliases() {
        return main.getConfig().getStringList(this.cmdPath + "tpaccept.aliases");
    }
    public Float CmdTpacceptCost() {
        return (float) main.getConfig().getDouble(this.cmdPath + "tpaccept.command-cost");
    }
    //</editor-fold>
    //<editor-fold desc="Tpdeny Command">
    public String CmdTpdenyCommand() {
        return main.getConfig().getString(this.cmdPath + "tpdeny.command");
    }
    public String CmdTpdenyPermission() {
        return main.getConfig().getString(this.cmdPath + "tpdeny.permission");
    }
    public String CmdTpdenyDescription() {
        return main.getConfig().getString(this.cmdPath + "tpdeny.description");
    }
    public Double CmdTpdenyCooldown() {
        return main.getConfig().getDouble(this.cmdPath + "tpdeny.cooldown");
    }
    public List<String> CmdTpdenyAliases() {
        return main.getConfig().getStringList(this.cmdPath + "tpdeny.aliases");
    }
    public Float CmdTpdenyCost() {
        return (float) main.getConfig().getDouble(this.cmdPath + "tpdeny.command-cost");
    }
    //</editor-fold>
    //<editor-fold desc="Tpacancel Command">
    public String CmdTpacancelCommand() {
        return main.getConfig().getString(this.cmdPath + "tpacancel.command");
    }
    public String CmdTpacancelPermission() {
        return main.getConfig().getString(this.cmdPath + "tpacancel.permission");
    }
    public String CmdTpacancelDescription() {
        return main.getConfig().getString(this.cmdPath + "tpacancel.description");
    }
    public Double CmdTpacancelCooldown() {
        return main.getConfig().getDouble(this.cmdPath + "tpacancel.cooldown");
    }
    public List<String> CmdTpacancelAliases() {
        return main.getConfig().getStringList(this.cmdPath + "tpacancel.aliases");
    }
    public Float CmdTpacancelCost() {
        return (float) main.getConfig().getDouble(this.cmdPath + "tpacancel.command-cost");
    }
    //</editor-fold>

    //<editor-fold desc="Permissions">
    public String PermissionBypassWarmup() {
        return main.getConfig().getString(permissionsBypassPath+"warmup");
    }
    public String PermissionBypassCommandCost(String commandPermission) {
        return main.getConfig().getString(permissionsBypassPath+"command-cost")
                .replace("%command_permission%", commandPermission);
    }
    //</editor-fold>

    public static class ConfigUtilsExtras {

        public boolean canAfford(Player p, String commandPermission, Float cost) {
            if(p.hasPermission(zTPA.cfgu.PermissionBypassCommandCost(commandPermission))) {
                return true;
            }
            Economy economy = (Economy) zTPA.economy;
            LanguageUtils.HooksMSG hooks = new LanguageUtils.HooksMSG();
            if (zTPA.getInstance().getServer().getPluginManager().isPluginEnabled("Vault")) {
                if(economy.has(p, cost)) {
                    economy.withdrawPlayer(p, cost);
                    return true;
                }
                hooks.sendMsg(p, hooks.getVaultCantAfford(cost));
                return false;
            }
            return true;
        }

    }

}
