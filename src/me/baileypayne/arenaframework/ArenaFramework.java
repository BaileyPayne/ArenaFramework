
package me.baileypayne.arenaframework;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bailey
 */
public class ArenaFramework extends JavaPlugin {
    
    @Override
    public void onEnable(){
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        if(getConfig() == null) {
            saveDefaultConfig();
        }
        ArenaManager arenaManager = new ArenaManager(this);
        ArenaManager.getManager().loadGames();
    }
    @Override
    public void onDisable(){
        saveConfig();        
    }
    public void registerListeners(){
        PluginManager pm = getServer().getPluginManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player p = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("create")){
            ArenaManager.getManager().createArena(p.getLocation());
            p.sendMessage("Created arena at " + p.getLocation().toString());

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("join")){
            if(args.length != 1){
                p.sendMessage("Insuffcient arguments!");
                return true;
            }
            int num = 0;
            try{
                num = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                p.sendMessage("Invalid arena ID");
            }
            ArenaManager.getManager().addPlayer(p, num);

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("leave")){
            ArenaManager.getManager().removePlayer(p);
            p.sendMessage("You have left the arena!");

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("remove")){
            if(args.length != 1){
                p.sendMessage("Insuffcient arguments!");
                return true;
            }
            int num = 0;
            try{
                num = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                p.sendMessage("Invalid arena ID");
            }
            ArenaManager.getManager().removeArena(num);

            return true;
        }

        return false;
    }
}
