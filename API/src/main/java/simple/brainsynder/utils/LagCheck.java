package simple.brainsynder.utils;

import org.bukkit.scheduler.BukkitRunnable;

public class LagCheck extends BukkitRunnable implements ILagChecker {
    private long sec, currentSec;
    private int ticks, tps = 0;

    public int getTicksPerSecond() {
        return tps;
    }

    public boolean isLagging() {
        return this.tps <= 16;
    }

    public void run() {
        sec = (System.currentTimeMillis() / 1000);
        if (currentSec == sec) {
            ticks++;
            return;
        }
        currentSec = sec;
        tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
        ticks = 0;
    }
}