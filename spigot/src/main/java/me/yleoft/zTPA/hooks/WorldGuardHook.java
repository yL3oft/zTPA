package me.yleoft.zTPA.hooks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.yleoft.zTPA.zTPABukkit;

public class WorldGuardHook {

    public static void setupFlags() {
        zTPABukkit.sendTPAFlag = registerFlag("send-tpa", true);
        zTPABukkit.acceptTPAFlag = registerFlag("accept-tpa", true);
        zTPABukkit.denyTPAFlag = registerFlag("deny-tpa", true);
        zTPABukkit.cancelTPAFlag = registerFlag("cancel-tpa", true);
        zTPABukkit.useTPAFlag = registerFlag("use-tpa", true);
    }

    private static StateFlag registerFlag(String name, boolean defaultValue) {
        FlagRegistry registry = getFlagRegistry();
        try {
            StateFlag flag = new StateFlag(name, defaultValue);
            registry.register(flag);
            return flag;
        } catch (FlagConflictException | IllegalStateException e) {
            Flag<?> existingFlag = registry.get(name);
            if (existingFlag instanceof StateFlag) {
                return (StateFlag) existingFlag;
            }
        }
        throw new FlagConflictException("Unable to register flag: " + name);
    }

    private static FlagRegistry getFlagRegistry() {
        return WorldGuard.getInstance().getFlagRegistry();
    }

}
