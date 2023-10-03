package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class DeathListener implements Listener {
    private final NevexisCore plugin;
    private final String DEATH_FORMAT;

    public DeathListener(final NevexisCore plugin) {
        this.plugin = plugin;
        String tmpChatFormat = Objects.requireNonNull(plugin.getConfig().getString("core.death-format"));
        this.DEATH_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent deathEvent) {
        deathEvent.setDeathMessage(DEATH_FORMAT.replace("%message%", Objects.requireNonNull(deathEvent.getDeathMessage())));

        if (this.plugin.ACTIVITY_ENABLED) {
            final DiscordWebhook deathWebhook = DiscordWebhookUtil.deathActivity(deathEvent);
            deathWebhook.execute(this.plugin.getConfig().getString("activity.discord-webhook-url"));
        }
    }
}
