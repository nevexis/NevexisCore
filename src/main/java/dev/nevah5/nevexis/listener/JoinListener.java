package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class JoinListener implements Listener {
    private final NevexisCore plugin;
    private final String JOIN_FORMAT;

    public JoinListener(final NevexisCore plugin) {
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.join-format"));
        this.JOIN_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent joinEvent) {
        joinEvent.setJoinMessage(JOIN_FORMAT.replace("%player%", joinEvent.getPlayer().getName()));

        final DiscordWebhook joinWebhook = DiscordWebhookUtil.joinActivity(joinEvent);
        joinWebhook.execute(this.plugin);
    }
}
