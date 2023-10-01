package dev.nevah5.nevexis.webhook.template.activity;

import dev.nevah5.nevexis.webhook.DiscordWebhook;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinWebhook {
    public JoinWebhook(final String webhookUrl, final PlayerJoinEvent joinEvent) {
        try {
            new DiscordWebhook(webhookUrl)
                    .setEmbedTitle("Join Activity")
                    .setEmbedDescription(joinEvent.getPlayer().getName() + " joined.")
                    .setEmbedColor(6750105)
                    .execute();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
