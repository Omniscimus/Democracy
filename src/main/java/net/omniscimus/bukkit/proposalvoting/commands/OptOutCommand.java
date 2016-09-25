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
 * Represents the command that is issued by a player when he intends to opt out
 * of the ProposalVoting system.
 */
public class OptOutCommand extends PluginCommand {

    /**
     * The message that should be sent to the command sender when he has
     * successfully opted out.
     */
    private static final String OPTED_OUT = ChatColor.GOLD + "You have opted out of the ProposalVoting system. "
            + "Your playtime will no longer be logged. "
            + "Please note that the consequence of this is that you will not be able to vote.";

    /**
     * The message that should be sent to the command sender when he had already
     * opted in.
     */
    private static final String ALREADY_OPTED_OUT = ChatColor.RED
            + "You are already opted out of the ProposalVoting system.";

    /**
     * Creates a new OptOutCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public OptOutCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Registers the command sender as opted out of the ProposalVoting system.
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
        if (Configuration.playerHasOptedIn(id)) {
            Configuration.setPlayerOptIn(id, false);
            sender.sendMessage(OPTED_OUT);
        } else {
            sender.sendMessage(ALREADY_OPTED_OUT);
        }
    }

}
