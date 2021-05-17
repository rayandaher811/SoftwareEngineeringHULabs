package org.sertia;

public class PurpleTavPolicy {
    private static PurpleTavPolicy policy;
    private boolean canShowMovies;
    private int maxAllowedHallCapacity;

    protected PurpleTavPolicy(){
        canShowMovies = true;
        maxAllowedHallCapacity = 5; // need to be set by demands, according to table
    }

    public static PurpleTavPolicy getInstance(){
        if (policy == null)
            policy = new PurpleTavPolicy();
        return policy;
    }

    public void updatePolicy(boolean canShowMovies, int maxAllowedHallCapacity){
        if (policy != null) {
            this.canShowMovies = canShowMovies;
            this.maxAllowedHallCapacity = maxAllowedHallCapacity;
        }
    }
}
