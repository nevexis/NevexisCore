package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class QuitListener implements Listener {
    private final NevexisCore plugin;
    private final String QUIT_FORMAT;

    public QuitListener(final NevexisCore plugin) {
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.quit-format"));
        this.QUIT_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent quitEvent) {
        quitEvent.setQuitMessage(QUIT_FORMAT.replace("%player%", quitEvent.getPlayer().getName()));

        if (this.plugin.ACTIVITY_ENABLED) {
            final DiscordWebhook joinWebhook = DiscordWebhookUtil.quitActivity(quitEvent);
            joinWebhook.execute(this.plugin.ACTIVITY_WEBHOOK_URL);
        }
    }
}
