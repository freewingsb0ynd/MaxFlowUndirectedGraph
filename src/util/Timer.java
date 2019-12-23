package util;

public class Timer {
    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public double getTimeInMilliseconds() {
        return (double) (System.nanoTime() - startTime) / 1000000;
    }

}
