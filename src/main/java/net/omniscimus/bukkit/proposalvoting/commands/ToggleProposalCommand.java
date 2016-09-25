package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.exceptions.NoPermissionException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to toggle whether
 * voting on a proposal is enabled.
 */
public class ToggleProposalCommand extends ProposalCommand {

    /**
     * Creates a new ToggleProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public ToggleProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Enables voting on the proposal if it is currently disabled, and vice
     * versa.
     */
    @Override
    public void run() throws NoPermissionException, WrongSyntaxException {
        if (!sender.hasPermission("proposalvoting.admin"))
            throw new NoPermissionException();
        if (args.length != 2)
            throw new WrongSyntaxException();
        int id4;
        try {
            id4 = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }
        if (toggleVoting(id4))
            sender.sendMessage(ChatColor.GOLD + "Voting on proposal with ID " + id4 + " is now enabled.");
        else
            sender.sendMessage(ChatColor.GOLD + "Voting on proposal with ID " + id4 + " is now disabled.");
    }

    /**
     * Toggles whether voting on this proposal is enabled.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @return {@code true} if voting was enabled; {@code false} if voting was
     *         disabled.
     */
    private boolean toggleVoting(int proposalId) {
        boolean newValue = !Configuration.getVotingEnabled(proposalId);
        Configuration.setVotingEnabled(proposalId, newValue);
        return newValue;
    }

}
