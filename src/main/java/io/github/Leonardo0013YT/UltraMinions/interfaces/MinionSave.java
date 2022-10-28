package io.github.Leonardo0013YT.UltraMinions.interfaces;

public interface MinionSave {

    int getActions();

    void setActions(int actions);

    int getFuelAmount();

    void setFuelAmount(int fuelAmount);

    int getTotalFuel();

    void setTotalFuel(int totalFuel);

    String getType();

    void setType(String type);

    String getKey();

    void setKey(String key);

    String getSkin();

    void setSkin(String skin);

    String getLoc();

    void setLoc(String loc);

    String getChest();

    String getFuel();

    void setFuel(String fuel);

    String getAutoSell();

    void setAutoSell(String autoSell);

    String getCompressor();

    void setCompressor(String compressor);

    String getAutoSmelt();

    void setAutoSmelt(String autoSmelt);

    boolean isChest();

    void setChest(String chest);

    void setChest(boolean chest);

    long getFuelTime();

    void setFuelTime(long fuelTime);

    int getLevel();

    void setLevel(int level);

    int getGenerated();

    void setGenerated(int generated);

    int getWork();

    void setWork(int work);

    int getSleep();

    void setSleep(int sleep);

    int getFood();

    void setFood(int food);

    int getHealth();

    void setHealth(int health);

}
