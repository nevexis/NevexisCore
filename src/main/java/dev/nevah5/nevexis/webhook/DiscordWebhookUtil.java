package dev.nevah5.nevexis.webhook;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DiscordWebhookUtil {

    public static DiscordWebhook pluginState(final boolean isEnabled) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Status Change", "", "")
                .setEmbedTitle("Plugin " + (isEnabled ? "enabled" : "disabled"))
                .setEmbedDescription(isEnabled ? "the plugin has been enabled." : "The plugin has been disabled.")
                .setEmbedTimestamp()
                .setEmbedColor(isEnabled ? 6750105 : 11739419)
                .build();
    }

    public static DiscordWebhook joinActivity(final PlayerJoinEvent joinEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Join", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(joinEvent.getPlayer()))
                .setEmbedDescription(joinEvent.getPlayer().getName() + " joined.")
                .setEmbedTimestamp()
                .setEmbedColor(6750105)
                .build();
    }

    public static DiscordWebhook quitActivity(final PlayerQuitEvent quitEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Quit", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(quitEvent.getPlayer()))
                .setEmbedDescription(quitEvent.getPlayer().getName() + " left.")
                .setEmbedTimestamp()
                .setEmbedColor(11739419)
                .build();
    }

    public static DiscordWebhook chatActivity(final AsyncPlayerChatEvent chatEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Chat", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(chatEvent.getPlayer()))
                .setEmbedDescription(chatEvent.getMessage())
                .setEmbedTimestamp()
                .setEmbedColor(16777215)
                .build();
    }

    public static DiscordWebhook teamChatActivity(final Player player, final String message) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Team Chat", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(player))
                .setEmbedDescription(message)
                .setEmbedTimestamp()
                .setEmbedColor(16777215)
                .build();
    }

    public static DiscordWebhook commandActivity(final PlayerCommandPreprocessEvent commandEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Command", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(commandEvent.getPlayer()))
                .setEmbedDescription(commandEvent.getMessage())
                .setEmbedTimestamp()
                .setEmbedColor(3134122)
                .build();
    }

    public static DiscordWebhook deathActivity(final PlayerDeathEvent playerDeathEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Death", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(playerDeathEvent.getEntity()))
                .setEmbedDescription(playerDeathEvent.getDeathMessage())
                .setEmbedTimestamp()
                .setEmbedColor(3134122)
                .build();
    }

    private static String getFormattedPlayerInfo(final Player player) {
        return player.getName() + " [" + player.getUniqueId() + "]";
    }
}
