public interface DBInterface {
    void updatePlayerData(String Gamertag, String Platform) throws Exception;
    void addPlayerData(String Gamertag, String Platform) throws Exception;
    void getPlayerData(String gamertag);
}
