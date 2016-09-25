package net.omniscimus.bukkit.democracy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.democracy.Configuration;
import net.omniscimus.bukkit.democracy.PluginCommand;
import net.omniscimus.bukkit.democracy.exceptions.NoPermissionException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders which intend to modify
 * voting proposals.
 */
public class ProposalCommand extends PluginCommand {

    /**
     * Creates a new ProposalCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public ProposalCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Checks which action to perform based on the command arguments, and then
     * performs that action.
     */
    @Override
    public void run() throws NoPermissionException, WrongSyntaxException {
        if (args.length == 0) {
            throw new WrongSyntaxException();
        }

        switch (args[0]) {
        case "check":
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
            break;
        case "add":
            if (!sender.hasPermission("democracy.admin"))
                throw new NoPermissionException();
            int id1 = createProposal();
            sender.sendMessage(ChatColor.GOLD + "A new proposal with ID '" + ChatColor.RED + id1 + ChatColor.GOLD
                    + "' has been created.");
            // Kek, actually it wasn't created, but it will be created as soon
            // as the commandsender adds information
            // TODO be honest
            break;
        case "remove":
            if (!sender.hasPermission("democracy.admin"))
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
                sender.sendMessage(ChatColor.GOLD + "Proposal with ID '" + ChatColor.RED + id2 + ChatColor.GOLD
                        + "' does not exist!");
            }
            break;
        case "edit":
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
            break;
        case "toggle":
            if (!sender.hasPermission("democracy.admin"))
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
            break;
        default:
        }
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

    /**
     * Gets the current number of votes on a proposal option.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param optionId
     *            The ID of the option.
     * @return the amount of votes on this proposal option
     */
    private int checkVotes(int proposalId, int optionId) {
        return Configuration.getProposalOptionVotes(proposalId, optionId);
    }

}
