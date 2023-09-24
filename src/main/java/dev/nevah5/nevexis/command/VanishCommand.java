package dev.nevah5.nevexis.command;

import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class VanishCommand implements CommandExecutor, Listener {
    private final NevexisCore plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishCommand(final NevexisCore plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (!commandSender.hasPermission("vanish.use")) {
            commandSender.sendMessage("You don't have permission to use this command.");
            return true;
        }
        Player target;
        if (args.length > 0) {
            if (!commandSender.hasPermission("vanish.use.others")) {
                commandSender.sendMessage("You don't have permission to vanish others.");
                return true;
            }
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                commandSender.sendMessage("Player not found.");
                return true;
            }
        } else if (commandSender instanceof Player) {
            target = (Player) commandSender;
        } else {
            commandSender.sendMessage("Please specify a player.");
            return true;
        }

        if (vanishedPlayers.contains(target.getUniqueId())) {
            showPlayer(target, commandSender);
        } else {
            vanishPlayer(target, commandSender);
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        if (vanishedPlayers.contains(joinedPlayer.getUniqueId())) {
            event.setJoinMessage(null);
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("vanish.see")) {
                    player.hidePlayer(this.plugin, joinedPlayer);
                }
            }
        }
        for (final UUID uuid : vanishedPlayers) {
            final Player vanishedPlayer = Bukkit.getPlayer(uuid);
            if (vanishedPlayer != null && !joinedPlayer.hasPermission("vanish.see")) {
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
            if (!onlinePlayer.hasPermission("vanish.see")) {
                onlinePlayer.hidePlayer(this.plugin, player);
            }
        }
        if(player.equals(sender)) {
            sender.sendMessage("You are now vanished!");
        } else {
            sender.sendMessage(String.format("%s is now vanished!", player.getName()));
        }
    }

    private void showPlayer(final Player player, final CommandSender sender) {
        vanishedPlayers.remove(player.getUniqueId());
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(this.plugin, player);
        }
        if(player.equals(sender)) {
            sender.sendMessage("You are now visible!");
        } else {
            sender.sendMessage(String.format("%s is now visible!", player.getName()));
        }
    }
}
