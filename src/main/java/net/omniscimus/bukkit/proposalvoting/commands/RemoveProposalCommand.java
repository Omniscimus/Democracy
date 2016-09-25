package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.exceptions.NoPermissionException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to perform an
 * operation on voting proposals.
 */
public class RemoveProposalCommand extends ProposalCommand {

    /**
     * Creates a new RemoveProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public RemoveProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Permanently deletes the proposal specified in the command arguments.
     */
    @Override
    public void run() throws NoPermissionException, WrongSyntaxException {
        if (!sender.hasPermission("proposalvoting.admin"))
            throw new NoPermissionException();
        if (args.length != 2) {
            throw new WrongSyntaxException();
        }
        int id2;
        try {
            id2 = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }
        if (removeProposal(id2)) {
            sender.sendMessage(ChatColor.GOLD + "Successfully deleted proposal with ID '" + ChatColor.RED + id2
                    + ChatColor.GOLD + "'.");
        } else {
            sender.sendMessage(
                    ChatColor.GOLD + "Proposal with ID '" + ChatColor.RED + id2 + ChatColor.GOLD + "' does not exist!");
        }
    }

    /**
     * Permanently deletes the proposal.
     * 
     * @param id
     *            The ID of the proposal to delete.
     * @return {@code true} if the proposal was successfully deleted.
     */
    private boolean removeProposal(int id) {
        return Configuration.deleteProposal(id);
    }

}
