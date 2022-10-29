package io.github.Leonardo0013YT.UltraMinions.listeners;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionCollectEvent;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionLoadEvent;
import io.github.Leonardo0013YT.UltraMinions.api.events.MinionUnloadEvent;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerData;
import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.database.minion.PlayerMinionUpgrade;
import io.github.Leonardo0013YT.UltraMinions.food.Food;
import io.github.Leonardo0013YT.UltraMinions.managers.MinionManager;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import io.github.Leonardo0013YT.UltraMinions.minions.levels.MinionLevel;
import io.github.Leonardo0013YT.UltraMinions.tiers.Tier;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;
import io.github.Leonardo0013YT.UltraMinions.utils.MathUtils;
import io.github.Leonardo0013YT.UltraMinions.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener implements Listener {

    private final Main plugin;
    private final Map<UUID, Long> lastRemove = new HashMap<>(), lastClick = new HashMap<>();

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (plugin.isStop()) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(plugin.getLang().get("messages.inStopLogin"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStop(PlayerCommandPreprocessEvent e) {
        if (!plugin.getCfm().isSecureStop()) return;
        if (plugin.isStop()) return;
        String cmd = e.getMessage().substring(1).split(" ", 2)[0].toLowerCase();
        if ((cmd.equals("minecraft:stop") || cmd.equals("bukkit:stop") || cmd.equals("stop"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(ServerCommandEvent e) {
        if (!plugin.getCfm().isSecureStop()) return;
        if (plugin.isStop()) return;
        String cmd = e.getCommand().split(" ", 2)[0].toLowerCase();
        if (cmd.equals("minecraft:stop") || cmd.equals("bukkit:stop") || cmd.equals("stop")) {
            e.setCancelled(true);
            shutdown();
        }
    }

    public void shutdown() {
        plugin.setStop(true);
        int amount = Bukkit.getOnlinePlayers().size();
        ArrayList<PlayerData> uuids = new ArrayList<>(PlayerData.getPlayers().values());
        for (PlayerData pd : uuids) {
            plugin.getDb().savePlayerSync(pd.getUuid());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(plugin, amount * 5L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new PlayerData(p.getUniqueId());
        plugin.getDb().loadPlayer(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.getDb().savePlayer(p);
        lastRemove.remove(p.getUniqueId());
        lastClick.remove(p.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        plugin.getDb().savePlayer(p);
        lastRemove.remove(p.getUniqueId());
        lastClick.remove(p.getUniqueId());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onLoad(ChunkLoadEvent e) {
        if (e.isAsynchronous()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> load(e));
        } else {
            load(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunk(ChunkUnloadEvent e) {
        if (e.isAsynchronous()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unload(e));
        } else {
            unload(e);
        }
    }

    @EventHandler
    public void onBurn(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand && (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))) {
            ArmorStand stand = (ArmorStand) e.getEntity();
            PlayerMinion pm = plugin.getMm().getActiveMinions().get(stand.getUniqueId());
            if (pm == null) {
                return;
            }
            e.setCancelled(true);
            stand.setFireTicks(0);
        }
    }

    @EventHandler
    public void onExtend(BlockPistonExtendEvent e) {
        Block bl = e.getBlock();
        if (isInBlockRadius(bl)) {
            e.setCancelled(true);
        }
        for (Block b : e.getBlocks()) {
            if (b instanceof Skull || isInBlockRadius(b)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (b instanceof Skull || isInBlockRadius(b)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onIgnite(EntityCombustEvent e) {
        if (e.getEntity().hasMetadata("MINION")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                ArmorStand stand = (ArmorStand) e.getEntity();
                PlayerMinion pm = plugin.getMm().getActiveMinions().get(stand.getUniqueId());
                if (pm == null) {
                    return;
                }
                e.setCancelled(true);
                if (!plugin.getCfm().isDestroyToRemove()) {
                    p.sendMessage(plugin.getLang().get("messages.noDestroy"));
                    return;
                }
                if (plugin.getCfm().isAdminBypass() && (p.isOp() || p.hasPermission("ultraminions.admin"))) {
                    lastRemove.put(p.getUniqueId(), System.currentTimeMillis());
                    Player on = pm.getP();
                    PlayerData pd = PlayerData.getPlayerUUID(on.getUniqueId());
                    Minion m = plugin.getMm().getMinion(pm.getKey());
                    MinionLevel ml = m.getMinionLevelByLevel(pm.getStat().getLevel());
                    MathUtils mu = new MathUtils(pm, pm.getUpgrade().getCompressor(), pm.getUpgrade().getAutoSmelt(), pm.getUpgrade().getAutoSell());
                    mu.fill();
                    plugin.getMm().getActiveMinions().remove(stand.getUniqueId());
                    pm.destroy();
                    pd.removePlayerMinion(pm);
                    remove(p, pm, pd, ml);
                    pd.getTypes().put(pm.getKey(), Math.max(pd.getTypes().getOrDefault(pm.getKey(), 0) - 1, 0));
                    on.sendMessage(plugin.getLang().get("messages.adminRemoved"));
                    return;
                }
                boolean permited = (plugin.getAdm().isAddon()) ? (plugin.getAdm().checkMember(p) && plugin.getCfm().isRemoveMinion()) || pm.getP().getUniqueId().equals(p.getUniqueId()) : pm.getP().getUniqueId().equals(p.getUniqueId());
                if (permited) {
                    lastRemove.put(p.getUniqueId(), System.currentTimeMillis());
                    PlayerData pd = PlayerData.getPlayerUUID(pm.getP().getUniqueId());
                    Minion m = plugin.getMm().getMinion(pm.getKey());
                    MinionLevel ml = m.getMinionLevelByLevel(pm.getStat().getLevel());
                    plugin.getMm().getActiveMinions().remove(stand.getUniqueId());
                    pm.destroy();
                    pd.removePlayerMinion(pm);
                    MathUtils mu = new MathUtils(pm, pm.getUpgrade().getCompressor(), pm.getUpgrade().getAutoSmelt(), pm.getUpgrade().getAutoSell());
                    mu.fill();
                    pd.getTypes().put(pm.getKey(), Math.max(pd.getTypes().getOrDefault(pm.getKey(), 0) - 1, 0));
                    remove(p, pm, pd, ml);
                } else {
                    p.sendMessage(plugin.getLang().get("messages.noDestroyYour"));
                }
            }
        }
    }

    private void remove(Player p, PlayerMinion pm, PlayerData pd, MinionLevel ml) {
        MinionCollectEvent ec = new MinionCollectEvent(new HashMap<>(pm.getItems()), p);
        Bukkit.getPluginManager().callEvent(ec);
        Utils.addItems(p, ec.getItems(), pm.getMinionLevel().getMax());
        pm.setActions(0);
        Utils.addItems(p, ml.getMinionHead(pm));
        if (pm.getUpgrade().getFuel() != null) {
            UpgradeFuel uf = pm.getUpgrade().getFuel();
            if (uf.isUnlimited()) {
                Utils.addItems(p, uf.getResult(false));
            }
        }
        p.sendMessage(plugin.getLang().get("messages.removedMinion").replaceAll("<min>", String.valueOf(pd.getMinionSize())).replaceAll("<max>", String.valueOf(plugin.getAdm().getMaxMinion(p))));
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand) {
            Player p = e.getPlayer();
            ArmorStand stand = (ArmorStand) e.getRightClicked();
            if (lastRemove.containsKey(p.getUniqueId())) {
                long last = lastRemove.get(p.getUniqueId());
                if (last + 500 > System.currentTimeMillis()) {
                    e.setCancelled(true);
                    return;
                }
                lastRemove.remove(p.getUniqueId());
            }
            if (plugin.getMm().getMinionsToLoad().containsKey(stand.getUniqueId()) || plugin.getMm().getActiveMinions().containsKey(stand.getUniqueId())) {
                e.setCancelled(true);
            }
            PlayerMinion pm = plugin.getMm().getActiveMinions().get(stand.getUniqueId());
            if (pm == null) {
                return;
            }
            e.setCancelled(true);
            if (lastClick.containsKey(p.getUniqueId())) {
                long last = lastClick.get(p.getUniqueId());
                if (last + 500 > System.currentTimeMillis()) {
                    return;
                }
                lastClick.remove(p.getUniqueId());
            }
            lastClick.put(p.getUniqueId(), System.currentTimeMillis());
            boolean admin = plugin.getCfm().isAdminBypass() && (p.isOp() || p.hasPermission("ultraminions.admin"));
            boolean permited = (plugin.getAdm().isAddon()) ? (plugin.getAdm().checkMember(p) && plugin.getCfm().isOpenInventory()) || pm.getP().getUniqueId().equals(p.getUniqueId()) : pm.getP().getUniqueId().equals(p.getUniqueId());
            if (permited) {
                plugin.getMm().removeView(p);
                plugin.getMm().setView(p, pm);
                MathUtils mu = new MathUtils(pm, pm.getUpgrade().getCompressor(), pm.getUpgrade().getAutoSmelt(), pm.getUpgrade().getAutoSell());
                mu.fill();
                ItemStack item = p.getItemInHand();
                if (!item.getType().equals(Material.AIR)) {
                    if (plugin.getCfm().isFood() && pm.getStat().getFood() < pm.getMinionLevel().getFood()) {
                        Food f = plugin.getFm().getFoodByItem(item.clone());
                        if (f != null) {
                            pm.getStat().addFood(getFoodAmount(p, pm, f, item));
                            if (pm.getStat().getFood() > 10) {
                                plugin.getAdm().deleteHologram(pm);
                            }
                            p.sendMessage(plugin.getLang().get("messages.feed").replaceAll("<amount>", String.valueOf(pm.getStat().getFood())));
                            return;
                        }
                    }
                    if (plugin.getCfm().isHealth() && pm.getStat().getHealth() < pm.getMinionLevel().getHealth()) {
                        if (check(pm.getMinion().getPlace(), item.clone())) {
                            pm.getStat().addHealth(getHealthAmount(p, pm, item));
                            if (pm.getStat().getHealth() > 10) {
                                plugin.getAdm().deleteHologram(pm);
                            }
                            p.sendMessage(plugin.getLang().get("messages.health").replaceAll("<amount>", String.valueOf(pm.getStat().getHealth())));
                            return;
                        }
                    }
                }
                plugin.getMem().createMinionMenu(p, pm);
            } else {
                if (admin) {
                    plugin.getMem().createAdminMinionMenu(p, pm);
                    return;
                }
                p.sendMessage(plugin.getLang().get("messages.noYourMinion"));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            if (e.getClickedBlock().getType().name().endsWith("SHULKER_BOX")) {
                if (isInBlockRadius(e.getClickedBlock())) {
                    e.setCancelled(true);
                    return;
                }
            }
            ItemStack main = p.getInventory().getItemInMainHand();
            ItemStack off = p.getInventory().getItemInOffHand();
            boolean cMain = checkItemHand(p, main, e);
            boolean cOff = false;
            if (!cMain) {
                cOff = checkItemHand(p, off, e);
            }

            if (!cMain && !cOff) {
                return;
            }
            ItemStack item;
            if (cMain) {
                item = main;
            } else {
                item = off;
            }
            e.setCancelled(true);
            Block b = e.getClickedBlock();
            PlayerData pd = PlayerData.getPlayerData(p);

            if (pd == null) {
                return;
            }
            if (isInBlockRadius(b)) {
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.alreadyMinion"));
                return;
            }
            if (pd.getMinionSize() >= plugin.getAdm().getMaxMinion(p)) {
                p.sendMessage(plugin.getLang().get("messages.maxMinion"));
                return;
            }
            if (plugin.getAdm().isAddon() && !plugin.getAdm().checkMember(p)) {
                p.sendMessage(plugin.getLang().get("messages.noPutThis"));
                return;
            }
            if (plugin.getAdm().isProtect(p, b.getLocation())) {
                p.sendMessage(plugin.getLang().get("messages.isProtect"));
                return;
            }
            if (plugin.getCfm().isPreciousstones()) {
                if (!plugin.getAdm().getPca().checkRegion(p, b.getLocation())) {
                    p.sendMessage(plugin.getLang().get("messages.noPreciusStone"));

                    return;
                }
            }
            if (plugin.getCfm().isProtectionstones()) {
                if (!plugin.getAdm().getPrsa().checkRegion(p, b.getLocation())) {
                    p.sendMessage(plugin.getLang().get("messages.noProtectionStone"));
                   return;
                }
            }
            if (plugin.getCfm().isTowny()) {
                if (!plugin.getAdm().getTowny().isInYourTown(p)) {
                    p.sendMessage(plugin.getLang().get("messages.noYourTown"));
                    return;
                }
            }
            if (plugin.getCfm().isPlotsquared()) {
                if (!plugin.getAdm().isAllowedPlot(p, b.getLocation())) {
                    p.sendMessage(plugin.getLang().get("messages.onlyInPlot"));
                     return;
                }
            }
            String key = NBTEditor.getString(item, "KEY");

            if (plugin.getCfm().isPermissionToPlace()) {
                if (!plugin.getAdm().hasPermission(p, "ultraminions.place." + key)) {
                    p.sendMessage(plugin.getLang().get("messages.noPermissionToPlace"));
                     return;
                }
            }
            int max = plugin.getAdm().getMaxPerType(p, key);
            if (max != 0) {
                if (pd.getTypes().getOrDefault(key, 0) >= max) {
                    p.sendMessage(plugin.getLang().get("messages.noMoreThisType"));
                 return;
                }
            }
            pd.getTypes().put(key, pd.getTypes().getOrDefault(key, 0) + 1);
            int level = NBTEditor.getInt(item, "LEVEL");
            int generated = NBTEditor.getInt(item, "GENERATED");
            int food = NBTEditor.getInt(item, "FOOD");
            int health = NBTEditor.getInt(item, "HEALTH");
            int workTime = NBTEditor.getInt(item, "WORKTIME");
            int sleep = NBTEditor.getInt(item, "SLEEP");
            long fueltime = NBTEditor.getLong(item, "FUELTIME");


            PlayerMinion pm = new PlayerMinion(b.getLocation().clone().add(0.5, 1, 0.5), key, p);
            PlayerMinionUpgrade upgrade = new PlayerMinionUpgrade(pm);
            String autosell = NBTEditor.getString(item, "AUTOSELL");

            String skin = NBTEditor.contains(item, "SKIN") ? NBTEditor.getString(item, "SKIN") : "none";
            MinionManager.createMinion(item, level, generated, food, health, workTime, sleep, fueltime, pm, upgrade, autosell, plugin);
            pd.addPlayerMinion(pm);
            pm.firstSpawn();
            removeItemInHand(p, cMain);
            p.sendMessage(plugin.getLang().get("messages.placeMinion").replaceAll("<minion>", pm.getMinion().getTitle()).replaceAll("<min>", String.valueOf(pd.getMinionSize())).replaceAll("<max>", String.valueOf(plugin.getAdm().getMaxMinion(p))));
            pm.setSkin(skin);
            if (plugin.getCfm().isUnlockingTiers()) {
                if (pd.isNewUnlocked(key, level)) {
                    pd.setLevel(key, level);
                    Tier now = plugin.getTm().getTierByUnlocked(pd.getUnlocked());
                    Tier next = plugin.getTm().getTierByUnlocked(pd.getUnlocked() + 1);
                    pd.setUnlocked(pd.getUnlocked() + 1);
                    if (next != null) {
                        if (level == 1) {
                            Tier nt = plugin.getTm().getNextTier(next);
                            p.sendMessage(plugin.getLang().get("messages.newMinion").replaceAll("<title>", pm.getMinionLevel().getLevelTitle()).replaceAll("<tier>", String.valueOf(nt.getRequired() - pd.getUnlocked())));

                            return;
                        }
                        if (!now.equals(next)) {
                            p.sendMessage(plugin.getAdm().getTier(p).getMsg().replaceAll("<now>", String.valueOf(pd.getMinionSize())).replaceAll("<max>", String.valueOf(plugin.getAdm().getMaxMinion(p))));
                        } else {
                            Tier nt = plugin.getTm().getNextTier(next);
                            p.sendMessage(plugin.getLang().get("messages.newMinion").replaceAll("<title>", pm.getMinionLevel().getLevelTitle()).replaceAll("<tier>", String.valueOf(nt.getRequired() - pd.getUnlocked())));
                        }
                    }
                }
            }
        }
    }

    public boolean checkItemHand(Player p, ItemStack item, PlayerInteractEvent e) {
        if (item != null && !item.getType().equals(Material.AIR)) {
            if ((item.getType().equals(Material.LAVA_BUCKET) || item.getType().equals(Material.WATER_BUCKET) || item.getType().isBlock()) && (plugin.getUm().getFuel(false, item) != null || plugin.getUm().getAutoSell(item) != null || plugin.getUm().getCompressor(item) != null || plugin.getUm().getAutoSmelt(item) != null)) {
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.noPlaceUpgrades"));
                return false;
            }
            if (plugin.getSkm().getMinionSkinByName(item) != null) {
                e.setCancelled(true);
                p.sendMessage(plugin.getLang().get("messages.noPutSkin"));
                return false;
            }

            return NBTEditor.getString(item, "KEY") != null && !Objects.equals(NBTEditor.getString(item, "KEY"), "");
        }
        return false;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity().hasMetadata("MINION")) {
            if (e.getEntity().getKiller() == null) {
                e.getDrops().clear();
            }
        }
    }

    private void removeItemInHand(Player p, boolean main) {
        ItemStack item = (main) ? p.getInventory().getItemInMainHand() : p.getInventory().getItemInOffHand();
        if (item != null && !item.getType().equals(Material.AIR)) {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                if (main) {
                    p.getInventory().setItemInMainHand(null);
                } else {
                    p.getInventory().setItemInOffHand(null);
                }
            }
        }
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        if (e.getTo() == null) return;
        Chunk chunk = e.getTo().getChunk();
        loadChunk(chunk);
    }

    private void load(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        loadChunk(chunk);
    }

    public void loadChunk(Chunk chunk) {
        if (plugin.getMm().getToSpawn().size() > 0) {
            Collection<PlayerMinion> remove = new ArrayList<>();
            for (PlayerMinion pm : plugin.getMm().getToSpawn()) {
                int x = pm.getSpawn().getBlockX() >> 4;
                int z = pm.getSpawn().getBlockZ() >> 4;
                if (chunk.getX() == x && chunk.getZ() == z) {
                    pm.firstSpawn();
                    ArmorStand as = pm.getArmor();
                    if (as == null) continue;
                    UUID uuid = as.getUniqueId();
                    plugin.getMm().getActiveMinions().put(uuid, pm);
                    remove.add(pm);
                }
            }
            if (remove.size() > 0) {
                plugin.getMm().getToSpawn().removeAll(remove);
                remove.clear();
            }
        }
        if (plugin.getMm().getMinionsToLoad().size() > 0 || plugin.getMm().getMinionsToRemove().size() > 0) {
            Collection<UUID> removeLoad = new ArrayList<>();
            Entity[] entities = chunk.getEntities();
            for (Entity ent : entities) {
                if (!(ent instanceof ArmorStand)) continue;
                ArmorStand as = (ArmorStand) ent;
                UUID uuid = as.getUniqueId();
                if (plugin.getMm().getMinionsToRemove().contains(uuid)) {
                    as.remove();
                    continue;
                }
                if (Utils.isMinionUUID(uuid)) {
                    PlayerMinion pm = plugin.getMm().getMinionsToLoad().get(uuid);
                    if (pm == null) continue;
                    MinionLoadEvent load = new MinionLoadEvent(pm, chunk);
                    Bukkit.getPluginManager().callEvent(load);
                    if (load.isCancelled()) {
                        continue;
                    }
                    pm.setLoaded(true);
                    plugin.getMm().getActiveMinions().put(uuid, pm);
                    removeLoad.add(uuid);
                }
            }
            if (removeLoad.size() > 0) {
                removeLoad.forEach(u -> plugin.getMm().getMinionsToLoad().remove(u));
                removeLoad.clear();
            }
        }
    }

    private void unload(ChunkUnloadEvent e) {
        Chunk chunk = e.getChunk();
        if (plugin.getMm().getActiveMinions().size() > 0) {
            Collection<UUID> active = new ArrayList<>();
            Entity[] entities = chunk.getEntities();
            for (Entity ent : entities) {
                if (!(ent instanceof ArmorStand)) continue;
                ArmorStand as = (ArmorStand) ent;
                UUID uuid = as.getUniqueId();
                if (Utils.isMinionUUID(uuid)) {
                    PlayerMinion pm = plugin.getMm().getActiveMinions().get(uuid);
                    if (pm == null) continue;
                    MinionUnloadEvent unload = new MinionUnloadEvent(pm, chunk);
                    Bukkit.getPluginManager().callEvent(unload);
                    if (unload.isCancelled()) {
                        continue;
                    }
                    pm.setLoaded(false);
                    plugin.getAdm().deleteHologram(pm);
                    plugin.getMm().getMinionsToLoad().put(uuid, pm);
                    active.add(uuid);
                }
            }
            if (active.size() > 0) {
                active.forEach(u -> plugin.getMm().getActiveMinions().remove(u));
                active.clear();
            }
        }
    }

    private boolean isInBlockRadius(Block b) {
        Location l = b.getLocation().clone().add(0.5, 0, 0.5);
        Collection<Entity> entities = l.getWorld().getNearbyEntities(l, 2, 2, 2);
        boolean checked = false;
        for (Entity e : entities) {
            if (e instanceof ArmorStand) {
                if (!checked) {
                    checked = Utils.isMinionUUID(e.getUniqueId());
                }
            }
        }
        return checked;
    }

    private int getFoodAmount(Player p, PlayerMinion pm, Food f, ItemStack item) {
        int amount = item.getAmount();
        int actual = pm.getMinionLevel().getFood() - pm.getStat().getFood();
        int need = actual / f.getAmount();
        if (item.getAmount() > need) {
            item.setAmount(amount - Math.max(need, 1));
            return pm.getMinionLevel().getFood();
        } else {
            p.setItemInHand(null);
        }
        return amount * f.getAmount();
    }


    private int getHealthAmount(Player p, PlayerMinion pm, ItemStack item) {
        int amount = item.getAmount();
        int actual = pm.getMinionLevel().getHealth() - pm.getStat().getHealth();
        int need = actual / 5;
        if (item.getAmount() > need) {
            item.setAmount(amount - Math.max(need, 1));
            return pm.getMinionLevel().getHealth();
        } else {
            p.setItemInHand(null);
        }
        return amount * 5;
    }

    private boolean check(ItemStack i1, ItemStack i2) {
        ItemStack item1 = i1.clone();
        item1.setAmount(1);
        ItemStack item2 = i2.clone();
        item2.setAmount(1);
        return item1.equals(item2);
    }


}