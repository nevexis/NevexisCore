package dev.nevah5.nevexis.webhook;

import org.bukkit.command.CommandSender;
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
                .setEmbedDescription(isEnabled ? "The plugin has been enabled." : "The plugin has been disabled.")
                .setEmbedTimestamp()
                .setEmbedColor(3093151)
                .build();
    }

    public static DiscordWebhook joinActivity(final PlayerJoinEvent joinEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Join", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(joinEvent.getPlayer()))
                .setEmbedDescription(joinEvent.getPlayer().getName() + " joined.")
                .setEmbedTimestamp()
                .setEmbedColor(3128625)
                .build();
    }

    public static DiscordWebhook quitActivity(final PlayerQuitEvent quitEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Quit", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(quitEvent.getPlayer()))
                .setEmbedDescription(quitEvent.getPlayer().getName() + " left.")
                .setEmbedTimestamp()
                .setEmbedColor(12399153)
                .build();
    }

    public static DiscordWebhook chatActivity(final AsyncPlayerChatEvent chatEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Chat", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(chatEvent.getPlayer()))
                .setEmbedDescription(chatEvent.getMessage())
                .setEmbedTimestamp()
                .setEmbedColor(16055039)
                .build();
    }

    public static DiscordWebhook teamChatActivity(final Player player, final String message) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Team Chat", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(player))
                .setEmbedDescription(message)
                .setEmbedTimestamp()
                .setEmbedColor(531253)
                .build();
    }

    public static DiscordWebhook commandIssuedActivity(final PlayerCommandPreprocessEvent commandEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Command Issued", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(commandEvent.getPlayer()))
                .setEmbedDescription(commandEvent.getMessage())
                .setEmbedTimestamp()
                .setEmbedColor(5577629)
                .build();
    }

    public static DiscordWebhook deathActivity(final String deathMessage, final PlayerDeathEvent playerDeathEvent) {
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Death", "", "")
                .setEmbedTitle(getFormattedPlayerInfo(playerDeathEvent.getEntity()))
                .setEmbedDescription(deathMessage)
                .setEmbedTimestamp()
                .setEmbedColor(14270239)
                .build();
    }

    public static DiscordWebhook commandExecutionActivity(final CommandSender sender, final String message) {
        if(sender instanceof Player) {
            return new DiscordWebhook().builder()
                    .setEmbedAuthor("Command Execution", "", "")
                    .setEmbedTitle(getFormattedPlayerInfo((Player) sender))
                    .setEmbedDescription(message)
                    .setEmbedTimestamp()
                    .setEmbedColor(5577629)
                    .build();
        }
        return new DiscordWebhook().builder()
                .setEmbedAuthor("Command Execution", "", "")
                .setEmbedTitle(sender.getName())
                .setEmbedDescription(message)
                .setEmbedTimestamp()
                .setEmbedColor(5577629)
                .build();
    }

    private static String getFormattedPlayerInfo(final Player player) {
        return player.getName() + " [" + player.getUniqueId() + "]";
    }
}
