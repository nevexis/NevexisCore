package dev.nevah5.nevexis.webhook;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DiscordWebhookUtil {

    public static DiscordWebhook pluginState(final boolean isEnabled) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Plugin State")
                .setEmbedDescription(isEnabled ? "Plugin is now enabled." : "Plugin is now disabled.")
                .setEmbedTimestamp()
                .setEmbedColor(isEnabled ? 6750105 : 11739419)
                .build();
    }

    public static DiscordWebhook joinActivity(final PlayerJoinEvent joinEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Join Activity")
                .setEmbedAuthor(joinEvent.getPlayer().getUniqueId().toString(), null, null)
                .setEmbedDescription(joinEvent.getPlayer().getName() + " joined.")
                .setEmbedTimestamp()
                .setEmbedColor(6750105)
                .build();
    }

    public static DiscordWebhook quitActivity(final PlayerQuitEvent quitEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Quit Activity")
                .setEmbedAuthor(quitEvent.getPlayer().getUniqueId().toString(), null, null)
                .setEmbedDescription(quitEvent.getPlayer().getName() + " left.")
                .setEmbedTimestamp()
                .setEmbedColor(11739419)
                .build();
    }

    public static DiscordWebhook chatActivity(final AsyncPlayerChatEvent chatEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Chat Activity")
                .setEmbedAuthor(chatEvent.getPlayer().getUniqueId().toString(), null, null)
                .addEmbedField(chatEvent.getPlayer().getName(), chatEvent.getMessage(), false)
                .setEmbedTimestamp()
                .setEmbedColor(16777215)
                .build();
    }

    public static DiscordWebhook commandActivity(final PlayerCommandPreprocessEvent commandEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Command Issued")
                .setEmbedAuthor(commandEvent.getPlayer().getUniqueId().toString(), null, null)
                .addEmbedField(commandEvent.getPlayer().getName(), commandEvent.getMessage(), false)
                .setEmbedTimestamp()
                .setEmbedColor(3134122)
                .build();
    }

    public static DiscordWebhook deathActivity(final PlayerDeathEvent playerDeathEvent) {
        return new DiscordWebhook().builder()
                .setEmbedTitle("Player Death")
                .setEmbedAuthor(playerDeathEvent.getEntity().getUniqueId().toString(), null, null)
                .addEmbedField(playerDeathEvent.getEntity().getName(), playerDeathEvent.getDeathMessage(), false)
                .setEmbedTimestamp()
                .setEmbedColor(3134122)
                .build();
    }

}
