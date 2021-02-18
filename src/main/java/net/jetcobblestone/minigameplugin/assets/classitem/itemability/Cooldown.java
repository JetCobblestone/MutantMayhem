package net.jetcobblestone.minigameplugin.assets.classitem.itemability;

public class Cooldown {
	long cooldown;
	long creationTime;
	
	public Cooldown(long cooldown) {
		this.cooldown = cooldown;
		creationTime = System.currentTimeMillis();
	}
	
	public boolean isFinished() {
		final long difference = System.currentTimeMillis() - creationTime;
		return difference > cooldown;
	}
	
	public long getTime() {
		final long difference = System.currentTimeMillis() - creationTime;
		return difference;
	}
}
