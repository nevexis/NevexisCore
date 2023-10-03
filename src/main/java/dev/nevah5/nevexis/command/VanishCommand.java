package dev.nevah5.nevexis.command;

import dev.nevah5.nevexis.NevexisCore;
import dev.nevah5.nevexis.webhook.DiscordWebhook;
import dev.nevah5.nevexis.webhook.DiscordWebhookUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class VanishCommand implements CommandExecutor, Listener {
    private final String OTHERS_NO_PERMISSION;
    private final String PLAYER_NOT_FOUND;
    private final String VANISH_PLAYER;
    private final String SHOW_PLAYER;
    private final String OTHERS_VANISH_PLAYER;
    private final String OTHERS_SHOW_PLAYER;
    private final NevexisCore plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishCommand(final NevexisCore plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.OTHERS_NO_PERMISSION = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.others.no-permission")));
        this.PLAYER_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.player-not-found")));
        this.VANISH_PLAYER = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.vanish-player")));
        this.SHOW_PLAYER = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.show-player")));
        this.OTHERS_VANISH_PLAYER = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.others.vanish-player")));
        this.OTHERS_SHOW_PLAYER = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vanish.others.show-player")));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        final DiscordWebhook webhook = DiscordWebhookUtil.commandExecutionActivity(commandSender, "/" + command.getName() + String.join("", args));

        if (!commandSender.hasPermission("nevexis.vanish.use")) {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION);
            webhook.builder()
                    .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.plugin.NO_PERMISSION, false)
                    .build()
                    .execute(this.plugin);
            return true;
        }
        Player target;
        if (args.length > 0) {
            if (!commandSender.hasPermission("nevexis.vanish.use.others")) {
                commandSender.sendMessage(this.plugin.SERVER_PREFIX + OTHERS_NO_PERMISSION);
                webhook.builder()
                        .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.OTHERS_NO_PERMISSION, false)
                        .build()
                        .execute(this.plugin);
                return true;
            }
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.PLAYER_NOT_FOUND);
                webhook.builder()
                        .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.PLAYER_NOT_FOUND, false)
                        .build()
                        .execute(this.plugin);
                return true;
            }
        } else if (commandSender instanceof Player) {
            target = (Player) commandSender;
        } else {
            commandSender.sendMessage(this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER);

            final DiscordWebhook commandWebhook = DiscordWebhookUtil.commandExecutionActivity(commandSender, command.getName() + " " + String.join(" ", args))
                    .builder()
                    .addEmbedField("Error", this.plugin.SERVER_PREFIX + this.plugin.NOT_PLAYER, false)
                    .build();
            commandWebhook.execute(this.plugin);
            return true;
        }

        if (vanishedPlayers.contains(target.getUniqueId())) {
            showPlayer(target, commandSender);
        } else {
            vanishPlayer(target, commandSender);
        }
        webhook.execute(this.plugin);
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        if (vanishedPlayers.contains(joinedPlayer.getUniqueId())) {
            event.setJoinMessage(null);
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("nevexis.vanish.see")) {
                    player.hidePlayer(this.plugin, joinedPlayer);
                }
            }
        }
        for (final UUID uuid : vanishedPlayers) {
            final Player vanishedPlayer = Bukkit.getPlayer(uuid);
            if (vanishedPlayer != null && !joinedPlayer.hasPermission("nevexis.vanish.see")) {
                joinedPlayer.hidePlayer(this.plugin, vanishedPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        Player quittingPlayer = event.getPlayer();
        if (vanishedPlayers.contains(quittingPlayer.getUniqueId())) {
            event.setQuitMessage(null);
        }
    }

    private void vanishPlayer(final Player player, final CommandSender sender) {
        vanishedPlayers.add(player.getUniqueId());
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("nevexis.vanish.see")) {
                onlinePlayer.hidePlayer(this.plugin, player);
            }
        }
        if(player.equals(sender)) {
            sender.sendMessage(this.plugin.SERVER_PREFIX + VANISH_PLAYER);
        } else {
            sender.sendMessage(this.plugin.SERVER_PREFIX + OTHERS_VANISH_PLAYER.replace("%player%", player.getName()));
        }
    }

    private void showPlayer(final Player player, final CommandSender sender) {
        vanishedPlayers.remove(player.getUniqueId());
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(this.plugin, player);
        }
        if(player.equals(sender)) {
            sender.sendMessage(this.plugin.SERVER_PREFIX + SHOW_PLAYER);
        } else {
            sender.sendMessage(this.plugin.SERVER_PREFIX + OTHERS_SHOW_PLAYER.replace("%player%", player.getName()));
        }
    }
}
