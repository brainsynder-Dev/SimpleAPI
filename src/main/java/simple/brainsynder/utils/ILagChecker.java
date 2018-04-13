package simple.brainsynder.utils;

public interface ILagChecker {

    int getTicksPerSecond();

    default long getLastAverage () {
        return getTicksPerSecond();
    }

    boolean isLagging();

}
