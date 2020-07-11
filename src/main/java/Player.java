public class Player {
    private double kd;
    private double spm;
    private int top25;
    private int top5;
    private double timePlayed;
    private int wins;
    private int gamesPlayed;
    private String gamertag;
    private String platform;

    public Player(double kd, double spm, int top25, int top5, double timePlayed,
                  int wins, int gamesPlayed, String gamertag, String platform){
        this.kd = kd;
        this.spm = spm;
        this.top25 = top25;
        this.top5 = top5;
        this.timePlayed = timePlayed;
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
        this.gamertag = gamertag;
        this.platform = platform;
    }

    public double getKd() {
        return kd;
    }

    public void setKd(double kd) {
        this.kd = kd;
    }

    public double getSpm() {
        return spm;
    }

    public void setSpm(double spm) {
        this.spm = spm;
    }

    public int getTop25() {
        return top25;
    }

    public void setTop25(int top25) {
        this.top25 = top25;
    }

    public int getTop5() {
        return top5;
    }

    public void setTop5(int top5) {
        this.top5 = top5;
    }

    public double getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(double timePlayed) {
        this.timePlayed = timePlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    //TODO implement methods to return various percentages
//    public double getPercentageGamesWon(){
//
//    }
//
//    public double getPercentageTop25(){
//
//    }
//
//    public double getPercentageTop5(){
//
//    }
//
//    public double getKDPerPercentageTop25(){
//
//    }
//
//    public double getKDPerPercentageTop5(){
//
//    }
//
//    public double getSPMPerTimePlayed(){
//
//    }
//
// TODO add other fields as more API data is retrieved from last 20 games.....
//
//    public double getAverageTimeMovingLast20Percentage(){
//
//    }

}
