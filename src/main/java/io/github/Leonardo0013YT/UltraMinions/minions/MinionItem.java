package io.github.Leonardo0013YT.UltraMinions.minions;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinionItem {

    private ArrayList<ItemStack> items;
    private ArrayList<ItemStack> cached = new ArrayList<>();
    private int actionsToStack = 1;

    public MinionItem(ArrayList<ItemStack> items) {
        this.items = items;
        for (ItemStack item : this.items) {
            if (item == null || item.getType().equals(Material.AIR)) {
                actionsToStack = 64;
                continue;
            }
            int a = 64 / ((item.getAmount() > 0) ? item.getAmount() : 1);
            if (actionsToStack < a) {
                actionsToStack = a;
            }
        }
        ArrayList<ItemStack> st = getStack(64);
        for (int i = 0; i < 100; i++) {
            cached.addAll(st);
        }
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public int getActionsToStack() {
        return (actionsToStack > 0) ? actionsToStack : 64;
    }

    public MinionSell getItems(PlayerMinion pm, int actions, int spaces, int compress, int max, MinionItem normal) {
        if (actions < compress) {
            return normal.getItems(pm, actions, spaces, max);
        }
        HashMap<ItemStack, Integer> items = new HashMap<>();
        ArrayList<ItemStack> sellItems = new ArrayList<>();
        List<ItemStack> st = getStack(max);
        int com = actions / compress;
        int nor = actions % compress;
        int sts = com / getActionsToStack();
        int sst = com % getActionsToStack();
        int sell = 0;
        int realized = 0;
        int chest = 0;
        if (sts > 0) {
            if (sts > spaces) {
                sell = sts - spaces;
                int posStacks = sts - sell;
                int a = 0;
                while (a < posStacks) {
                    for (ItemStack i : st) {
                        ItemStack it = i.clone();
                        it.setAmount(1);
                        items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                    }
                    a++;
                    realized += getActionsToStack();
                }
                int b = 0;
                while (b < sell) {
                    if (b >= 20) break;
                    if (pm.getChest() != null) {
                        if (!pm.getChest().isFull()) {
                            pm.getChest().addItem(st, getActionsToStack());
                            chest += getActionsToStack();
                        } else {
                            sellItems.addAll(st);
                        }
                    } else {
                        sellItems.addAll(st);
                    }
                    b++;
                }
            } else {
                int a = 0;
                while (a < sts) {
                    for (ItemStack i : st) {
                        ItemStack it = i.clone();
                        it.setAmount(1);
                        items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                    }
                    a++;
                    realized += getActionsToStack();
                }
            }
        }
        if (sst > 0) {
            if (sell > 0) {
                chest = getChest(pm, max, sellItems, sst, chest);
            } else {
                for (ItemStack i : getStack(sst, max)) {
                    ItemStack it = i.clone();
                    it.setAmount(1);
                    items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                }
            }
        }
        if (nor > 0) {
            if (sell > 0) {
                if (pm.getChest() != null) {
                    if (!pm.getChest().isFull()) {
                        pm.getChest().addItem(normal.getStack(nor, max), getActionsToStack());
                        chest += nor;
                    } else {
                        sellItems.addAll(normal.getStack(nor, max));
                    }
                } else {
                    sellItems.addAll(normal.getStack(nor, max));
                }
            } else {
                for (ItemStack i : normal.getStack(nor, max)) {
                    ItemStack it = i.clone();
                    it.setAmount(1);
                    items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                }
            }
        }
        return new MinionSell(items, sellItems, actions - realized, chest);
    }

    private int getChest(PlayerMinion pm, int max, ArrayList<ItemStack> sellItems, int sst, int chest) {
        if (pm.getChest() != null) {
            if (!pm.getChest().isFull()) {
                pm.getChest().addItem(getStack(sst, max), getActionsToStack());
                chest += sst;
            } else {
                sellItems.addAll(getStack(sst, max));
            }
        } else {
            sellItems.addAll(getStack(sst, max));
        }
        return chest;
    }

    public MinionSell getItems(PlayerMinion pm, int actions, int spaces, int max) {
        HashMap<ItemStack, Integer> items = new HashMap<>();
        ArrayList<ItemStack> sellItems = new ArrayList<>();
        int stacks = actions / getActionsToStack();
        int rest = actions % getActionsToStack();
        int realized = 0;
        int chest = 0;
        List<ItemStack> st = getStack(max);
        int sell = 0;
        int used = 0;
        if (stacks > 0) {
            if (stacks > spaces) {
                sell = stacks - spaces;
                int posStacks = stacks - sell;
                int a = 0;
                while (a < posStacks) {
                    for (ItemStack i : st) {
                        ItemStack it = i.clone();
                        it.setAmount(1);
                        items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                    }
                    a++;
                    realized += getActionsToStack();
                    used++;
                }
                int b = 0;
                while (b < sell) {
                    if (b >= 20) break;
                    if (pm.getChest() != null) {
                        if (!pm.getChest().isFull()) {
                            pm.getChest().addItem(st, getActionsToStack());
                            chest += getActionsToStack();
                            sell--;
                        } else {
                            sellItems.addAll(st);
                        }
                    } else {
                        sellItems.addAll(st);
                    }
                    b++;
                }
            } else {
                int a = 0;
                while (a < stacks) {
                    used++;
                    for (ItemStack i : st) {
                        ItemStack it = i.clone();
                        it.setAmount(1);
                        items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                    }
                    a++;
                    realized += getActionsToStack();
                }
            }
        }
        if (rest > 0) {
            if (used >= spaces || sell > 0) {
                chest = getChest(pm, max, sellItems, rest, chest);
            } else {
                for (ItemStack i : getStack(rest, max)) {
                    ItemStack it = i.clone();
                    it.setAmount(1);
                    items.put(it, items.getOrDefault(it, 0) + i.getAmount());
                }
                realized += rest;
            }
        }
        return new MinionSell(items, sellItems, actions - realized, chest);
    }

    public ArrayList<ItemStack> getStack(int amount, int max) {
        ArrayList<ItemStack> items = new ArrayList<>();
        if (amount <= 0) return items;
        for (ItemStack i : this.items) {
            if (i == null || i.getType().equals(Material.AIR)) continue;
            ItemStack item = i.clone();
            int a = i.getAmount();
            item.setAmount(Math.min(a * amount, Math.min(max, 64)));
            items.add(item);
        }
        return items;
    }

    public ArrayList<ItemStack> getStack(int max) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack i : this.items) {
            if (i == null || i.getType().equals(Material.AIR)) continue;
            ItemStack item = i.clone();
            item.setAmount(Math.min(max, 64));
            items.add(item);
        }
        return items;
    }

}