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
        final DiscordWebhook webhook = DiscordWebhookUtil.commandExecutionActivity(commandSender, "/" + command.getName());

        if (!commandSender.hasPermission("nevexis.staff")) {
            commandSender.sendMessage(this.plugin.getNoPermissionMessage());

            webhook.builder()
                    .addEmbedField("Error", this.plugin.getNoPermissionMessage(), false)
                    .build()
                    .execute(this.plugin);

            return true;
        }
        if (commandSender instanceof Player) {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                if (!player.hasPermission("nevexis.staff")) {
                    player.sendMessage(String.join("", Collections.nCopies(100, " \n")));
                    player.sendMessage(this.plugin.getSERVER_PREFIX() + this.CHAT_CLEAR_TEXT);
                } else {
                    player.sendMessage(this.plugin.getSERVER_PREFIX() + this.CHAT_CLEAR_TEXT + String.format(" (%s)", commandSender.getName()));
                }
            });
            webhook.execute(this.plugin);
        } else {
            commandSender.sendMessage(this.plugin.getOnlyExecutableByPlayerMessage());

            webhook.builder()
                    .addEmbedField("Error", this.plugin.getOnlyExecutableByPlayerMessage(), false)
                    .build()
                    .execute(this.plugin);
        }
        return true;
    }

}