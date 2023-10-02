package dev.nevah5.nevexis.webhook;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DiscordWebhookUtil {

    public static DiscordWebhook joinActivity(final PlayerJoinEvent joinEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Join Activity")
                .setEmbedDescription(joinEvent.getPlayer().getName() + " joined.")
                .setEmbedColor(6750105)
                .build();
    }

    public static DiscordWebhook quitActivity(final PlayerQuitEvent quitEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Quit Activity")
                .setEmbedDescription(quitEvent.getPlayer().getName() + " left.")
                .setEmbedColor(11739419)
                .build();
    }

    public static DiscordWebhook chatActivity(final AsyncPlayerChatEvent chatEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Chat Activity")
                .setEmbedDescription(chatEvent.getPlayer().getName() + " >> " + chatEvent.getMessage())
                .setEmbedColor(16777215)
                .build();
    }

}
