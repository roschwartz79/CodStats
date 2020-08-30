public class Score {

    public static double getScore(Player player){
        // Get time and games played
        double timePlayed =player.getTP();
        double gamesPlayed =player.getGP();


        //System.out.println("\nDATA START FOR " +player.getGamertag());
        // kd ratio
        double kd =player.getKD() * 500.0;
        //System.out.println("KD*500: " + kd);

        // score per minute
        double spm =player.getSPM() * 5.0;
        //System.out.println("SPM*5: " + spm);

        // top 25 / time played
        double top25Time =player.getTP25()/timePlayed * 100000.0;
        //System.out.println("top25/time * 100000: " + top25Time);

        // top 25 / gamesPlayed
        double top25Games =player.getTP25()/gamesPlayed * 100.0;
        //System.out.println("top25/games * 100: " + top25Games);

        // top 5 / time played
        double top5Time =player.getTP5()/timePlayed * 500000.0;
        //System.out.println("Top 5/time * 500000: " + top5Time);

        // top 5 / gamesPlayed * 10
        double top5Games =player.getTP5()/gamesPlayed * 500.0;
        //System.out.println("Top 5/games * 500: " + top5Games);

        // wins / timePlayed * 10
        double winsTime =player.getWINS()/timePlayed * 3000000.0;
        //System.out.println("Wins/time * 3000000: "+winsTime);

        // wins / gamesPlayed * 20
        double winsGames =player.getWINS()/gamesPlayed * 1000.0;
        //System.out.println("wins/games * 500: " + winsGames);

        // headshot percentage * 10000
        double headshotPercentage =player.getHDSHTPCT() * 2500.0;
        //System.out.println("headshot percentage * 2500: " + headshotPercentage);

        // average lifetime
        double avgLife =player.getAVGLF();
        //System.out.println("AvgLife: " + avgLife);

        // damage done / 50
        double damageDone =player.getDMGD() / 50.0;
        //System.out.println("damagedone/50: " + damageDone);

        // team wipes * 10
        double teamWipes =player.getTMWP() * 10.0;
        //System.out.println("team wipes*10: " + teamWipes);

        //System.out.println("DATA END");
        // ADD UP THE SCORES
        double score = kd + spm + top25Time + top25Games + top5Time
                + top5Games + winsTime + winsGames + headshotPercentage
                + avgLife + damageDone;

        return score;
    }

}
