
package me.baileypayne.arenaframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bailey
 */
public class ArenaManager {
    
    //save where player tp'ed
    public Map<String, Location> locs = new HashMap<String, Location>();
    //make a new instance
    public static ArenaManager am = new ArenaManager();
    //kit fiels
    Map<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
    Map<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();
    //list of arenas
    List<Arena> arenas = new ArrayList<Arena>();
    int arenaSize = 0;
    
    static ArenaFramework plugin;
    public ArenaManager(ArenaFramework arenaFramework){
        plugin = arenaFramework;
    }
    public ArenaManager(){
        
    }
    public static ArenaManager getManager(){
        return am;
    }
    //get an arena object
    public Arena getArena(int i){
        for(Arena a : arenas){
            if(a.getId() == i){
                return a;
            }
        }
        return null;
    }
    //add players
    public void addPlayer(Player p, int i){
        Arena a = getArena(i); //gets the arena
        if(a == null){ //checking it exists
            p.sendMessage(ChatColor.RED + "Invalid Arena!");
            return;
        }
        a.getPlayers().add(p.getName()); //adds them to arena list
        inv.put(p.getName(), p.getInventory().getContents()); //saves inv
        armor.put(p.getName(), p.getInventory().getArmorContents()); //saves armor
        
        //clears inventory
        p.getInventory().setArmorContents(null);
        p.getInventory().clear();
        
        //tp them to arena
        locs.put(p.getName(), p.getLocation());
        p.teleport(a.spawn);
    }
    //remove players
    public void removePlayer(Player p){
        Arena a = null; 
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(p.getName())){
                a = arena;
            }
        }
        if(a ==  null || !a.getPlayers().contains(p.getName())){
            p.sendMessage(ChatColor.RED + "Invalid Operation!");
            return;
        }
        a.getPlayers().remove(p.getName()); //removes from arena
        
        //clear inv
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        
        //restore inv
        p.getInventory().setContents(inv.get(p.getName()));
        p.getInventory().setArmorContents(armor.get(p.getName()));
        
        //remove from hashmap
        inv.remove(p.getName());
        armor.remove(p.getName());
        p.teleport(locs.get(p.getName()));
        locs.remove(p.getName());
        
        p.setFireTicks(0);
    }
    //create arena
    public Arena createArena(Location l){
        
        int num = arenaSize + 1;
        arenaSize++;
        
        Arena a = new Arena(l, num);
        arenas.add(a);
        
        plugin.getConfig().set("Arenas." + num, serializeLoc(l));
        List<Integer> list = plugin.getConfig().getIntegerList("Arenas.Arenas");
        list.add(num);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();
        
        return a;
        
    }
    //reload arenas
    public Arena reloadArena(Location l) {
        int num = arenaSize + 1;
        arenaSize++;
 
        Arena a = new Arena(l, num);
        arenas.add(a);
 
        return a;
    }
    //remove arenas
    public void removeArena(int i) {
        Arena a = getArena(i);
        if(a == null) {
            return;
        }
        arenas.remove(a);

        plugin.getConfig().set("Arenas." + i, null);
        List<Integer> list = plugin.getConfig().getIntegerList("Arenas.Arenas");
        list.remove(i);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();    
    }
    //checks if in game
    public boolean isInGame(Player p){
        for(Arena a : arenas){
            if(a.getPlayers().contains(p.getName()))
                return true;
        }
        return false;
    }

    //loads arenas
    public void loadGames(){
        arenaSize = 0;      

        if(plugin.getConfig().getIntegerList("Arenas.Arenas").isEmpty()){
            return;
        }
                
        for(int i : plugin.getConfig().getIntegerList("Arenas.Arenas")){
            Arena a = reloadArena(deserializeLoc(plugin.getConfig().getString("Arenas." + i)));
            a.id = i;
        }
    }

    public String serializeLoc(Location l){
        return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
    }
    public Location deserializeLoc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
    }
}
