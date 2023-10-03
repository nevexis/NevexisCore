package dev.nevah5.nevexis.command;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

public class TeamChatCommand implements CommandExecutor {

    private final NevexisCore plugin;

    public TeamChatCommand(final NevexisCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (!commandSender.hasPermission("nevexis.staff")) {
            commandSender.sendMessage(this.plugin.NO_PERMISSION);
            return true;
        }
        if (this.plugin.getConfig().getBoolean("activity.enabled")) {
            final DiscordWebhook teamChatWebhook = DiscordWebhookUtil.teamChatActivity(Objects.requireNonNull(commandSender.getServer().getPlayer(command.getName())), Arrays.toString(args));
            teamChatWebhook.execute(this.plugin.getConfig().getString("activity.discord-webhook-url"));
        }
        return true;
    }

}