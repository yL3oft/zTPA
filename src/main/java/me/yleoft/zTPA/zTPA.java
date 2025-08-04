package me.yleoft.zTPA;

import com.sk89q.worldguard.protection.flags.StateFlag;
import me.yleoft.zAPI.Metrics;
import me.yleoft.zAPI.managers.FileManager;
import me.yleoft.zAPI.managers.UpdateManager;
import me.yleoft.zAPI.utils.FileUtils;
import me.yleoft.zAPI.zAPI;
import me.yleoft.zTPA.commands.*;
import me.yleoft.zTPA.constructors.TeleportRequest;
import me.yleoft.zTPA.hooks.PlaceholderAPIHandler;
import me.yleoft.zTPA.listeners.*;
import me.yleoft.zTPA.tabcompleters.*;
import me.yleoft.zTPA.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;
import static me.yleoft.zAPI.managers.PluginYAMLManager.*;
import static me.yleoft.zAPI.managers.PluginYAMLManager.registerEvent;
import static me.yleoft.zAPI.utils.StringUtils.transform;
import static me.yleoft.zTPA.utils.LanguageUtils.loadzAPIMessages;

public final class zTPA extends JavaPlugin {

    public static FileUtils configFileUtils;
    public static StateFlag sendTeleportRequestFlag;
    public static StateFlag acceptTeleportRequestFlag;
    public static StateFlag denyTeleportRequestFlag;
    public static StateFlag cancelTeleportRequestFlag;
    public static StateFlag bypassTpaWarmupFlag;
    public static StateFlag bypassTpaCostFlag;

    public static boolean usePlaceholderAPI = false;
    public static boolean useWorldGuard = false;
    public static boolean useVault = false;

    public static final Map<UUID, List<TeleportRequest>> tpaRequests = new ConcurrentHashMap<>();
    public static final Map<UUID, List<TeleportRequest>> targetRequestMap = new ConcurrentHashMap<>();

    private static zTPA main;
    public static ConfigUtils cfgu;
    public static zTPA getInstance() {
        return main;
    }

    public static Object papi;
    public static Object economy;

    public static UpdateManager checker;
    public static boolean needsUpdate = false;
    public String updateVersion = getDescription().getVersion();

    public String pluginName = getDescription().getName();
    public String coloredPluginName = this.pluginName;
    public String pluginVer = getDescription().getVersion();
    public String site = "https://modrinth.com/plugin/ztpa/version/latest";
    public static int bStatsId = 26707;

    @Override
    public void onLoad() {
        //<editor-fold desc="WorldGuard Hook">
        // TODO Add worldguard flags
        //</editor-fold>
    }

