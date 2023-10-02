package dev.nevah5.nevexis.listener;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Objects;

public class CommandListener implements Listener {
    private final NevexisCore plugin;

    public CommandListener(final NevexisCore plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(final PlayerCommandPreprocessEvent commandEvent){
        if(this.plugin.getConfig().getBoolean("activity.enabled")) {
            final DiscordWebhook chatWebhook = DiscordWebhookUtil.commandActivity(commandEvent);
            chatWebhook.execute(this.plugin.getConfig().getString("activity.discord-webhook-url"));
        }
    }
}
