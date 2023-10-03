package dev.nevah5.nevexis.command;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TeamChatCommand implements CommandExecutor {

    private final NevexisCore plugin;
    private final String TEAM_CHAT_FORMAT;

    public TeamChatCommand(final NevexisCore plugin) {
        this.plugin = plugin;
        final String tmpChatFormat = Objects.requireNonNull(this.plugin.getConfig().getString("core.team-chat-format"));
        this.TEAM_CHAT_FORMAT = ChatColor.translateAlternateColorCodes('&', tmpChatFormat);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (!commandSender.hasPermission("nevexis.staff")) {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION);
            return true;
        }
        final String message = String.join(" ", args);
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final String teamMessage = TEAM_CHAT_FORMAT.replace("%player%", player.getName())
                    .replace("%message%", ChatColor.translateAlternateColorCodes('&', message));

            this.plugin.getServer().getOnlinePlayers().forEach(p -> {
                if (p.hasPermission("nevexis.staff")) {
                    p.sendMessage(teamMessage);
                }
            });

            if (this.plugin.ACTIVITY_ENABLED) {
                final DiscordWebhook teamChatWebhook = DiscordWebhookUtil.teamChatActivity(player, message);
                teamChatWebhook.execute(this.plugin.ACTIVITY_WEBHOOK_URL);
            }
        } else {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER);

            final DiscordWebhook commandWebhook = DiscordWebhookUtil.nonPlayerCommand(commandSender, command.getName() + " " + message)
                    .builder()
                    .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER, false)
                    .build();
            commandWebhook.execute(this.plugin.ACTIVITY_WEBHOOK_URL);
        }
        return true;
    }

}