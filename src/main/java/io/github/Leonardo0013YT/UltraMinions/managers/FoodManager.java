package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.food.Food;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FoodManager {

    private Main plugin;
    private HashMap<String, Food> foods = new HashMap<>();

    public FoodManager(Main plugin) {
        this.plugin = plugin;
        loadFoods();
    }

    public void loadFoods() {
        foods.clear();
        if (plugin.getFoods().isSet("foods")) {
            ConfigurationSection food = plugin.getFoods().getConfig().getConfigurationSection("foods");
            for (String s : food.getKeys(false)) {
                Food f = new Food(plugin.getFoods().getConfig().getItemStack("foods." + s + ".item"), plugin.getFoods().getInt("foods." + s + ".amount"), s);
                foods.put(f.getId(), f);
            }
        }
    }

    public Food getFoodByItem(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return null;
        for (Food f : foods.values()) {
            item.setAmount(f.getFood().getAmount());
            if (f.getFood() == null || f.getFood().getType().equals(Material.AIR)) continue;
            if (Utils.isSimilar(f.getFood(), item)) {
                return f;
            }
        }
        return null;
    }

    public Food getFoodByKey(String id) {
        return foods.get(id);
    }

    public HashMap<String, Food> getFoods() {
        return foods;
    }
}