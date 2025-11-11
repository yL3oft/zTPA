package me.yleoft.zTPA.utils;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.yleoft.zTPA.hooks.WorldGuardHook;
import org.bukkit.entity.Player;

import static me.yleoft.zTPA.zTPABukkit.useWorldGuard;

public abstract class WorldGuardUtils extends WorldGuardHook {

    public static boolean getFlagStateAtPlayer(Player p, StateFlag flag) {
        if(!useWorldGuard) return true;
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld());
        if (canBypass) return true;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(localPlayer.getLocation());
        return set.testState(localPlayer, flag);
    }

}
