package simple.brainsynder.utils;

public interface ILagChecker {

    double getTicksPerSecond();

    long getLastAverage ();

    boolean isLagging();

}