    @Override
    public void onEnable() {
        zAPI.init(this, pluginName, coloredPluginName, false);
        //<editor-fold desc="Variables">
        main = zTPA.this;
        pluginName = getDescription().getName();
        coloredPluginName = this.pluginName;
        pluginVer = getDescription().getVersion();
        cfgu = new ConfigUtils();
        //</editor-fold>
        LanguageUtils.Helper helper = new LanguageUtils.Helper() {};
        coloredPluginName = transform(requireNonNull(getConfig().getString("prefix")));
        zAPI.setColoredPluginName(coloredPluginName);
        helper.sendMsg(getServer().getConsoleSender(), coloredPluginName+"§f------------------------------------------------------");
        helper.sendMsg(getServer().getConsoleSender(), coloredPluginName+"§fPlugin started loading...");
        updatePlugin();
        //<editor-fold desc="Files">
        helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', coloredPluginName + "&fChecking if files exist..."));
        if(configFileUtils == null) {
            getConfig();
        }
        List<FileUtils> fus = new ArrayList<>();
        fus.add(FileManager.createFile("languages/en.yml"));
        fus.add(FileManager.createFile("languages/pt-br.yml"));
        for(FileUtils fu : fus) {
            fu.saveDefaultConfig();
            fu.reloadConfig();
        }
        helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', coloredPluginName + "&fAll files have been created!"));
        //</editor-fold>
        //<editor-fold desc="Metrics">
        if(cfgu.hasMetrics()) {
            Metrics metrics = zAPI.startMetrics(bStatsId);
            metrics.addCustomChart(new Metrics.DrilldownPie("player_count", () -> {
                int players = Bukkit.getOnlinePlayers().size();
                return buildDistribution(players);
            }));
            helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                    coloredPluginName+"&aMetrics enabled."
            ));
        }
        //</editor-fold>
        loadCommands();
        loadzAPIMessages();
        //<editor-fold desc="Hooks">
        helper.sendMsg(getServer().getConsoleSender(), coloredPluginName + "§fTrying to connect to hooks...");
        //<editor-fold desc="PlaceholderAPI">
        try {
            zAPI.setPlaceholderAPIHandler(new PlaceholderAPIHandler());
            if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                usePlaceholderAPI = true;
                zAPI.registerPlaceholderExpansion(getDescription().getAuthors().toString(), pluginVer, true, true);
                papi = zAPI.getPlaceholderExpansion();
                helper.sendMsg(getServer().getConsoleSender(), coloredPluginName + "§aPlaceholderAPI hooked successfully!");
            } else {
                helper.sendMsg(getServer().getConsoleSender(), coloredPluginName + "§cPlaceholderAPI plugin not found! Disabling hook...");
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error hooking into PlaceholderAPI", e);
        }
        //</editor-fold>
        //<editor-fold desc="Vault">
        try {
            if (getServer().getPluginManager().isPluginEnabled("Vault")) {
                useVault = true;
                setupEconomy();
                helper.sendMsg(getServer().getConsoleSender(), coloredPluginName + "§aConnected to Vault successfully!");
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error hooking into VaultAPI", e);
        }
        //</editor-fold>
        //</editor-fold>
        helper.sendMsg(getServer().getConsoleSender(), coloredPluginName+"§fPlugin started (Any errors will be above this message)");
        helper.sendMsg(getServer().getConsoleSender(), coloredPluginName+"§f------------------------------------------------------");
    }

    @Override
    public void onDisable() {
        if (!zAPI.isFolia()) {
            Bukkit.getScheduler().cancelTasks(this);
        }
        HandlerList.unregisterAll(this);
        if(papi != null) {
            zAPI.unregisterPlaceholderExpansion();
        }
        unregisterPermissions();
        main = null;
    }

    public void updatePlugin() {
        LanguageUtils.Helper helper = new LanguageUtils.Helper() {};
        checker = new UpdateManager(this, "https://api.github.com/repos/yL3oft/zTPA/tags", "ztpa");
        String version = checker.getVersion();
        String pf = "&8&l|> &r";

        if(!getDescription().getVersion().equals(version)) {
            needsUpdate = true;
            updateVersion = version;
            helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                    coloredPluginName+"&cPlugin is out-dated!"
            ));
            helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                    pf+"&fNew version: &e"+version
            ));
            helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                    pf+"&fYour version: &e"+getDescription().getVersion()
            ));
            if(cfgu.isAutoUpdate()) {
                helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                        pf+"&fAttempting to auto-update it..."
                ));
                try {
                    String path = checker.update();
                    helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                            pf+"&aPlugin updated! &7Saved in: "+path
                    ));
                    helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                            pf+"&aRestart the server to apply changes."
                    ));
                }catch (Exception e) {
                    helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                            pf+"&cCould not auto-update it."
                    ));
                    helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                            pf+"&fYou can update your plugin here: &e"+site
                    ));
                }
            }else {
                helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                        pf+"&fYou can update your plugin here: &e"+site
                ));
            }
        }else {
            helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&',
                    coloredPluginName+"&aPlugin is up-to-date!"
            ));
        }
    }
    public void loadCommands() {
        LanguageUtils.CommandsMSG helper = new LanguageUtils.CommandsMSG();
        helper.sendMsg(getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', coloredPluginName + "§fTrying to load commands, permissions & events..."));
        //<editor-fold desc="Commands">
        try {
            unregisterCommands();
            registerCommand(cfgu.CmdMainCommand(), new MainCommand(), cfgu.CmdMainCooldown(), new MainCompleter(), cfgu.CmdMainDescription(), cfgu.CmdMainAliases().toArray(new String[0]));
            registerCommand(cfgu.CmdTpaCommand(), new TpaCommand(), cfgu.CmdTpaCooldown(), new TpaCompleter(), cfgu.CmdTpaDescription(), cfgu.CmdTpaAliases().toArray(new String[0]));
            registerCommand(cfgu.CmdTpacceptCommand(), new TpacceptCommand(), cfgu.CmdTpacceptCooldown(), new TpaCompleter(), cfgu.CmdTpacceptDescription(), cfgu.CmdTpacceptAliases().toArray(new String[0]));
            registerCommand(cfgu.CmdTpdenyCommand(), new TpdenyCommand(), cfgu.CmdTpdenyCooldown(), new TpaCompleter(), cfgu.CmdTpdenyDescription(), cfgu.CmdTpdenyAliases().toArray(new String[0]));
            registerCommand(cfgu.CmdTpacancelCommand(), new TpacancelCommand(), cfgu.CmdTpacancelCooldown(), new TpaCompleter(), cfgu.CmdTpacancelDescription(), cfgu.CmdTpacancelAliases().toArray(new String[0]));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load commands", e);
        }
        //</editor-fold>
        //<editor-fold desc="Permissions">
        try {
            unregisterPermissions();
            Map<String, Boolean> helpANDmainChildren = new HashMap<>();
            helpANDmainChildren.put(cfgu.CmdMainPermission(), true);
            helpANDmainChildren.put(cfgu.CmdMainHelpPermission(), true);
            registerPermission(cfgu.CmdMainPermission(), "Permission to use the '/" + cfgu.CmdMainCommand() + "' command", PermissionDefault.TRUE);
            registerPermission(cfgu.CmdMainHelpPermission(), "Permission to use the '/" + cfgu.CmdMainCommand() + " (help|?)' command (With perm)", PermissionDefault.OP);
            registerPermission(cfgu.CmdMainVersionPermission(), "Permission to use the '/" + cfgu.CmdMainCommand() + " (version|ver)' command", PermissionDefault.TRUE);
            registerPermission(cfgu.CmdMainVersionUpdatePermission(), "Permission to use the '/" + cfgu.CmdMainCommand() + " (version|ver) update' command", PermissionDefault.OP);
            registerPermission(cfgu.CmdMainReloadPermission(), "Permission to use the '/" + cfgu.CmdMainCommand() + " (reload|rl)' command", PermissionDefault.OP, helpANDmainChildren);
            registerPermission(cfgu.CmdTpaPermission(), "Permission to use the '/" + cfgu.CmdTpaCommand() + "' command", PermissionDefault.TRUE);
            registerPermission(cfgu.CmdTpacceptPermission(), "Permission to use the '/" + cfgu.CmdTpacceptCommand() + "' command", PermissionDefault.TRUE);
            registerPermission(cfgu.CmdTpdenyPermission(), "Permission to use the '/" + cfgu.CmdTpdenyCommand() + "' command", PermissionDefault.TRUE);
            registerPermission(cfgu.CmdTpacancelPermission(), "Permission to use the '/" + cfgu.CmdTpacancelCommand() + "' command", PermissionDefault.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            helper.sendMsg(getServer().getConsoleSender(), this.coloredPluginName + "§cError registering permissions (This doesn't affect anything in general)!");
        }
        //</editor-fold>
        //<editor-fold desc="Listeners">
        registerEvent(new PlayerListeners());
        //</editor-fold>
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    //<editor-fold desc="bStats">
    private Map<String, Map<String, Integer>> buildDistribution(int players) {
        Map<String, Map<String, Integer>> outer = new HashMap<>();
        String outerBucket = outerBucket(players);
        String innerBucket = innerBucket(players);
        Map<String, Integer> inner = new HashMap<>();
        inner.put(innerBucket, 1);
        outer.put(outerBucket, inner);
        return outer;
    }

    private String outerBucket(int n) {
        if (n <= 0) return "0";
        if (n <= 10) return "1-10";
        if (n <= 25) return "11-25";
        if (n <= 50) return "26-50";
        if (n <= 100) return "51-100";
        if (n <= 200) return "101-200";
        return "200+";
    }

    private String innerBucket(int n) {
        if (n <= 0) return "0";
        if (n <= 10) {
            return String.valueOf(n);
        }
        if (n <= 25) {
            return rangeOfFive(n, 11, 25);
        }
        if (n <= 50) {
            return rangeOfFive(n, 26, 50);
        }
        if (n <= 100) {
            return rangeOfFive(n, 51, 100);
        }
        if (n <= 200) {
            return rangeOfFive(n, 101, 200);
        }
        return "200+";
    }

    private String rangeOfFive(int n, int start, int end) {
        int bucketStart = ((n - start) / 5) * 5 + start;
        int bucketEnd = Math.min(bucketStart + 4, end);
        return bucketStart + "-" + bucketEnd;
    }
    //</editor-fold>

    //<editor-fold desc="Java Overrides">
    @Override
    public @NotNull FileConfiguration getConfig() {
        if (configFileUtils == null) {
            // Lazy initialization if not already set
            File configFile = new File(getDataFolder(), "config.yml");
            configFileUtils = new FileUtils(configFile, "config.yml");
            configFileUtils.saveDefaultConfig();
            configFileUtils.reloadConfig(false);
        }
        return configFileUtils.getConfig();
    }

    @Override
    public void saveDefaultConfig() {
        if (configFileUtils == null) {
            File configFile = new File(getDataFolder(), "config.yml");
            configFileUtils = new FileUtils(configFile, "config.yml");
        }
        configFileUtils.saveDefaultConfig();
    }

    @Override
    public void reloadConfig() {
        if (configFileUtils == null) {
            File configFile = new File(getDataFolder(), "config.yml");
            configFileUtils = new FileUtils(configFile, "config.yml");
            configFileUtils.saveDefaultConfig();
        }
        configFileUtils.reloadConfig(false);
    }
    //</editor-fold>

}
