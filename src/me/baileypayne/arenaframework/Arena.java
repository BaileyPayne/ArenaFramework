
package me.baileypayne.arenaframework;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author Bailey
 */
public class Arena {
    
    public int id = 0; //arena id
    public Location spawn; //arenas spawn
    List<String> players = new ArrayList<String>(); // list of players
    
    //Getters and Setters
    //sets id and spawn
    public Arena(Location loc, int id){
        this.spawn = loc;
        this.id = id;
    }
    //Gets arena Id
    public int getId(){
        return this.id;
    }
    //Gets Players in arena
    public List<String> getPlayers(){
        return this.players;
    }
}
