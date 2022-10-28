package io.github.Leonardo0013YT.UltraMinions.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sets/Gets NBT tags from ItemStacks
 * Supports 1.8-1.16
 * <p>
 * Github: https://github.com/BananaPuncher714/NBTEditor
 * Spigot: https://www.spigotmc.org/threads/269621/
 *
 * @author BananaPuncher714
 * @version 7.17.0
 */
public final class NBTEditor {
    public static ItemStack getHead(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if(url.isEmpty())return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);

        ItemMeta meta = head.getItemMeta();
        assert meta != null;

        head.setItemMeta(meta);
        return head;
    }



    public static String getString(Object object, Object... keys) {
        String key="";
        for(Object k:keys){
            key+=k+"_";
        }
        NBTItem nbtItem=new NBTItem((ItemStack) object);
        return nbtItem.getString(key);
    }


    public static int getInt(Object object, Object... keys) {
        String key="";
        for(Object k:keys){
            key+=k+"_";
        }
        NBTItem nbtItem=new NBTItem((ItemStack) object);

        return Integer.parseInt(nbtItem.getString(key));
    }

    public static long getLong(Object object, Object... keys) {
        String key="";
        for(Object k: keys){
            key+=k+"_";
        }
        NBTItem nbtItem=new NBTItem((ItemStack) object);
        return Long.parseLong(nbtItem.getString(key));
    }


    public static boolean contains(Object object, Object... keys) {
        String key="";
        for(Object k: keys){
            key+=k+"_";
        }
        NBTItem nbtItem=new NBTItem((ItemStack) object);
       return nbtItem.hasKey(key);

    }

    /**
     * Get the keys at the specific location, if it is a compound.
     *
     * @param object Must be an ItemStack, Entity, Block, or NBTCompound
     * @param keys   Keys in descending order
     * @return A set of keys
     */


    /**
     * Gets the size of the list or NBTCompound at the given location.
     *
     * @param object Must be an ItemStack, Entity, Block, or NBTCompound
     * @param keys   Keys in descending order
     * @return The size of the list or compound at the given location.
     */

    /**
     * Sets the value in the object with the given keys
     *
     * @param <T>    ItemStack, Entity, Block, or NBTCompound.
     * @param object Must be an ItemStack, Entity, Block, or NBTCompound
     * @param value  The value to set, can be an NBTCompound
     * @param keys   The keys in descending order
     * @return The new item stack if the object provided is an item, else original object
     */
    public static <T> T set(T object, Object value, Object... keys) {
        if (object instanceof ItemStack) {
            ItemStack itemStack=(ItemStack) object;

            String key="";
            for(Object k:keys){
                key+=k+"_";
            }
            NBTItem nbtItem=new NBTItem(itemStack);
            nbtItem.setObject(key,value);
            return (T) nbtItem.getItem();
        }
        return object;
    }







}
