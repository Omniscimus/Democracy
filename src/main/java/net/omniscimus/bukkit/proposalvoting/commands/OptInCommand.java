package net.omniscimus.bukkit.proposalvoting.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.PluginCommand;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSenderTypeException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by a player when he intends to opt in
 * to the ProposalVoting system.
 */
public class OptInCommand extends PluginCommand {

    /**
     * The message that should be sent to the command sender when he has
     * successfully opted in.
     */
    private static final String OPTED_IN = ChatColor.GOLD + "You have opted in to the ProposalVoting system. "
            + "Your total amount of playtime per week will be logged. "
            + "This is to make sure that only active players can vote.";

    /**
     * The message that should be sent to the command sender when he had already
     * opted in.
     */
    private static final String ALREADY_OPTED_IN = ChatColor.RED + "You have already opted in to the ProposalVoting system.";

    /**
     * Creates a new PluginCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public OptInCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Registers the command sender, if possible, as opted in to the ProposalVoting
     * system.
     */
    @Override
    public void run() throws WrongSyntaxException, WrongSenderTypeException {
        if (args.length > 0) {
            throw new WrongSyntaxException();
        }

        if (!(sender instanceof Player)) {
            throw new WrongSenderTypeException();
        }

        UUID id = ((Player) sender).getUniqueId();
        if (!Configuration.playerHasOptedIn(id)) {
            Configuration.setPlayerOptIn(id, true);
            sender.sendMessage(OPTED_IN);
        } else {
            sender.sendMessage(ALREADY_OPTED_IN);
        }
    }

}
