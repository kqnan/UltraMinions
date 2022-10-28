package io.github.Leonardo0013YT.UltraMinions.craft;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraMinions.minions.Minion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Craft implements Cloneable {

    private final HashMap<ItemStack, Integer> atLeast = new HashMap<>();
    private final ItemStack[] matrix;
    private final ItemStack result;
    private final String key, permission;
    private final int level;
    private final boolean minionCraft;
    private final boolean isCraft;
    private final String url;

    public Craft(ItemStack[] matrix, ItemStack result, String key, String permission, int level, boolean minionCraft, boolean isCraft, String url) {
        this.matrix = matrix;
        this.result = result;
        this.key = key;
        this.permission = permission;
        this.level = level;
        this.minionCraft = minionCraft;
        this.isCraft = isCraft;
        this.url = url;
        for (ItemStack at : matrix) {
            if (at == null || at.getType().equals(Material.AIR)) {
                continue;
            }
            ItemStack i = at.clone();
            int a = 0;
            for (ItemStack it : atLeast.keySet()) {
                if (i.isSimilar(it)) {
                    a += atLeast.get(it);
                }
            }
            a += i.getAmount();
            i.setAmount(1);
            atLeast.put(i, a);
        }
    }

    public String getPermission() {
        return permission;
    }

    public boolean isCraft() {
        return isCraft;
    }

    public boolean isMinionCraft() {
        return minionCraft;
    }

    public String getKey() {
        return key;
    }

    public int getLevel() {
        return level;
    }

    public HashMap<ItemStack, Integer> getAtLeast() {
        return atLeast;
    }

    public ItemStack[] getMatrix() {
        return matrix;
    }

    public ItemStack getResult() {
        if (minionCraft) {
            Minion m = Main.get().getMm().getMinion(key);
            return m.getMinionLevelByLevel(level).getMinionHead(url);
        }
        return result;
    }

    public boolean checkRequired(ItemStack[] input, CallBackAPI<HashMap<Integer, Integer>> now) {
        HashMap<Integer, Integer> values = new HashMap<>();
        boolean passed = true;
        for (int i = 0; i < input.length; i++) {
            ItemStack need = matrix[i];
            if (need == null) {
                need = matrix[i] = new ItemStack(Material.AIR);
            }
            ItemStack gived = input[i].clone();
            boolean na = need.getType().equals(Material.AIR);
            boolean gi = gived.getType().equals(Material.AIR);
            if (na) {
                if (!gi) {
                    passed = false;
                    break;
                }
                continue;
            }
            if (need.getAmount() > gived.getAmount()) {
                passed = false;
                break;
            }
            gived.setAmount(need.getAmount());
            if (!gived.equals(need)) {
                passed = false;
                break;
            }
            values.put(i, input[i].getAmount() - need.getAmount());
        }
        if (passed) {
            now.done(values);
        }
        return passed;
    }

    @Override
    public Craft clone() {
        return new Craft(matrix, result, key, permission, level, minionCraft, isCraft, url);
    }

}