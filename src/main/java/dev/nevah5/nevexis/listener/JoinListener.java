package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.template.activity.JoinWebhook;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.HttpURLConnection;
import java.util.Objects;

public class JoinListener implements Listener {
    private final NevexisCore plugin;
    private final String JOIN_FORMAT;

    public JoinListener(final NevexisCore plugin){
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.join-format"));
        this.JOIN_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onJoinSendWebhook(final PlayerJoinEvent joinEvent) {
        if(this.plugin.getConfig().getBoolean("activity.enabled")) {
            new JoinWebhook(this.plugin.getConfig().getString("activity.discord-webhook-url"), joinEvent);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent joinEvent){
        joinEvent.setJoinMessage(JOIN_FORMAT.replace("%player%", joinEvent.getPlayer().getName()));
    }
}
