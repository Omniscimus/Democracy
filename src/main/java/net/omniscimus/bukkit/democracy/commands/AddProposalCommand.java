package net.omniscimus.bukkit.democracy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.democracy.Configuration;
import net.omniscimus.bukkit.democracy.exceptions.NoPermissionException;

/**
 * Represents the command that is issued by senders who intend to create a new
 * proposal.
 */
public class AddProposalCommand extends ProposalCommand {

    /**
     * Creates a new AddProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public AddProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Sends the CommandSender the next unused proposal ID.
     */
    @Override
    public void run() throws NoPermissionException {
        if (!sender.hasPermission("democracy.admin"))
            throw new NoPermissionException();
        int id1 = createProposal();
        sender.sendMessage(
                ChatColor.GOLD + "You can create a new proposal by using the edit command using the proposal ID: '"
                        + ChatColor.RED + id1 + ChatColor.GOLD + "'.");
    }

    /**
     * Gets an ID that can be used for a new proposal. Actually doesn't create
     * or save the proposal.
     * 
     * @return a usable proposal ID.
     */
    private int createProposal() {
        return Configuration.getNextProposalId();
    }

}
