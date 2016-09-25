package net.omniscimus.bukkit.proposalvoting.commands;

import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.proposalvoting.PluginCommand;
import net.omniscimus.bukkit.proposalvoting.exceptions.NoPermissionException;
import net.omniscimus.bukkit.proposalvoting.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by senders who intend to perform an
 * operation on voting proposals.
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
            new CheckProposalCommand(sender, args).run();
            break;
        case "add":
            new AddProposalCommand(sender, args).run();
            break;
        case "remove":
            new RemoveProposalCommand(sender, args).run();
            break;
        case "edit":
            new EditProposalCommand(sender, args).run();
            break;
        case "toggle":
            new ToggleProposalCommand(sender, args).run();
            break;
        default:
            throw new WrongSyntaxException();
        }
    }

}
