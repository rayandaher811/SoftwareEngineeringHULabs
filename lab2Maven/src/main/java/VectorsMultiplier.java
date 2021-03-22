class VectorsMultiplier extends Thread {
    private final Result result;
    private final int startIndex;
    private final int range;
    private final int[] firstVector;
    private final int[] secondVector;

    VectorsMultiplier(int[] firstVector,
                      int[] secondVector,
                      int startIndex,
                      int range,
                      Result result) {
        this.firstVector = firstVector;
        this.secondVector = secondVector;
        this.result = result;
        this.startIndex = startIndex;
        this.range = range;
    }

    @Override
    public void run() {
        int product = 0;
        for (int i = startIndex; i < startIndex + range; i++) {
            product += firstVector[i] * secondVector[i];
        }

        result.addToResult(product);
    }
}
