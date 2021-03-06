package net.omniscimus.bukkit.proposalvoting;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents the plugin's configuration. These values are stored in the file
 * 'config.yml'.
 */
public class Configuration {

    // TODO config version
    // TODO don't store playtime in milliseconds, lol

    private static Main plugin;
    private static FileConfiguration config;

    /**
     * Sets the FileConfiguration to use for the configuration options, and
     * keeps the Main instance for saving it.
     * 
     * @param config
     *            the FileConfiguration to use
     */
    public static void init(Main plugin) {
        Configuration.plugin = plugin;
        Configuration.config = plugin.getConfig();
    }

    /**
     * Saves the configuration values to the file.
     */
    public static void saveConfig() {
        plugin.saveConfig();
    }

    /**
     * Gets whether a player has opted in to the ProposalVoting system, thereby
     * approving of logging his playtime.
     * 
     * @param playerId
     *            The Unique User ID of this player, as generated by Minecraft.
     * @return {@code true} if the player has opted in.
     */
    public static boolean playerHasOptedIn(UUID playerId) {
        return config.getBoolean("player." + playerId + ".opt-in", false);
    }

    /**
     * Changes whether a player has opted in to the ProposalVoting system,
     * thereby approving of logging his playtime.
     * 
     * @param playerId
     *            The Unique User ID of this player, as generated by Minecraft.
     * @param optIn
     *            A boolean indicating whether this player has opted in.
     */
    public static void setPlayerOptIn(UUID playerId, boolean optIn) {
        config.set("player." + playerId + ".opt-in", optIn);
        saveConfig();
    }

    /**
     * Gets the player's amount of played time over the specified week.
     * 
     * @param playerId
     *            The Unique User ID of this player, as generated by Minecraft.
     * @param year_week
     *            A String indicating the year and week over which to look up
     *            the playtime, formatted as in %y_%w.
     * @return the player's amount of played time, in milliseconds.
     */
    public static long getPlaytime(UUID playerId, String year_week) {
        return config.getLong("playtime." + year_week + "." + playerId, 0L);
    }

    /**
     * Changes the player's amount of played time over the specified week.
     * 
     * @param playerId
     *            The Unique User ID of this player, as generated by Minecraft.
     * @param year_week
     *            A String indicating the year and week over which to look up
     *            the playtime, formatted as in %y_%w.
     * @param playtime
     *            The player's amount of played time, in milliseconds.
     */
    public static void setPlaytime(UUID playerId, String year_week, long playtime) {
        config.set("playtime." + year_week + "." + playerId, playtime);
        saveConfig();
    }

    /**
     * Gets the configured amount of playtime that is required for a player to
     * be allowed to vote.
     * 
     * @return the playtime requirement, in milliseconds.
     */
    public static long getPlaytimeRequirement() {
        return config.getLong("electorate-requirements.playtime", 0L);
    }

    /**
     * Changes the configured amount of playtime that is required for a player
     * to be allowed to vote.
     * 
     * @param playtime
     *            The playtime requirement, in milliseconds.
     */
    public static void setPlaytimeRequirement(long playtime) {
        config.set("electorate-requirements.playtime", playtime);
        saveConfig();
    }

    /**
     * Gets an ID that may be used for a new proposal.
     * 
     * @return the next still unused proposal ID.
     */
    public static int getNextProposalId() {
        ConfigurationSection proposals = config.getConfigurationSection("proposals");
        if (proposals == null)
            return 1;
        int highestId = 0;
        for (String idStr : proposals.getKeys(false)) {
            int id = Integer.parseInt(idStr);
            if (id > highestId)
                highestId = id;
        }
        return highestId++;
    }

    /**
     * Gets all used proposal IDs.
     * 
     * @param active
     *            A boolean indicating if only the IDs of the currently active
     *            proposals should be returned.
     * @return a Set containing used proposal IDs, in String format; these
     *         should be parsable to Integers.
     */
    public Set<String> getProposalIds(boolean active) {
        ConfigurationSection propSection = config.getConfigurationSection("proposals.");
        if (propSection == null)
            return new HashSet<String>();
        Set<String> proposals = propSection.getKeys(false);
        if (!active)
            return proposals;
        else {
            Set<String> activeProposals = new HashSet<>();
            for (String s : proposals) {
                if (propSection.getBoolean(s + ".active", false))
                    activeProposals.add(s);
            }
            return activeProposals;
        }
    }

    /**
     * Gets the title of a certain proposal.
     * 
     * @param id
     *            The ID of the proposal of which the title should be looked up.
     * @return the title of the proposal
     */
    public static String getProposalTitle(int id) {
        return config.getString("proposals." + id + ".title");
    }

