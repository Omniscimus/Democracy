package net.omniscimus.bukkit.democracy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.omniscimus.bukkit.democracy.PluginCommand;
import net.omniscimus.bukkit.democracy.Voter;
import net.omniscimus.bukkit.democracy.exceptions.WrongSenderTypeException;
import net.omniscimus.bukkit.democracy.exceptions.WrongSyntaxException;

/**
 * Represents the command that is issued by a player who intends to vote on a
 * proposal option.
 */
public class VoteCommand extends PluginCommand {

    /**
     * Creates a new VoteCommand instance.
     * 
     * @param sender
     *            The instance which sent this command.
     * @param args
     *            The command arguments.
     */
    public VoteCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    /**
     * Casts the sender's vote on his preferred proposal option, if he satisfies
     * all requirements.
     */
    @Override
    public void run() throws WrongSenderTypeException, WrongSyntaxException {

        // TODO summary of proposals and options.
        // TODO some text stating that multi-voting is prohibited, people should
        // think carefully before voting, command syntax

        if (!(sender instanceof Player)) {
            throw new WrongSenderTypeException();
        }

        if (args.length != 2) {
            throw new WrongSyntaxException();
        }

        int proposalId, optionId;
        try {
            proposalId = Integer.parseInt(args[0]);
            optionId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongSyntaxException();
        }

        Voter voter = new Voter(((Player) sender).getUniqueId());
        if (voter.vote(proposalId, optionId)) {
            sender.sendMessage(ChatColor.GOLD + "Your vote has been registered.");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Voting failed."); // TODO specify: already voted, or not opted in, or invalid proposal, or invalid option
        }
    }

}
