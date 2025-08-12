package me.yleoft.zTPA.hooks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.yleoft.zTPA.zTPA;

public class WorldGuardHook {

    public static void setupFlags() {
        zTPA.sendTPAFlag = registerFlag("send-tpa", true);
        zTPA.acceptTPAFlag = registerFlag("accept-tpa", true);
        zTPA.denyTPAFlag = registerFlag("deny-tpa", true);
        zTPA.cancelTPAFlag = registerFlag("cancel-tpa", true);
        zTPA.useTPAFlag = registerFlag("use-tpa", true);
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
