package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to check the
 * number of votes on a certain proposal option.
 */
public class CheckProposalCommand extends ProposalCommand {

    /**
     * Creates a new CheckProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public CheckProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Sends the CommandSender the current amount of votes on the specified
     * proposal option.
     */
    @Override
    public void run() throws WrongSyntaxException {
        if (args.length != 3) {
            throw new WrongSyntaxException();
        }
        int propId, optionId;
        try {
            propId = Integer.parseInt(args[1]);
            optionId = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }
        sender.sendMessage(ChatColor.RED + "" + checkVotes(propId, optionId) + ChatColor.GOLD
                + " players have voted for this proposal option.");
    }

    /**
     * Gets the current number of votes on a proposal option.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param optionId
     *            The ID of the option.
     * @return the amount of votes on this proposal option
     */
    public static int checkVotes(int proposalId, int optionId) {
        return Configuration.getProposalOptionVotes(proposalId, optionId);
    }

}
