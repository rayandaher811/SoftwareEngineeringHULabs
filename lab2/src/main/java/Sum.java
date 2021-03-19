import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Sum {
    /**
     * This program calculates the sum of 32 items, single threaded
     *
     * @param args
     */
    public static void main(String[] args) {
        long totalNanos = 0;
        int expectedRuns = 150;

        for (int runsCounter = 1; runsCounter <= expectedRuns; runsCounter++) {
            // Starting to measure time
            long startTime = System.nanoTime(); // Computation start time
            long sum = 0;

            // Computation
            for (long startIndex = 0; startIndex < 4_294_967_296L; startIndex++){
                sum += 1;
            }

            // Getting end time timestamp and measure diff
            long difference = System.nanoTime() - startTime;

            long minutesInDifference = TimeUnit.NANOSECONDS.toMinutes(difference);
            long secondsInDifference =
                    TimeUnit.NANOSECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(minutesInDifference);
            System.out.format("Total execution time: %d min, %d sec, %d nano-second %d sum\n",
                    minutesInDifference,
                    secondsInDifference,
                    difference,
                    sum);
            totalNanos += difference;
            System.out.println("Average: " + totalNanos / runsCounter);

        }
    }
}
