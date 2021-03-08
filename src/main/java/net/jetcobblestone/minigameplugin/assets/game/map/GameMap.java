package net.jetcobblestone.minigameplugin.assets.game.map;

import net.jetcobblestone.minigameplugin.VoidChunkGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameMap {

    private final String name;
    private final List<Location> spawnPoints;
    private final World world;

    public GameMap(String name, List<Location> spawnPoints) {
        this.name = name;
        this.spawnPoints = spawnPoints;
        World test = Bukkit.getWorld(name);
        if (test != null) {
            world = test;
        }
        else {
            WorldCreator worldCreator = new WorldCreator(name);
            worldCreator.generator(new VoidChunkGenerator());
            world = worldCreator.createWorld();
        }
        //noinspection ConstantConditions
        Bukkit.unloadWorld(world, false);
    }

    public String getName() {
        return name;
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public World getWorld(){
        return world;
    }

    public World getWorldCopy() throws IOException {
        final File worldDir = world.getWorldFolder();

        boolean check = false;
        int counter = 0;
        String newName = null;

        //find the next free name
        while (!check) {
            Bukkit.getLogger().info("" + counter);
            newName = world.getName() + "_temp" + counter;
            final World temp = Bukkit.getWorld(newName);

            if (temp == null) {
                check = true;
            }
            else {
                counter++;
            }

        }

        //Copies map
        final File newDir = new File(worldDir.getParent(), newName);
        FileUtils.copyDirectory(worldDir, newDir);

        //Deletes the uid.dat file
        //noinspection ConstantConditions
        for (File file : newDir.listFiles()) {
            if (file.getName().equals("uid.dat")) {
                if (!file.delete()) {
                    Bukkit.getLogger().warning("Could not delete uid.dat of " + newName);
                }
            }
        }

        //creates the copy
        final WorldCreator creator = new WorldCreator(newName);
        creator.generator(new VoidChunkGenerator());
        return Bukkit.createWorld(creator);
    }
}