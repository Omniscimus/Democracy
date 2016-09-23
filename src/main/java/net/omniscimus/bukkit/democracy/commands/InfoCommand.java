package net.omniscimus.bukkit.democracy.commands;

import org.bukkit.command.CommandSender;

import net.omniscimus.bukkit.democracy.PluginCommand;
import net.omniscimus.bukkit.democracy.exceptions.WrongArgumentsNumberException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSenderTypeException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued when the sender should receive
 * information about this plugin.
 */
public class InfoCommand extends PluginCommand {

    /**
     * Creates a new InfoCommand.
     * 
     * @param sender
     *            The entity who sent this command.
     * @param args
     *            The command arguments.
     */
    public InfoCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Sends the command sender information about this plugin.
     */
    @Override
    public void run() throws WrongArgumentsNumberException, WrongSenderTypeException, WrongSyntaxException {
        throw new UnsupportedOperationException(); // TODO
    }

}
