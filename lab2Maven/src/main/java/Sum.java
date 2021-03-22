import java.util.concurrent.TimeUnit;

public class Sum {
    /**
     * This program calculates the sum of 32 items, single threaded
     *
     * @param args
     */
    public static void main(String[] args) {
        long val = (long) Math.pow(2, 32);

        // Starting to measure time
        long startTime = System.nanoTime(); // Computation start time
        long sum = 0;

        // Computation
        for (long startIndex = 0; startIndex < val; startIndex++) {
            sum += 1;
        }

        // Getting end time timestamp and measure diff
        long difference = System.nanoTime() - startTime;

        long minutesInDifference = TimeUnit.NANOSECONDS.toMinutes(difference);
        long secondsInDifference =
                TimeUnit.NANOSECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(minutesInDifference);
        System.out.format("Total execution time: %d min, %d sec, %d nano-second\n",
                minutesInDifference,
                secondsInDifference,
                difference);

    }
}