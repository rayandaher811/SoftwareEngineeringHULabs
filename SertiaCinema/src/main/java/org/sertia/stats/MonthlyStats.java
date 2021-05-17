package org.sertia.stats;

public class MonthlyStats {
    private static MonthlyStats monthlyStats = null;

    protected MonthlyStats(){

    }

    public static MonthlyStats getInstance(){
        if (monthlyStats == null){
            monthlyStats = new MonthlyStats();
        }

        return monthlyStats;
    }

    public AbstractStatistcs getStatisticsByCinemaName(String cinemaName){
        // TODO impl
        return null;
    }

    private void generateProfitStatsByCinema(String cinemaName){

    }

    private void generateMoneyReturnsStatsByCinema(String cinemaName){

    }

    private void generateComplainsStats(){

    }
}
