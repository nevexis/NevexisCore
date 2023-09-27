package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class JoinListener implements Listener {
    private final NevexisCore plugin;
    private final String JOIN_FORMAT;

    public JoinListener(final NevexisCore plugin){
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.join-format"));
        tmpChatFormat = tmpChatFormat.replace("%player%", "%s");
        this.JOIN_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onChat(final PlayerJoinEvent joinEvent){
        joinEvent.setJoinMessage(JOIN_FORMAT);
    }
}
