package io.github.Leonardo0013YT.UltraMinions.database.data;

import io.github.Leonardo0013YT.UltraMinions.interfaces.DataSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataSave implements DataSave {

    private UUID uuid;
    private int unlocked;
    private long lastLogin;
    private HashMap<String, Integer> levels = new HashMap<>();
    private ArrayList<String> data = new ArrayList<>();

    @Override
    public ArrayList<String> getData() {
        return data;
    }

    @Override
    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public HashMap<String, Integer> getLevels() {
        return levels;
    }

    @Override
    public void setLevels(HashMap<String, Integer> levels) {
        this.levels = levels;
    }

    @Override
    public int getUnlocked() {
        return unlocked;
    }

    @Override
    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }

    @Override
    public long getLastLogin() {
        return lastLogin;
    }

    @Override
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}