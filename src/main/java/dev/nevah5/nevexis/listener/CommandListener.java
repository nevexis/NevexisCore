package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandListener implements Listener {
    private final NevexisCore plugin;

    public CommandListener(final NevexisCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent commandEvent) {
        final List<?> deniedCommands = Objects.requireNonNull(this.plugin.getConfig().getList("core.disallowed-commands"));
        AtomicBoolean cancelCommand = new AtomicBoolean(false);

        if (!commandEvent.getPlayer().hasPermission("nevexis.core.*")) {
            deniedCommands.forEach(o -> {
                String[] arrCommand = commandEvent.getMessage().toLowerCase().split(" ", 2);
                final String firstPart = arrCommand[0].trim().replace("/", "");

                if (firstPart.equalsIgnoreCase(o.toString().toLowerCase())) {
                    cancelCommand.set(true);
                }
            });
        }

        final DiscordWebhook chatWebhook = DiscordWebhookUtil.commandIssuedActivity(commandEvent);
        if (cancelCommand.get()) {
            chatWebhook.builder()
                    .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION, false)
                    .build();
        }
        chatWebhook.execute(this.plugin);

        if(cancelCommand.get()) {
            commandEvent.setCancelled(true);
            commandEvent.getPlayer().sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION);
        }
    }
}