    /**
     * Changes the title of a certain proposal.
     * 
     * @param id
     *            The ID of the proposal of which the title should be changed.
     * @param newTitle
     *            The new title for the proposal.
     */
    public static void updateProposalTitle(int id, String newTitle) {
        config.set("proposals." + id + ".title", newTitle);
        saveConfig();
    }

    /**
     * Gets the description of a certain proposal.
     * 
     * @param id
     *            The ID of the proposal of which the description should be
     *            looked up.
     * @return the description of the proposal
     */
    public static String getProposalDescription(int id) {
        return config.getString("proposals." + id + ".description");
    }

    /**
     * Changes the description of a certain proposal.
     * 
     * @param id
     *            The ID of the proposal of which the description should be
     *            changed.
     * @param newDescription
     *            The new description for the proposal.
     */
    public static void updateProposalDescription(int id, String newDescription) {
        config.set("proposals." + id + ".description", newDescription);
        saveConfig();
    }

    /**
     * Gets all option IDs for a certain proposal.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @return a Set containing used option IDs, in String format; these should
     *         be parsable to Integers.
     */
    public Set<String> getProposalOptionIds(int proposalId) {
        ConfigurationSection optionsSection = config.getConfigurationSection("proposals." + proposalId + ".options");
        if (optionsSection == null)
            return new HashSet<String>();
        return optionsSection.getKeys(false);
    }

    /**
     * Gets the description of an option on which players can vote for a certain
     * proposal.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param optionId
     *            The ID of the option.
     * @return the description of the option.
     */
    public static String getProposalOptionDescription(int proposalId, int optionId) {
        return config.getString("proposals." + proposalId + ".options." + optionId + ".description");
    }

    /**
     * Changes the description of an option on which players can vote for a
     * certain proposal.
     * 
     * @param proposalId
     *            The ID of the proposal of which an option should be changed.
     * @param optionId
     *            The ID of the option which should be changed.
     * @param option
     *            The new description for the option.
     */
    public static void setProposalOptionDescription(int proposalId, int optionId, String option) {
        config.set("proposals." + proposalId + ".options." + optionId + ".description", option);
        saveConfig();
    }

    /**
     * Gets the amount of votes on an option of a certain proposal.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param optionId
     *            The ID of the option.
     * @return the amount of votes
     */
    public static int getProposalOptionVotes(int proposalId, int optionId) {
        return config.getInt("proposals." + proposalId + ".options." + optionId + ".votes");
    }

    /**
     * Changes the amount of votes on an option of a certain proposal.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param optionId
     *            The ID of the option.
     * @param votes
     *            The new amount of votes.
     */
    public static void setProposalOptionVotes(int proposalId, int optionId, int votes) {
        config.set("proposals." + proposalId + ".options." + optionId + ".votes", votes);
        saveConfig();
    }

    /**
     * Gets whether a player has voted on a certain proposal.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param playerId
     *            The Unique User ID of the player, as generated by Minecraft.
     * @return {@code true} if the player has voted on this proposal.
     */
    public static boolean playerHasVoted(int proposalId, UUID playerId) {
        return config.getBoolean("proposals." + proposalId + ".players-voted." + playerId);
    }

    /**
     * Changes if a player has voted on a certain proposal. Note: this does not
     * remove the player's actual vote, but only the check on the player
     * himself.
     * 
     * @param proposalId
     *            The ID of the proposal.
     * @param playerId
     *            The Unique User ID of the player, as generated by Minecraft.
     * @param hasVoted
     *            A boolean indicating whether this player has voted on this
     *            proposal.
     */
    public static void setPlayerHasVoted(int proposalId, UUID playerId, boolean hasVoted) {
        config.set("proposals." + proposalId + ".players-voted." + playerId, hasVoted);
        saveConfig();
    }

    /**
     * Gets if voting is enabled for a certain proposal.
     * 
     * @param id
     *            The ID of the proposal.
     * @return {@code true} if voting is enabled.
     */
    public static boolean getVotingEnabled(int id) {
        return config.getBoolean("proposals." + id + ".enabled", false);
    }

    /**
     * Changes if voting is enabled for a certain proposal.
     * 
     * @param id
     *            The ID of the proposal.
     * @param enabled
     *            A boolean indicating whether voting should be enabled for this
     *            proposal.
     */
    public static void setVotingEnabled(int id, boolean enabled) {
        config.set("proposals." + id + ".enabled", enabled);
        saveConfig();
    }

    /**
     * Permanently deletes an entire proposal, including its options and votes.
     * 
     * @param id
     *            The ID of the proposal.
     * @return {@code true} if the proposal was deleted; {@code false} if the
     *         proposal did not exist.
     */
    public static boolean deleteProposal(int id) {
        if (config.getConfigurationSection("proposals." + id) == null)
            return false;
        config.set("proposals." + id, null);
        saveConfig();
        return true;
    }

}
