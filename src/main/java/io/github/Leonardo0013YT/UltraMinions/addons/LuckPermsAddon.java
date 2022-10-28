package io.github.Leonardo0013YT.UltraMinions.addons;

import io.github.Leonardo0013YT.UltraMinions.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class LuckPermsAddon {

    private LuckPerms api;
    private Main plugin;

    public LuckPermsAddon(Main plugin) {
        this.plugin = plugin;
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
    }

    public boolean hasPermission(Player p, String perm) {
        User user = api.getUserManager().getUser(p.getUniqueId());
        if (user == null) {
            return false;
        }
        Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());
        Node node = Node.builder(perm).build();
        if (group != null) {
            AtomicBoolean inherited = new AtomicBoolean(false);
            group.getInheritedGroups(QueryOptions.builder(QueryMode.CONTEXTUAL).build()).forEach(bl -> {
                if (bl.getNodes().contains(node)) {
                    inherited.set(true);
                }
            });
            group.getInheritedGroups(QueryOptions.builder(QueryMode.NON_CONTEXTUAL).build()).forEach(bl -> {
                if (bl.getNodes().contains(node)) {
                    inherited.set(true);
                }
            });
            if (group.getNodes().contains(node)) {
                return true;
            }
            if (user.getNodes().contains(node)) {
                return true;
            }
            return inherited.get();
        }
        return user.getNodes().contains(node);
    }

    public int getMaxMinion(Player p) {
        User user = api.getUserManager().getUser(p.getUniqueId());
        if (user == null) {
            return plugin.getCfm().getDefaultMaxMinion();
        }
        Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());
        HashSet<Node> nodes = new HashSet<>(user.getNodes());
        if (group != null) {
            nodes.addAll(group.getNodes());
            group.getInheritedGroups(QueryOptions.builder(QueryMode.CONTEXTUAL).build()).forEach(bl -> nodes.addAll(bl.getNodes()));
            group.getInheritedGroups(QueryOptions.builder(QueryMode.NON_CONTEXTUAL).build()).forEach(bl -> nodes.addAll(bl.getNodes()));
        }
        for (Node node : nodes) {
            String perm = node.getKey();
            if (perm.startsWith("minions.max.")) {
                try {
                    return Integer.parseInt(perm.replaceFirst("minions.max.", ""));
                } catch (NumberFormatException e) {
                    return plugin.getCfm().getDefaultMaxMinion();
                }
            }
        }
        return plugin.getCfm().getDefaultMaxMinion();
    }

}