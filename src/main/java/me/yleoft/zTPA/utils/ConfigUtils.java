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
    protected String tpo = "teleport-options.";
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

    //<editor-fold desc="Permissions">
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
