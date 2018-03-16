package simple.brainsynder.utils;

import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.Core;

public class LagCheck extends BukkitRunnable implements ILagChecker {
    private long _lastRun = -1L;
	private int _count;
	private double _ticksPerSecond;
    private long _lastAverage;

	public LagCheck()
	{
		this._lastRun = System.currentTimeMillis();
		this._lastAverage = System.currentTimeMillis();
	}

	public static ILagChecker getInstance () {
	    return Core.getInstance().getLagCheck();
    }

	@Override
	public double getTicksPerSecond()
	{
		return _ticksPerSecond;
	}

    @Override
    public long getLastAverage () {
        return _lastAverage;
    }

    @Override
	public boolean isLagging()
	{
        return _ticksPerSecond <= 16D;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        _ticksPerSecond = 1000.0D / (now - this._lastRun) * 20.0D;
        if (this._count % 30 == 0)
        {
            this._lastAverage = now;
        }
        this._lastRun = now;
        this._count += 1;
    }
}