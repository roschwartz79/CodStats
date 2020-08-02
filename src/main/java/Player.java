public class Player {
    private double kd;
    private double spm;
    private int top25;
    private int top5;
    private double timePlayed;
    private int wins;
    private int gamesPlayed;
    private int downs;
    private int contracts;
    private int revives;
    private String gamertag;
    private String platform;
    private double killsLast20;
    private double killsPerGame;
    private double objectiveTeamWiped;
    private double avgLifetime;
    private double distanceTraveled;
    private double headshotPercentage;
    private int gulagKills;
    private double damageDone;
    private double damageTaken;
    private int objLastStandKill;

    public Player(double kd, double spm, int top25, int top5, double timePlayed,
                  int wins, int gamesPlayed, int downs,
                  int contacts, int revives, double killsLast20, double killsPerGame,
                  double objectiveTeamWiped, double avgLifetime, double distanceTraveled,
                  double headshotPercentage, int gulagKills, double damageDone,
                  double damageTaken, int objLastStandKill, String gamertag, String platform){
        this.kd = kd;
        this.spm = spm;
        this.top25 = top25;
        this.top5 = top5;
        this.timePlayed = timePlayed;
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
        this.gamertag = gamertag;
        this.platform = platform;
        this.downs = downs;
        this.contracts = contacts;
        this.revives = revives;
        this.killsLast20 = killsLast20;
        this.killsPerGame = killsPerGame;
        this.objectiveTeamWiped = objectiveTeamWiped;
        this.avgLifetime = avgLifetime;
        this.distanceTraveled = distanceTraveled;
        this.headshotPercentage = headshotPercentage;
        this.gulagKills = gulagKills;
        this.damageDone = damageDone;
        this.damageTaken = damageTaken;
        this.objLastStandKill = objLastStandKill;
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

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public int getContracts() {
        return contracts;
    }

    public void setContracts(int contracts) {
        this.contracts = contracts;
    }

    public int getRevives() {
        return revives;
    }

    public void setRevives(int revives) {
        this.revives = revives;
    }

    public double getKillsLast20() {
        return killsLast20;
    }

    public void setKillsLast20(double killsLast20) {
        this.killsLast20 = killsLast20;
    }

    public double getKillsPerGame() {
        return killsPerGame;
    }

    public void setKillsPerGame(double killsPerGame) {
        this.killsPerGame = killsPerGame;
    }

    public double getObjectiveTeamWiped() {
        return objectiveTeamWiped;
    }

    public void setObjectiveTeamWiped(double objectiveTeamWiped) {
        this.objectiveTeamWiped = objectiveTeamWiped;
    }

    public double getAvgLifetime() {
        return avgLifetime;
    }

    public void setAvgLifetime(double avgLifetime) {
        this.avgLifetime = avgLifetime;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public double getHeadshotPercentage() {
        return headshotPercentage;
    }

    public void setHeadshotPercentage(double headshotPercentage) {
        this.headshotPercentage = headshotPercentage;
    }

    public int getGulagKills() {
        return gulagKills;
    }

    public void setGulagKills(int gulagKills) {
        this.gulagKills = gulagKills;
    }

    public double getDamageDone() {
        return damageDone;
    }

    public void setDamageDone(double damageDone) {
        this.damageDone = damageDone;
    }

    public double getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    public int getObjLastStandKill() {
        return objLastStandKill;
    }

    public void setObjLastStandKill(int objLastStandKill) {
        this.objLastStandKill = objLastStandKill;
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
