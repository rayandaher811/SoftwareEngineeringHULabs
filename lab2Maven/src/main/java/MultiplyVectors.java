import java.util.Arrays;
import java.util.Random;

public class MultiplyVectors {
    private static final Random RANDOM = new Random();
    private static final int VECTOR_DIGIT_UPPER_BOUND = 10;

    public static void main(String[] args) {
        try {
            validateArgs(args);
            int vectorSize = extractVectorSize(args);
            int threadCount = extractThreadCount(args);
            if (threadCount > vectorSize) {
                System.out.println("more threads than needed, using " + vectorSize + " threads instead");
                threadCount = vectorSize;
            }

            runCalculation(vectorSize, threadCount);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command, example:");
            System.out.println("java MultiplyVectors 10 5");
            System.exit(-1);
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
    }

    private static int extractVectorSize(String[] args) {
        int vectorSize = Integer.parseInt(args[0]);

        if (vectorSize < 0) {
            System.out.println("Invalid vector size " + args[0]);
            throw new IllegalArgumentException();
        }

        return vectorSize;
    }

    private static int extractThreadCount(String[] args) {
        int threadCount = Integer.parseInt(args[1]);

        if (threadCount <= 0) {
            System.out.println("Invalid thread count " + args[1]);
            throw new IllegalArgumentException();
        }

        return threadCount;
    }

    private static void runCalculation(int vectorSize, int threadCount) {
        int[] firstVector = createRandomVector(vectorSize);
        int[] secondVector = createRandomVector(vectorSize);
        Result result = new Result();

        System.out.println("Calculating Dot Product of:");
        System.out.println(Arrays.toString(firstVector));
        System.out.println(Arrays.toString(secondVector));
        VectorsMultiplier[] multipliers = createMultipliers(firstVector, secondVector, threadCount, result);

        long s = System.currentTimeMillis();
        for (VectorsMultiplier multiplier : multipliers) {
            multiplier.start();
        }

        for (VectorsMultiplier multiplier : multipliers) {
            try {
                multiplier.join();
            } catch (InterruptedException e) {
                System.out.println("Encountered an error while calculating");
            }
        }

        System.out.println("Product: " + result.getSum());
    }

    private static int[] createRandomVector(int vectorSize) {
        int[] vector = new int[vectorSize];

        for (int i = 0; i < vectorSize; i++) {
            vector[i] = RANDOM.nextInt(VECTOR_DIGIT_UPPER_BOUND);
        }

        return vector;
    }

    private static VectorsMultiplier[] createMultipliers(int[] firstVector,
                                                         int[] secondVector,
                                                         int threadCount,
                                                         Result result) {
        int calculationRangeLength = Math.floorDiv(firstVector.length, threadCount);
        VectorsMultiplier[] multipliers = new VectorsMultiplier[threadCount];

        int currentStartIndex = 0;
        for (int i = 0; i < threadCount; i++) {
            if (i == threadCount - 1) {
                calculationRangeLength = firstVector.length - currentStartIndex;
            }

            multipliers[i] = new VectorsMultiplier(
                    firstVector,
                    secondVector,
                    currentStartIndex,
                    calculationRangeLength,
                    result);

            currentStartIndex += calculationRangeLength;
        }

        return multipliers;
    }
}
