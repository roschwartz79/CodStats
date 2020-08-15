public class Score {

    public static double getScore(Player player){
        // Get time and games played
        double timePlayed =player.getTimePlayed();
        double gamesPlayed =player.getGamesPlayed();


        System.out.println("\nDATA START FOR " +player.getGamertag());
        // kd ratio
        double kd =player.getKd() * 500.0;
        System.out.println("KD*500: " + kd);
        // score per minute
        double spm =player.getSpm() * 5.0;
        System.out.println("SPM*5: " + spm);

        // top 25 / time played
        double top25Time =player.getTop25()/timePlayed * 100000.0;
        System.out.println("top25/time * 100000: " + top25Time);

        // top 25 / gamesPlayed
        double top25Games =player.getTop25()/gamesPlayed * 100.0;
        System.out.println("top25/games * 100: " + top25Games);

        // top 5 / time played
        double top5Time =player.getTop5()/timePlayed * 500000.0;
        System.out.println("Top 5/time * 500000: " + top5Time);

        // top 5 / gamesPlayed * 10
        double top5Games =player.getTop5()/gamesPlayed * 500.0;
        System.out.println("Top 5/games * 500: " + top5Games);

        // wins / timePlayed * 10
        double winsTime =player.getWins()/timePlayed * 3000000.0;
        System.out.println("Wins/time * 3000000: "+winsTime);

        // wins / gamesPlayed * 20
        double winsGames =player.getWins()/gamesPlayed * 1000.0;
        System.out.println("wins/games * 500: " + winsGames);

        // kd / gamesPlayed * 2000
        double kdGames =player.getKd()/gamesPlayed * 200000.0;
        System.out.println("kd/games * 10000: " + kdGames);

        // headshot percentage * 10000
        double headshotPercentage =player.getHeadshotPercentage() * 2500.0;
        System.out.println("headshot percentage * 2500: " + headshotPercentage);

        // average lifetime
        double avgLife =player.getAvgLifetime();
        System.out.println("AvgLife: " + avgLife);

        // damage done / 50
        double damageDone =player.getDamageDone() / 50.0;
        System.out.println("damagedone/50: " + damageDone);

        // team wipes * 10
        double teamWipes =player.getObjectiveTeamWiped() * 10.0;
        System.out.println("team wipes*10: " + teamWipes);

        System.out.println("DATA END");
        // ADD UP THE SCORES
        double score = kd + spm + top25Time + top25Games + top5Time
                + top5Games + winsTime + winsGames + kdGames + headshotPercentage
                + avgLife + damageDone;


        return score;
    }
}
