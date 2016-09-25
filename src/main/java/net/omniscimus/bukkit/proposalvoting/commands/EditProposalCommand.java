package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.exceptions.NoPermissionException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to modify voting
 * proposals.
 */
public class EditProposalCommand extends ProposalCommand {

    /**
     * Creates a new EditProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public EditProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Changes a value in a proposal, both of which are specified by the
     * CommandSender.
     */
    @Override
    public void run() throws WrongSyntaxException, NoPermissionException {
        if (!sender.hasPermission("proposalvoting.admin"))
            throw new NoPermissionException();
        if (args.length < 4)
            throw new WrongSyntaxException();
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }
        editProposal(id, args);
        sender.sendMessage(ChatColor.GOLD + "The proposal was changed successfully.");
    }

    /**
     * Changes a value in a proposal.
     * 
     * @param proposalId
     *            The ID of the proposal to modify.
     * @param args
     *            The arguments of the command.
     * @throws WrongSyntaxException
     *             If the arguments are invalid.
     */
    private void editProposal(int proposalId, String[] args) throws WrongSyntaxException {
        switch (args[2]) {
        case "title":
            Configuration.updateProposalTitle(proposalId, concatenateArgs(args, 3, args.length - 1));
            break;
        case "description":
            Configuration.updateProposalDescription(proposalId, concatenateArgs(args, 3, args.length - 1));
            break;
        case "option":
            if (args.length < 5)
                throw new WrongSyntaxException();
            int optionId;
            try {
                optionId = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                throw new WrongSyntaxException();
            }
            Configuration.setProposalOptionDescription(proposalId, optionId, concatenateArgs(args, 4, args.length - 1));
            break;
        default:
            throw new WrongSyntaxException();
        }
    }

    /**
     * Concatenates entries in the String array and puts a space in between
     * every time.
     * 
     * @param args
     *            The array to convert to a String.
     * @param start
     *            The first entry that should be appended to the String.
     * @param end
     *            The last entry that should be appended to the String.
     * @return a String containing the elements of {@code args}, with a space in
     *         between each element.
     */
    String concatenateArgs(String[] args, int start, int end) {
        StringBuilder argsConcat = new StringBuilder();
        for (int i = 3; i <= end; i++) {
            argsConcat.append(args[i]);
            if (i == args.length - 1)
                argsConcat.append(" ");
        }
        return argsConcat.toString();
    }

}
