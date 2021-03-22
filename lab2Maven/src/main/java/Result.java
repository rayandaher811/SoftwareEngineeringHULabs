class Result {
    private int sum = 0;

    synchronized void addToResult(int number) {
        sum += number;
    }

    public int getSum() {
        return sum;
    }
}
