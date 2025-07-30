package me.yleoft.zTPA.hooks;

import me.yleoft.zTPA.zTPA;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPIHandler extends me.yleoft.zAPI.handlers.PlaceholderAPIHandler {

    @Override
    public String applyHookPlaceholders(OfflinePlayer p, String params) {
        String[] split = params.split("_");

        switch (split[0]) {
            case "version":
                return (zTPA.getInstance()).pluginVer;
        }
        return "";
    }

}
