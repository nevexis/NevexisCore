package dev.nevah5.nevexis.webhook.template.activity;

import dev.nevah5.nevexis.webhook.DiscordWebhook;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitWebhook {
    public QuitWebhook(final String webhookUrl, final PlayerQuitEvent quitEvent) {
        try {
            new DiscordWebhook(webhookUrl)
                    .setEmbedTitle("Quit Activity")
                    .setEmbedDescription(quitEvent.getPlayer().getName() + " left.")
                    .setEmbedColor(13904670)
                    .execute();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
