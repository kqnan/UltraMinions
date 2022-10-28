package io.github.Leonardo0013YT.UltraMinions.food;

import org.bukkit.inventory.ItemStack;

public class Food {

    private ItemStack food;
    private int amount;
    private String id;

    public Food(ItemStack food, int amount, String id) {
        this.food = food;
        this.amount = amount;
        this.id = id;
        if (food.getAmount() < 1) {
            food.setAmount(1);
        }
    }

    public String getId() {
        return id;
    }

    public ItemStack getFood() {
        return food;
    }

    public int getAmount() {
        return amount;
    }
}