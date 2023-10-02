package dev.nevah5.nevexis;

import dev.nevah5.nevexis.command.VanishCommand;
import dev.nevah5.nevexis.listener.ChatListener;
import dev.nevah5.nevexis.listener.CommandListener;
import dev.nevah5.nevexis.listener.JoinListener;
import dev.nevah5.nevexis.listener.QuitListener;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NevexisCore extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Listeners
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        // Commands
        Objects.requireNonNull(this.getCommand("vanish")).setExecutor(new VanishCommand(this));

        // Webhook
        if (this.getConfig().getBoolean("activity.enabled")) {
            final DiscordWebhook pluginState = DiscordWebhookUtil.pluginState(true);
            pluginState.execute(this.getConfig().getString("activity.discord-webhook-url"));
        }
    }

    @Override
    public void onDisable() {
        // Webhook
        if (this.getConfig().getBoolean("activity.enabled")) {
            final DiscordWebhook pluginState = DiscordWebhookUtil.pluginState(false);
            pluginState.execute(this.getConfig().getString("activity.discord-webhook-url"));
        }
    }

}
