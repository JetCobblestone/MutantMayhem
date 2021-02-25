package net.jetcobblestone.minigameplugin.assets.game.map;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class MapManager {

    public MapManager(MinigamePlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<Class<? extends Game>, List<GameMap>> gameMapMap = new HashMap<>();

    public List<GameMap> getMaps(Class<? extends Game> gameClass) {
        return gameMapMap.get(gameClass);
    }



    private final Logger logger = Bukkit.getLogger();
    private final MinigamePlugin plugin;

    private Class<? extends Game> gameClass = null;
    private String name = null;
    private final List<Location> spawnPoints = new ArrayList<>();
    private final List<GameMap> maps = new ArrayList<>();


    public boolean readMaps() {

        //Makes sure directory exists
        File dataFolder = plugin.getDataFolder();

        if (!dataFolder.exists()) {
            if (!plugin.getDataFolder().mkdir()) {
                logger.severe("Failed to create plugin directory");
                return false;
            }
        }


        //Makes sure file exists
        File mapFile = new File(plugin.getDataFolder(), "maps.txt");

        if (!mapFile.exists()) {
            try {
                if (!mapFile.createNewFile()) {
                    logger.severe("Failed to create maps file");
                }
            } catch (IOException e) {
                logger.severe("Error in creating maps.txt");
            }
            return false;
        }

        //Makes sure that the file isn't a directory
        if (mapFile.isDirectory()) {
            if (mapFile.delete()) {
                readMaps();
            }
            else {
                logger.severe("Please delete the directory 'JetsMinigames/maps.txt' located in your plugin's folder");
                return false;
            }
        }

        //Makes sure the file can be read
        if (!mapFile.canRead()) {
            logger.severe("map.txt could not be read");
            return false;
        }

        //Makes sure the file could be found
        final Scanner fileReader;
        try {
            fileReader = new Scanner(mapFile);
        } catch (FileNotFoundException e) {
            logger.severe("map.txt couldn't be found");
            return false;
        }

        //File reading

        while (fileReader.hasNext()) {

            String line = fileReader.nextLine();
            final Class<?> cache;

            //Reading the class
            if (line.startsWith("class: ")) {

                //Makes sure that this isn't the first time
                if (gameClass != null) {
                    if (!mapGame()) return false;
                }

                line = line.substring(7);

                //Makes sure class is real
                try {
                    cache = Class.forName(line);

                    } catch (ClassNotFoundException e) {
                        logger.severe("The class '" + line +"' is not a real class");
                        return false;
                    }

                //Makes sure entered class extends the Game class
                if (!Game.class.isAssignableFrom(cache)) {
                    logger.severe("The class '" + line + "' is not a game class");
                    return false;
                }

                //Finally the entered value is verified and gameClass is set to the entered value
                //noinspection unchecked
                gameClass = (Class<? extends Game>) cache;
                }
            else if (gameClass == null) {
                logger.severe("Name or co-ordinate entered with no class");
                return false;
            }

            if (line.startsWith("name: ")) {

                line = line.substring(6);

                //Means it's not the first time so previous map info must be compiled
                if (name != null) {
                    if (!makeGameMap()){
                        return false;
                    }
                }

                name = line;

            }

            else if (line.startsWith("spawnpoint: ")) {
                line = line.substring(12);
                String[] coordinatesStrings = line.split(", ");
                List<Double> coordinates = new ArrayList<>();

                if (name == null) {
                    logger.severe("The class '" + gameClass +"' has a spawnpoint entered before a name");
                    return false;
                }

                //Makes sure a number has been entered
                for (String coordinateString : coordinatesStrings) {
                    double coordinate;
                    try {
                        coordinate = Double.parseDouble(coordinateString);
                    }catch (NumberFormatException e) {
                        logger.severe("Coordinate for the map " + name + " is not a valid value");
                        return false;
                    }
                    coordinates.add(coordinate);
                }
                spawnPoints.add(new Location(Bukkit.getWorlds().get(0), coordinates.get(0), coordinates.get(1), coordinates.get(2)));
            }

        }
        if (gameClass != null) {
            if (!makeGameMap()){
                return false;
            }
            return mapGame();
        }
        else {
            logger.warning("maps.txt is empty");
            return true;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean makeGameMap() {
        if (spawnPoints.isEmpty()) {
            logger.severe("Map " + name + " has no spawnpoints");
            return false;
        }

        //adds the map and clears the spawnpoint list
        maps.add(new GameMap(name, new ArrayList<>(spawnPoints)));
        spawnPoints.clear();
        return true;
    }

    private boolean mapGame() {
        if (maps.isEmpty()) {
            logger.severe("The class " + gameClass.getName() + " has no maps");
            return false;
        }
        gameMapMap.put(gameClass, new ArrayList<>(maps));
        maps.clear();
        return true;
    }

}
