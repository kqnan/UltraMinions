package io.github.Leonardo0013YT.UltraMinions.setup;

import io.github.Leonardo0013YT.UltraMinions.craft.Craft;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetupCraft {

    private ItemStack[] matrix;
    private ItemStack result;
    private String permission;

    public SetupCraft() {
        permission = "none";
    }

    public SetupCraft(Craft c) {
        permission = c.getPermission();
        matrix = c.getMatrix();
        if (!c.getResult().getType().equals(Material.PLAYER_HEAD)) {
            result = c.getResult();
        }
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ItemStack[] getMatrix() {
        return matrix;
    }

    public void setMatrix(ItemStack[] matrix) {
        this.matrix = matrix;
    }
}