package dev.nevah5.nevexis;

import dev.nevah5.nevexis.command.VanishCommand;
import dev.nevah5.nevexis.listener.ChatListener;
import dev.nevah5.nevexis.listener.JoinListener;
import dev.nevah5.nevexis.listener.QuitListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NevexisCore extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Listeners
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(this), this);

        // Commands
        Objects.requireNonNull(this.getCommand("vanish")).setExecutor(new VanishCommand(this));
    }

}
