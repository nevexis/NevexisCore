package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class ChatListener implements Listener {
    private final NevexisCore plugin;
    private final String CHAT_FORMAT;

    public ChatListener(final NevexisCore plugin){
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.chat-format"));
        tmpChatFormat = tmpChatFormat.replace("%player%", "%s");
        tmpChatFormat = tmpChatFormat.replace("%message%", "%s");
        this.CHAT_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent chatEvent){
        chatEvent.setFormat(CHAT_FORMAT);
    }
}
