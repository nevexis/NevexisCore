package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.List;

/**
 * IMPORTANT!
 * This event is triggered WHEN THE SERVER SENDS THE AVAILABLE
 * COMMANDS TO THE SERVER, not when a player executes a command.
 */
public class CommandSendListener implements Listener {

    private final NevexisCore plugin;

    public CommandSendListener(final NevexisCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandSend(final PlayerCommandSendEvent event) {
        List<String> deniedCommands = this.plugin.getConfig().getStringList("core.disallowed-commands");

        for (String command : deniedCommands) {
            event.getCommands().remove(command);
        }

        // TODO: filter out additional/duplicate commands
        // e.g. minecraft:help isn't filtered out because bukkit:help is
    }
}
