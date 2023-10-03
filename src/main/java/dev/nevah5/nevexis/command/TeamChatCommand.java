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
        final String message = String.join(" ", args);
        final DiscordWebhook webhook = DiscordWebhookUtil.commandExecutionActivity(commandSender, "/" + command.getName() + " " + message);

        if (!commandSender.hasPermission("nevexis.staff")) {
            commandSender.sendMessage(this.plugin.getNoPermissionMessage());

            webhook.builder()
                    .addEmbedField("Error", this.plugin.getNoPermissionMessage(), false)
                    .build()
                    .execute(this.plugin);

            return true;
        }
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if (args.length > 0 && !args[0].trim().equals("")) {
                final String teamMessage = TEAM_CHAT_FORMAT.replace("%player%", player.getName())
                        .replace("%message%", ChatColor.translateAlternateColorCodes('&', message));

                this.plugin.getServer().getOnlinePlayers().forEach(p -> {
                    if (p.hasPermission("nevexis.staff")) {
                        p.sendMessage(teamMessage);
                    }
                });

                webhook.execute(this.plugin);
            } else {
                String errorMessage = String.format("&cInvalid usage: %s", command.getUsage().replace("<command>", command.getName()));
                errorMessage = this.plugin.getSERVER_PREFIX() + ChatColor.translateAlternateColorCodes('&', errorMessage);
                commandSender.sendMessage(errorMessage);

                webhook.builder()
                        .addEmbedField("Error", errorMessage, false)
                        .build()
                        .execute(this.plugin);
            }
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