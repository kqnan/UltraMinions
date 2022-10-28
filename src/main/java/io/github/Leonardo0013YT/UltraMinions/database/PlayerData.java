package io.github.Leonardo0013YT.UltraMinions.database;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private static final HashMap<UUID, PlayerData> players = new HashMap<>();
    private final UUID uuid;
    private final HashMap<String, PlayerMinion> minions = new HashMap<>();
    private final HashMap<String, Integer> types = new HashMap<>();
    private int unlocked, maxMinion = 3;
    private long lastLogin;
    private HashMap<String, Integer> levels = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        players.put(uuid, this);
    }

    public static PlayerData getPlayerData(Player p) {
        return players.get(p.getUniqueId());
    }

    public static PlayerData getPlayerUUID(UUID uuid) {
        return players.get(uuid);
    }

    public static void remove(Player p) {
        players.remove(p.getUniqueId());
    }

    public static void remove(UUID p) {
        players.remove(p);
    }

    public static HashMap<UUID, PlayerData> getPlayers() {
        return players;
    }

    public boolean isNewUnlocked(String key, int level) {
        if (!levels.containsKey(key)) {
            return true;
        }
        int l = levels.get(key);
        if (l >= level) {
            return false;
        }
        return l + 1 == level;
    }

    public boolean isUnlocked(String key, int level) {
        if (!levels.containsKey(key)) {
            return false;
        }
        int l = levels.get(key);
        return l >= level;
    }

    public int getMaxMinion() {
        return maxMinion;
    }

    public void setMaxMinion(int maxMinion) {
        this.maxMinion = maxMinion;
    }

    public void setLevel(String key, int level) {
        levels.put(key, level);
    }

    public HashMap<String, Integer> getLevels() {
        return levels;
    }

    public void setLevels(HashMap<String, Integer> levels) {
        this.levels = levels;
    }

    public HashMap<String, Integer> getTypes() {
        return types;
    }

    public int getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void addPlayerMinion(PlayerMinion pm) {
        minions.put(pm.getId(), pm);
    }

    public void removePlayerMinion(PlayerMinion pm) {
        minions.remove(pm.getId());
    }

    public HashMap<String, PlayerMinion> getMinions() {
        return minions;
    }

    public int getMinionSize() {
        return minions.size();
    }

    public UUID getUuid() {
        return uuid;
    }

}