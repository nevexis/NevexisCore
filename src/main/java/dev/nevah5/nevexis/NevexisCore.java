package dev.nevah5.nevexis;

import dev.nevah5.nevexis.command.ChatClearCommand;
import dev.nevah5.nevexis.command.TeamChatCommand;
import dev.nevah5.nevexis.command.VanishCommand;
import dev.nevah5.nevexis.listener.*;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NevexisCore extends JavaPlugin {

    private String SERVER_PREFIX;
    private String NOT_PLAYER;
    private String NO_PERMISSION;
    private boolean ACTIVITY_ENABLED;
    private String ACTIVITY_WEBHOOK_URL;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Configurations
        this.SERVER_PREFIX = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.getConfig().getString("core.server-prefix")));
        this.NOT_PLAYER = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.getConfig().getString("core.not-player")));
        this.NO_PERMISSION = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.getConfig().getString("core.no-permission")));
        this.ACTIVITY_ENABLED = this.getConfig().getBoolean("activity.enabled");
        if (this.ACTIVITY_ENABLED) {
            this.ACTIVITY_WEBHOOK_URL = Objects.requireNonNull(this.getConfig().getString("activity.discord-webhook-url"));
        }

        // Listeners
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CommandSendListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        // Commands
        Objects.requireNonNull(this.getCommand("vanish")).setExecutor(new VanishCommand(this));
        Objects.requireNonNull(this.getCommand("tc")).setExecutor(new TeamChatCommand(this));
        Objects.requireNonNull(this.getCommand("cc")).setExecutor(new ChatClearCommand(this));

        // Webhook
        if (this.ACTIVITY_ENABLED) {
            final DiscordWebhook pluginState = DiscordWebhookUtil.pluginState(true);
            pluginState.execute(this);
        }
    }

    @Override
    public void onDisable() {
        // Webhook
        if (this.ACTIVITY_ENABLED) {
            final DiscordWebhook pluginState = DiscordWebhookUtil.pluginState(false);
            pluginState.executeSync(this);
        }
    }

    public String getSERVER_PREFIX() {
        return SERVER_PREFIX;
    }

    public boolean isACTIVITY_ENABLED() {
        return ACTIVITY_ENABLED;
    }

    public String getACTIVITY_WEBHOOK_URL() {
        return ACTIVITY_WEBHOOK_URL;
    }

    public String getNoPermissionMessage() {
        return SERVER_PREFIX + NO_PERMISSION;
    }

    public String getOnlyExecutableByPlayerMessage() {
        return SERVER_PREFIX + NOT_PLAYER;
    }
}
