package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class QuitListener implements Listener {
    private final NevexisCore plugin;
    private final String QUIT_FORMAT;

    public QuitListener(final NevexisCore plugin){
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.quit-format"));
        tmpChatFormat = tmpChatFormat.replace("%player%", "%s");
        this.QUIT_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onChat(final PlayerQuitEvent quitEvent){
        quitEvent.setQuitMessage(QUIT_FORMAT);
    }
}
