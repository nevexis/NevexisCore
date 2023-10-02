package dev.nevah5.nevexis.webhook.template.activity;

import dev.nevah5.nevexis.webhook.DiscordWebhook;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitWebhook {
    public QuitWebhook(final String webhookUrl, final PlayerQuitEvent quitEvent) {
        final DiscordWebhook quitActivityWebhook = new DiscordWebhook().builder()
                .setEmbedTitle("Join Activity")
                .setEmbedDescription(quitEvent.getPlayer().getName() + " joined.")
                .setEmbedColor(6750105)
                .build();

        try {
            quitActivityWebhook.execute(webhookUrl);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
