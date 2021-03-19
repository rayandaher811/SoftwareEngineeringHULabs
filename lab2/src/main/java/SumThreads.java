import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SumThreads {

    private static class ComputationThread implements Runnable {
        private long startIndex;
        private long endIndex;
        private int sumInRange;

        public ComputationThread(long startIndex, long endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.sumInRange = 0;
        }

        @Override
        public void run() {
            while (startIndex < endIndex) {
                startIndex++;
                sumInRange++;
            }
        }

        public long getCalculatedValue() {
            return sumInRange;
        }
    }

    public static void main(String[] args) {
        long totalNanos = 0;
        int expectedRuns = 1;
        int numberOfThreads = 10;

        long value = 4294967296L;

        for (int runsCounter = 1; runsCounter <= expectedRuns; runsCounter++) {
            // Starting to measure time
            long startTime = System.nanoTime(); // Computation start time
            // Initialize thread pool
            Set<ComputationThread> computationObjects = new HashSet<>();
            Set<Thread> syncSet = new HashSet<>();

            long numberOfElementsForFirstThread = value % (numberOfThreads - 1);
            long numberOfElementsPerThread = ((value - numberOfElementsForFirstThread) / (numberOfThreads - 1)) - 1;
            computationObjects.add(new ComputationThread(0, numberOfElementsForFirstThread));

            // Initialize calculation objects
            for (int i = 1; i < numberOfThreads; i++) {
                computationObjects.add(new ComputationThread(numberOfElementsPerThread * i, numberOfElementsPerThread * (i + 1) + 1));
            }
            // create thread pool and initialize
            computationObjects.forEach(computationThread -> syncSet.add(new Thread(computationThread)));
            // start threads
            syncSet.forEach(Thread::start);

            // wait end of computation
            for (Thread thread1 : syncSet) {
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long calculatedResult = computationObjects.stream().mapToLong(ComputationThread::getCalculatedValue).sum();

            // Getting end time timestamp and measure diff
            long difference = System.nanoTime() - startTime;

            long minutesInDifference = TimeUnit.NANOSECONDS.toMinutes(difference);
            long secondsInDifference =
                    TimeUnit.NANOSECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(minutesInDifference);
            System.out.format("Total execution time: %d min, %d sec, %d nano-second %d sum\n",
                    minutesInDifference,
                    secondsInDifference,
                    difference,
                    calculatedResult);
            totalNanos += difference;
            System.out.println("Average: " + totalNanos / runsCounter);
        }
    }
}
