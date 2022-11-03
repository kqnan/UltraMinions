package io.github.Leonardo0013YT.UltraMinions.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.database.data.PlayerDataSave;
import io.github.Leonardo0013YT.UltraMinions.database.data.PlayerMinionSave;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionChest;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionStat;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionUpgrade;
import io.github.Leonardo0013YT.UltraMinions.interfaces.DataSave;
import io.github.Leonardo0013YT.UltraMinions.interfaces.MinionSave;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Database {

    private static boolean enabled;
    private static String SAVE = "UPDATE UltraMinions SET Data=? WHERE UUID=?";
    private Main plugin;
    private HikariDataSource hikari;
    private Connection connection;

    public Database(Main plugin) {
        this.plugin = plugin;
        enabled = plugin.getConfig().getBoolean("mysql.enabled");

        if (enabled) {
            int port = plugin.getConfig().getInt("mysql.port");
            String ip = plugin.getConfig().getString("mysql.host");
            String database = plugin.getConfig().getString("mysql.database");
            String username = plugin.getConfig().getString("mysql.username");
            String password = plugin.getConfig().getString("mysql.password");
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?autoReconnect=true";
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(connectionString);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("databaseName", database);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts", true);
            config.addDataSourceProperty("useLocalSessionState", true);
            config.addDataSourceProperty("rewriteBatchedStatements", true);
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            config.addDataSourceProperty("cacheServerConfiguration", true);
            config.addDataSourceProperty("elideSetAutoCommits", true);
            config.addDataSourceProperty("maintainTimeStats", false);
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("encoding", "UTF-8");
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("useSSL", false);
            config.addDataSourceProperty("tcpKeepAlive", true);
            config.setPoolName("UltraMinions " + UUID.randomUUID().toString());
            config.setMaxLifetime(Long.MAX_VALUE);
            config.setMinimumIdle(0);
            config.setIdleTimeout(30000L);
            config.setConnectionTimeout(10000L);
            config.setMaximumPoolSize(30);
            hikari = new HikariDataSource(config);
            createTable();
            plugin.sendLogMessage("§eMySQL connected correctly.");
        } else {
            File DataFile = new File(plugin.getDataFolder(), "/UltraMinions.db");
            if (!DataFile.exists()) {
                try {
                    DataFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }
            try {
                Class.forName("org.sqlite.JDBC");
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + DataFile);

                    plugin.sendLogMessage("§eSQLLite connected correctly.");
                    createTable();
                } catch (SQLException ex2) {
                    ex2.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            } catch (ClassNotFoundException ex3) {
                ex3.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    public void close() {
        if (enabled) {
            if (hikari != null) {
                hikari.close();
            }
        } else {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Connection getConnection() {
        return connection;
    }

    private HikariDataSource getHikari() {
        return hikari;
    }

    private void createTable() {
        if (enabled) {
            try {
                Connection connection = hikari.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS UltraMinions(UUID varchar(36) primary key, Name varchar(20), Data LONGTEXT);");
                statement.executeUpdate("ALTER TABLE UltraMinions MODIFY Data LONGTEXT;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS UltraMinions(UUID varchar(36) primary key, Name varchar(20), Data LONGTEXT);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayer(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                String SELECT = "SELECT * FROM UltraMinions WHERE UUID=?";
                if (enabled) {

                    try {
                        Connection connection = hikari.getConnection();
                        String INSERT = "INSERT INTO UltraMinions VALUES(?,?,?) ON DUPLICATE KEY UPDATE Name=?";
                        PreparedStatement insert = connection.prepareStatement(INSERT);
                        PreparedStatement select = connection.prepareStatement(SELECT);
                        insert.setString(1, p.getUniqueId().toString());
                        insert.setString(2, p.getName());
                        insert.setString(3, Main.toDataString(new PlayerDataSave()));
                        insert.setString(4, p.getName());
                        insert.execute();
                        select.setString(1, p.getUniqueId().toString());
                        ResultSet result = select.executeQuery();
                        if (result.next())
                            loadData(p, Main.fromDataString(result.getString("Data")));
                        close(connection, insert, result);
                        close(null, select, null);
                    } catch (SQLException ignored) {
                    }
                } else {
                    try {
                        String INSERT2 = "INSERT INTO `UltraMinions` (`UUID`, `Name`, `Data`) VALUES (?, ?, ?);";
                        PreparedStatement insert = connection.prepareStatement(INSERT2);
                        PreparedStatement select = connection.prepareStatement(SELECT);
                        select.setString(1, p.getUniqueId().toString());
                        ResultSet result = select.executeQuery();
                        if (result.next()) {
                            loadData(p, Main.fromDataString(result.getString("Data")));
                        } else {
                            insert.setString(1, p.getUniqueId().toString());
                            insert.setString(2, p.getName());
                            insert.setString(3, Main.toDataString(new PlayerDataSave()));
                            insert.executeUpdate();
                            PreparedStatement select2 = connection.prepareStatement(SELECT);
                            select2.setString(1, p.getUniqueId().toString());
                            ResultSet result2 = select2.executeQuery();
                            if (result2.next())
                                loadData(p, Main.fromDataString(result2.getString("Data")));
                            close(connection, select2, result2);
                        }
                        close(connection, insert, result);
                        close(connection, select, null);
                    } catch (SQLException ignored) {
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void savePlayer(final Player p) {
        PlayerData pd = PlayerData.getPlayerData(p);
        if (pd == null) return;
        DataSave ps = playerDataToDataSave(pd, false, false);
        if (ps == null) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (enabled) {
                    try {
                        Connection connection = hikari.getConnection();
                        PreparedStatement statement = connection.prepareStatement(SAVE);
                        statement.setString(1, Main.toDataString(ps));
                        statement.setString(2, p.getUniqueId().toString());
                        statement.execute();
                        close(connection, statement, null);
                        PlayerData.remove(p);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Connection connection = getConnection();
                        PreparedStatement statement = connection.prepareStatement(SAVE);
                        statement.setString(1, Main.toDataString(ps));
                        statement.setString(2, p.getUniqueId().toString());
                        statement.execute();
                        close(connection, statement, null);
                        PlayerData.remove(p);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void savePlayerSync(UUID p) {
        PlayerData pd = PlayerData.getPlayerUUID(p);
        if (pd == null) return;
        DataSave ps = playerDataToDataSave(pd, true, false);
        if (ps == null) return;
        if (enabled) {
            try {
                Connection connection = hikari.getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE);
                statement.setString(1, Main.toDataString(ps));
                statement.setString(2, p.toString());
                statement.execute();
                close(connection, statement, null);
                PlayerData.remove(p);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE);
                statement.setString(1, Main.toDataString(ps));
                statement.setString(2, p.toString());
                statement.execute();
                close(connection, statement, null);
                PlayerData.remove(p);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void autoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                   // plugin.sendLogMessage("自动保存玩家:"+p.getUniqueId()+"的数据");

                    PlayerData pd = PlayerData.getPlayerData(p);
                    if (pd == null) return;
                    DataSave ps = playerDataToDataSave(pd, true, true);
                    if (ps == null) return;
                    if (enabled) {
                        try {
                            Connection connection = hikari.getConnection();
                            PreparedStatement statement = connection.prepareStatement(SAVE);
                            statement.setString(1, Main.toDataString(ps));
                            statement.setString(2, p.getUniqueId().toString());
                            statement.execute();
                            close(connection, statement, null);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                      //  plugin.sendLogMessage("使用sqllite");
                        try {
                            Connection connection = getConnection();
                            PreparedStatement statement = connection.prepareStatement(SAVE);
                            statement.setString(1, Main.toDataString(ps));
                            statement.setString(2, p.getUniqueId().toString());
                            statement.execute();
                           // System.out.println(Main.toDataString(ps)+"     "+p.toString());
                            close(connection, statement, null);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.runTaskAsynchronously(plugin);
        plugin.sendLogMessage("Minions have been saved automatically.");
    }

    private void close(Connection connection, PreparedStatement statement, ResultSet result) {
        if (enabled && connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadData(Player p, DataSave loadPlayer) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (p == null || !p.isOnline()) return;
            PlayerData pd = new PlayerData(p.getUniqueId());
            pd.setLastLogin(loadPlayer.getLastLogin());
            pd.setUnlocked(loadPlayer.getUnlocked());
            pd.setLevels(loadPlayer.getLevels());
            for (String minion : new ArrayList<>(loadPlayer.getData())) {
                MinionSave ms = Main.fromMinionString(minion);
                if (!plugin.getMm().getMinions().containsKey(ms.getKey())) {
                    loadPlayer.getData().remove(minion);
                    return;
                }
                PlayerMinion pm = new PlayerMinion(Utils.getStringLocation(ms.getLoc()), ms.getKey(), p);
                pm.setStat(new PlayerMinionStat(pm, ms.getLevel(), ms.getGenerated(), ms.getWork(), ms.getSleep(), ms.getFood(), ms.getHealth(), ms.getFuelTime()));
                PlayerMinionUpgrade upgrade = new PlayerMinionUpgrade(pm);
                UpgradeFuel f = plugin.getUm().getFuel(ms.getFuel());
                if (f != null) {
                    upgrade.setFuel(f);
                    pm.getStat().setAmountFuel(ms.getFuelAmount());
                    pm.getStat().setTotalFuel(ms.getTotalFuel());
                }
                UpgradeCompressor c = plugin.getUm().getCompressor(ms.getCompressor());
                if (c != null) {
                    upgrade.setCompressor(c);
                }
                UpgradeAutoSmelt as = plugin.getUm().getAutoSmelt(ms.getAutoSmelt());
                if (as != null) {
                    upgrade.setAutoSmelt(as);
                }
                UpgradeAutoSell al = plugin.getUm().getAutoSell(ms.getAutoSell());
                if (al != null) {
                    upgrade.setAutoSell(al);
                }
                pm.setUpgrade(upgrade);
                if (pm.getSpawn().getChunk().isLoaded()) {
                    pm.firstSpawn();
                    pd.getTypes().put(pm.getKey(), pd.getTypes().getOrDefault(pm.getKey(), 0) + 1);
                }
                pm.setSkin(ms.getSkin());
                if (ms.isChest()) {
                    Block b = Utils.getStringLocation(ms.getChest()).getBlock();
                    if (b.getType().equals(Material.CHEST)) {
                        pm.setChest(new PlayerMinionChest(Utils.getStringLocation(ms.getChest())));
                    } else {
                        ms.setChest(false);
                    }
                }
                pm.setChest(ms.isChest());
                int a;
                if (plugin.getCfm().isOfflineWorking()) {
                    a = getActions(pm, pd.getLastLogin(), pm.getDelay(), pm.getStat().getFood(), pm.getStat().getHealth());
                } else {
                    a = 0;
                }
                pm.setActions(ms.getActions() + a);
                pd.getMinions().put(pm.getId(), pm);
                if (!pm.getSpawn().getChunk().isLoaded()) {
                    plugin.getMm().getToSpawn().add(pm);
                }
            }
        });
    }

    public PlayerDataSave playerDataToDataSave(PlayerData pd, boolean sync, boolean autoSave) {
        PlayerDataSave pds = new PlayerDataSave();
        ArrayList<String> minionSaves = new ArrayList<>();
        pds.setUuid(pd.getUuid());
        pds.setLastLogin(System.currentTimeMillis());
        pds.setUnlocked(pd.getUnlocked());
        pds.setLevels(pd.getLevels());
        for (PlayerMinion pm : pd.getMinions().values()) {
            PlayerMinionSave pms = new PlayerMinionSave();
            pms.setActions(pm.getActions());
            pms.setKey(pm.getKey());
            pms.setType(pm.getType().name());
            pms.setSkin(pm.getSkin());
            pms.setLoc(Utils.getLocationString(pm.getSpawn()));
            pms.setLevel(pm.getStat().getLevel());
            pms.setGenerated(pm.getStat().getGenerated());
            pms.setWork(pm.getStat().getWork());
            pms.setSleep(pm.getStat().getSleep());
            pms.setFood(pm.getStat().getFood());
            pms.setHealth(pm.getStat().getHealth());
            pms.setFuelTime(pm.getStat().getFuel());
            if (pm.getUpgrade().getFuel() != null) {
                pms.setFuel(pm.getUpgrade().getFuel().getName());
                pms.setFuelAmount(pm.getStat().getAmountFuel());
                pms.setTotalFuel(pm.getStat().getTotalFuel());
            }
            if (pm.getUpgrade().getCompressor() != null) {
                pms.setCompressor(pm.getUpgrade().getCompressor().getName());
            }
            if (pm.getUpgrade().getAutoSmelt() != null) {
                pms.setAutoSmelt(pm.getUpgrade().getAutoSmelt().getName());
            }
            if (pm.getUpgrade().getAutoSell() != null) {
                pms.setAutoSell(pm.getUpgrade().getAutoSell().getName());
            }
            pms.setChest(pm.isChest());
            if (pm.isChest()) {
                pms.setChest(Utils.getLocationString(pm.getChest().getLoc()));
            }
            minionSaves.add(Main.toMinionString(pms));
            if (!autoSave) {
                removeWhenOffline(pm, sync);
            }
        }
        pds.setData(minionSaves);
        plugin.getMm().getToSpawn().removeAll(pd.getMinions().values());
        return pds;
    }

    public void removeWhenOffline(PlayerMinion pm, boolean sync) {
        if (!sync) {
            if (pm.getArmor() != null) {
                plugin.getMm().getActiveMinions().remove(pm.getArmor().getUniqueId());
                if (plugin.getAdm().hasHologramPlugin()) {
                    if (plugin.getAdm().hasHologram(pm)) {
                        plugin.getAdm().deleteHologram(pm);
                    }
                }
                pm.destroy();
            }
        }
        pm.destroy();
    }

    public int getActions(PlayerMinion pm, long lastLogin, int delay, int food, int health) {
        int passed = (int) (((System.currentTimeMillis() - lastLogin) / 1000) / delay);
        if (!plugin.getCfm().isHealth() && !plugin.getCfm().isFood()) {
            return passed / pm.getMinion().getType().getActions();
        }
        int posActions = 0;
        if (plugin.getCfm().isHealth()) {
            posActions += health;
        }
        if (plugin.getCfm().isFood()) {
            posActions += food;
        }
        if (posActions == 0) {
            return 0;
        }
        if (posActions > 0 && (plugin.getCfm().isHealth() || plugin.getCfm().isFood())) {
            if (passed > posActions) {
                pm.getStat().setFood(0);
                pm.getStat().setHealth(0);
                return posActions / pm.getMinion().getType().getActions();
            }
        }
        if (plugin.getCfm().isHealth() || plugin.getCfm().isFood()) {
            int consumed = passed;
            int nFood = food;
            int nHealth = health;
            if (consumed >= food) {
                consumed -= food;
                nFood = 0;
            } else {
                nFood -= consumed;
                consumed = 0;
            }
            if (consumed >= health) {
                nHealth = 0;
            } else {
                nHealth -= consumed;
            }
            pm.getStat().setFood(nFood);
            pm.getStat().setHealth(nHealth);
        }
        return passed / pm.getMinion().getType().getActions();
    }

    public boolean checkOtherMinion(Player p, Location loc) {
        if (!p.getWorld().getName().equals(loc.getWorld().getName())) return false;
        ArrayList<Entity> entities = new ArrayList<>(loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5));
        entities.removeIf(entity -> !entity.getType().equals(EntityType.ARMOR_STAND));
        for (Entity as : entities) {
            UUID uuid = as.getUniqueId();
            if (Utils.isMinionUUID(uuid)) {
                PlayerMinion pm = plugin.getMm().getActiveMinions().get(uuid);
                return pm != null;
            }
        }
        return false;
    }

}