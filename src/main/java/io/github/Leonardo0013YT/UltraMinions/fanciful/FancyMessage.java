package io.github.Leonardo0013YT.UltraMinions.fanciful;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class FancyMessage {

    private Collection<TextComponent> text;
    private TextComponent now;

    public FancyMessage(String msg) {
        this.text = new ArrayList<>();
        this.now = new TextComponent(msg);
    }

    public FancyMessage addMsg(String msg) {
        text.add(now);
        this.now = new TextComponent(msg);
        return this;
    }

    public FancyMessage bold(boolean b) {
        now.setBold(b);
        return this;
    }

    public FancyMessage color(ChatColor color) {
        now.setColor(color);
        return this;
    }

    public FancyMessage setClick(ClickEvent.Action ca, String cmd) {
        now.setClickEvent(new ClickEvent(ca, cmd));
        return this;
    }

    public FancyMessage setHover(HoverEvent.Action ha, String msg) {
        now.setHoverEvent(new HoverEvent(ha, new ComponentBuilder(msg).create()));
        return this;
    }

    public void send(Player p) {
        text.add(now);
        TextComponent[] tc = new TextComponent[text.size()];
        int i = 0;
        for (TextComponent s : text) {
            tc[i] = s;
            i++;
        }
        p.spigot().sendMessage(tc);
    }

    public void send(CommandSender p) {
        TextComponent[] tc = (TextComponent[]) text.toArray();
        p.sendMessage(new TextComponent(tc).toString());
    }

}