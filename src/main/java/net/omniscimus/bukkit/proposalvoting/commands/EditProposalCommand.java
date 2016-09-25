package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.Configuration;
import net.omniscimus.bukkit.proposalvoting.exceptions.NoPermissionException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to modify
 * voting proposals.
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
        if (!sender.hasPermission("democracy.admin"))
            throw new NoPermissionException();
        if (args.length < 4)
            throw new WrongSyntaxException();
        int id3;
        try {
            id3 = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }
        StringBuilder argsConcat = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            argsConcat.append(args[i]);
        }
        editProposal(id3, args[2], argsConcat.toString());
        // TODO feedback
        // TODO make it possible to add options
    }

    /**
     * Changes a value in a proposal.
     * 
     * @param proposalId
     *            The ID of the proposal to modify.
     * @param thingToEdit
     *            The value to modify. One of {@code "title"} or
     *            {@code "description"}.
     * @param newValue
     *            The new value.
     * @throws WrongSyntaxException
     *             If the {@code thingToEdit} parameter is invalid.
     */
    private void editProposal(int proposalId, String thingToEdit, String newValue) throws WrongSyntaxException {
        switch (thingToEdit) {
        case "title":
            Configuration.updateProposalTitle(proposalId, newValue);
            break;
        case "description":
            Configuration.updateProposalDescription(proposalId, newValue);
            break;
        default:
            throw new WrongSyntaxException();
        }
    }

}
