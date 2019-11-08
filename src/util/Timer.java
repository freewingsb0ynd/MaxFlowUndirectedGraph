package util;

public class Timer {
    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public double getTime() {
        return (double) (System.nanoTime() - startTime) / 1000;
    }

}
