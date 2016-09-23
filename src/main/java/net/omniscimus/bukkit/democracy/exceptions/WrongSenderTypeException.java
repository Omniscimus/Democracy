package net.omniscimus.bukkit.democracy.exceptions;

/**
 * Thrown if a CommandSender sent a command to this plugin, but was not of the
 * correct type. E.g. the console sent a command that could only be sent by a
 * player.
 */
public class WrongSenderTypeException extends Exception {

    private static final long serialVersionUID = -7678711768045340994L;

}
