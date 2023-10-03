package dev.nevah5.nevexis.command;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

public class ChatClearCommand implements CommandExecutor {

    private final NevexisCore plugin;
    private final String CHAT_CLEAR_TEXT;

    public ChatClearCommand(final NevexisCore plugin) {
        this.plugin = plugin;
        this.CHAT_CLEAR_TEXT = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("core.chat-clear-text")));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (!commandSender.hasPermission("nevexis.staff")) {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION);
            return true;
        }
        if (commandSender instanceof Player) {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                if (!player.hasPermission("nevexis.staff")) {
                    player.sendMessage(String.join("", Collections.nCopies(100, " \n")));
                    player.sendMessage(this.plugin.SERVER_PREFIX + this.CHAT_CLEAR_TEXT);
                } else {
                    player.sendMessage(this.plugin.SERVER_PREFIX + this.CHAT_CLEAR_TEXT + String.format(" (%s)", commandSender.getName()));
                }
            });
        } else {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER);

            final DiscordWebhook commandWebhook = DiscordWebhookUtil.nonPlayerCommand(commandSender, command.getName())
                    .builder()
                    .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER, false)
                    .build();
            commandWebhook.execute(this.plugin.ACTIVITY_WEBHOOK_URL);
        }
        return true;
    }

}