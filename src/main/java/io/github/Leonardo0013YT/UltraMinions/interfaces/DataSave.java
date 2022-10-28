package io.github.Leonardo0013YT.UltraMinions.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface DataSave {

    ArrayList<String> getData();

    void setData(ArrayList<String> data);

    HashMap<String, Integer> getLevels();

    void setLevels(HashMap<String, Integer> levels);

    int getUnlocked();

    void setUnlocked(int unlocked);

    long getLastLogin();

    void setLastLogin(long lastLogin);

    UUID getUuid();

    void setUuid(UUID uuid);

}
