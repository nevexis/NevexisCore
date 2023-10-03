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
    private final String DEATH_MESSAGE_PREFIX;

    public DeathListener(final NevexisCore plugin) {
        this.plugin = plugin;
        String tmpDeathMsgPrefix = Objects.requireNonNull(plugin.getConfig().getString("core.death-message-prefix"));
        this.DEATH_MESSAGE_PREFIX = ChatColor.translateAlternateColorCodes('&', tmpDeathMsgPrefix);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent deathEvent) {
        final String deathMessage = deathEvent.getDeathMessage();
        deathEvent.setDeathMessage(this.DEATH_MESSAGE_PREFIX + deathEvent.getDeathMessage());

        final DiscordWebhook deathWebhook = DiscordWebhookUtil.deathActivity(deathMessage, deathEvent);
        deathWebhook.execute(this.plugin);
    }
}
