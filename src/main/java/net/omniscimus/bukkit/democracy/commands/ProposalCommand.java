package net.omniscimus.bukkit.democracy.commands;

import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.democracy.PluginCommand;
import net.omniscimus.bukkit.democracy.exceptions.NoPermissionException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSyntaxException;

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
            ((CheckProposalCommand) this).run();
            break;
        case "add":
            ((AddProposalCommand) this).run();
            break;
        case "remove":
            ((RemoveProposalCommand) this).run();
            break;
        case "edit":
            ((EditProposalCommand) this).run();
            break;
        case "toggle":
            ((ToggleProposalCommand) this).run();
            break;
        default:
            throw new WrongSyntaxException();
        }
    }

}
