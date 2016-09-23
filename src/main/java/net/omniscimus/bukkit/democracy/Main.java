package net.omniscimus.bukkit.democracy;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.omniscimus.bukkit.democracy.commands.ProposalCommand;
import net.omniscimus.bukkit.democracy.commands.InfoCommand;
import net.omniscimus.bukkit.democracy.commands.OptInCommand;
import net.omniscimus.bukkit.democracy.commands.OptOutCommand;
import net.omniscimus.bukkit.democracy.commands.VoteCommand;
import net.omniscimus.bukkit.democracy.exceptions.NoPermissionException;
import net.omniscimus.bukkit.democracy.exceptions.WrongArgumentsNumberException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSenderTypeException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSyntaxException;

/**
 * Main class for this plugin. The path to this class is in plugin.yml.
 * Interaction point for Bukkit.
 */
public class Main extends JavaPlugin implements Listener {

    /**
     * Used to see when we're entering a new week; necessary for logging
     * playtime per week.
     */
    private int weekNumber;
    private BukkitTask weekChangeChecker;

    /**
     * Used for the configuration file.
     */
    private static String currentWeek;

    private ArrayList<Voter> onlineVoters = new ArrayList<>();

    /**
     * Called by Bukkit when this plugin should enable.
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        DateFormatter date = new DateFormatter(new Date(System.currentTimeMillis()));
        weekNumber = date.getWeekNumber();
        currentWeek = date.getYear() + "_" + weekNumber;

        // Start logging playtime for anyone who's online right now.
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerVoter(player);
        }

        weekChangeChecker = getServer().getScheduler().runTaskTimer(this, () -> {
            checkWeekChange();
        }, 0L, 20 * 60);
    }

    /**
     * Called by Bukkit when this plugin has to disable.
     */
    @Override
    public void onDisable() {
        getServer().getScheduler().runTaskLater(this, () -> {
            weekChangeChecker.cancel();
        }, 20L);
        for (Player player : Bukkit.getOnlinePlayers()) {
            deregisterVoter(player);
        }
    }

    /**
     * Gets the numbers of the current year and week, formatted like %y_%w.
     * 
     * @return a String containing the current year and week
     */
    public static String getCurrentWeek() {
        return currentWeek;
    }

    /**
     * Gets the Voter object for a certain Player.
     * 
     * @param byPlayer
     *            The Player object by which to look up the Voter.
     * @return the Voter object associated with {@code byPlayer}
     */
    private Voter getVoter(Player byPlayer) {
        UUID id = byPlayer.getUniqueId();
        for (Voter voter : onlineVoters) {
            if (voter.getId().equals(id))
                return voter;
        }
        return null;
    }

    /**
     * Associates a Voter object with {@code player} and stores it in the list
     * of online Voters. Will keep track of this player's playtime starting
     * immediately.
     * 
     * @param player
     *            the Player for whom to create a Voter object
     */
    private void registerVoter(Player player) {
        long now = System.currentTimeMillis();
        Voter voter = new Voter(player.getUniqueId());
        voter.setLoginTime(now);
        onlineVoters.add(voter);
    }

    /**
     * Releases the Voter object associated with {@code player}. The playtime
     * counter of this player will be stopped and its value will be saved.
     * 
     * @param player
     *            the Player whose Voter object to release
     */
    private void deregisterVoter(Player player) {
        long now = System.currentTimeMillis();
        Voter voter = getVoter(player);
        voter.setLogoutTime(now);
        voter.updatePlaytime();
        onlineVoters.remove(voter);
    }

    /**
     * Called by Bukkit whenever a Player joins the server.
     * 
     * @param event
     *            the event that occurred
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        registerVoter(event.getPlayer());
    }

    /**
     * Called by Bukkit whenever a Player quits the server.
     * 
     * @param event
     *            the event that occurred
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        deregisterVoter(event.getPlayer());
    }

    /**
     * Called by Bukkit whenever one of this plugin's commands are used. These
     * commands are in plugin.yml.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (args.length == 0) {
                new InfoCommand(sender, args).run();
            } else {
                String[] subArgs = subArray(args, 1, args.length);
                switch (args[0]) {
                case "opt-in":
                    new OptInCommand(sender, subArgs).run();
                    break;
                case "opt-out":
                    new OptOutCommand(sender, subArgs).run();
                    break;
                case "vote":
                    new VoteCommand(sender, subArgs).run();
                    break;
                case "proposal":
                    new ProposalCommand(sender, subArgs).run();
                    break;
                case "check-proposal":
                    break;
                case "toggle-proposal":
                    break;
                default:
                    throw new WrongSyntaxException();
                }
            }
        } catch (WrongSyntaxException e) {
            // TODO
        } catch (WrongSenderTypeException e) {
            // TODO
        } catch (WrongArgumentsNumberException e) {
            // TODO
        } catch (NoPermissionException e) {
            // TODO
        }

        return true;
    }

    /**
     * Checks if the current week corresponds with the week stored in the
     * {@code weeknumber} variable.
     */
    public void checkWeekChange() {
        DateFormatter date = new DateFormatter(new Date(System.currentTimeMillis()));
        if (weekNumber != date.getWeekNumber()) {
            onDisable();
            onEnable();
        }
    }

    /**
     * Cuts some elements off an array.
     * 
     * @param array
     *            The array to prune.
     * @param start
     *            The array entry from which the new array should start.
     * @param end
     *            The array entry where the new array should end (exclusive).
     * @return a new array which ranges from {@code array[start]} to
     *         {@code array[end - 1]}.
     */
    private String[] subArray(String[] array, int start, int end) {
        String[] subArray = new String[end - start];
        for (int i = 0; i < subArray.length; i++) {
            subArray[i] = array[start + i];
        }
        return subArray;
    }

}
