package dev.nevah5.nevexis;

import dev.nevah5.nevexis.command.VanishCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NevexisCore extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("vanish")).setExecutor(new VanishCommand(this));
    }

}
