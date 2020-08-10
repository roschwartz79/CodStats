import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Main {
    public static FileHandler fileHandler = new FileHandler();
    public static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws Exception{

        while(true){
            System.out.println("\nPick an option:\n(0) Create a player data file\n(1) Read in an existing player data file\n(2) Add a new player\n(3) Compute and print scores" +
                    "\n(7) exit");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "0":
                    fileHandler.createFile();
                    break;
                case "1":
                    fileHandler.readCachedFile();
                    break;
                case "2":
                    System.out.printf("Enter the gamertag of the player you want to add: ");
                    String gamertagInput = scanner.nextLine();
                    System.out.printf("Enter the platform: Xbox (xbl) Playstation (psn) Activision name (uno) or Steam (steam): ");
                    String platformInput = scanner.nextLine();
                    fileHandler.addPlayerData(gamertagInput, platformInput);
                    break;
                case "3":
                    double[] scores = calculateScores();
                    printScores(scores);
                    break;
                case "7":
                    System.exit(0);
            }

        }
 }

    // calculate the new "score" for each player using normalized stats higher score -> better player
    // "kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"
    private static double[] calculateScores() throws FileNotFoundException {
        if (FileHandler.getPlayerList().size() == 0){
            System.out.println("Reading in the default data file...");
            fileHandler.readCachedFile();
        }
        ArrayList<Player> playerList = FileHandler.getPlayerList();

        double scores[] = new double[playerList.size()];
        // for each player
        for(int player = 0; player < playerList.size(); player++){
            // Get time and games played
            double timePlayed = playerList.get(player).getTimePlayed();
            double gamesPlayed = playerList.get(player).getGamesPlayed();


            System.out.println("\nDATA START FOR " + playerList.get(player).getGamertag());
            // kd ratio
            double kd = playerList.get(player).getKd() * 500;
            System.out.println("KD*500: " + kd);
            // score per minute
            double spm = playerList.get(player).getSpm() * 5;
            System.out.println("SPM*5: " + spm);

            // top 25 / time played
            double top25Time = playerList.get(player).getTop25()/timePlayed * 100000;
            System.out.println("top25/time * 100000: " + top25Time);

            // top 25 / gamesPlayed
            double top25Games = playerList.get(player).getTop25()/gamesPlayed * 100;
            System.out.println("top25/games * 100: " + top25Games);

            // top 5 / time played
            double top5Time = playerList.get(player).getTop5()/timePlayed * 500000;
            System.out.println("Top 5/time * 500000: " + top5Time);

            // top 5 / gamesPlayed * 10
            double top5Games = playerList.get(player).getTop5()/gamesPlayed * 500;
            System.out.println("Top 5/games * 500: " + top5Games);

            // wins / timePlayed * 10
            double winsTime = playerList.get(player).getWins()/timePlayed * 3000000;
            System.out.println("Wins/time * 3000000: "+winsTime);

            // wins / gamesPlayed * 20
            double winsGames = playerList.get(player).getWins()/gamesPlayed * 1000;
            System.out.println("wins/games * 500: " + winsGames);

            // kd / gamesPlayed * 2000
            double kdGames = playerList.get(player).getKd()/gamesPlayed * 200000;
            System.out.println("kd/games * 10000: " + kdGames);

            // headshot percentage * 10000
            double headshotPercentage = playerList.get(player).getHeadshotPercentage() * 2500;
            System.out.println("headshot percentage * 2500: " + headshotPercentage);

            // average lifetime
            double avgLife = playerList.get(player).getAvgLifetime();
            System.out.println("AvgLife: " + avgLife);

            // damage done / 50
            double damageDone = playerList.get(player).getDamageDone() / 50;
            System.out.println("damagedone/50: " + damageDone);

            // team wipes * 10
            double teamWipes = playerList.get(player).getObjectiveTeamWiped() * 10;
            System.out.println("team wipes*10: " + teamWipes);

            System.out.println("DATA END");
            // ADD UP THE SCORES
            scores[player] = kd + spm + top25Time + top25Games + top5Time
                    + top5Games + winsTime + winsGames + kdGames + headshotPercentage
                    + avgLife + damageDone;
        }

        return scores;
    }

    // Print out the data
    public static void printScores(double[] scores) throws PrinterException {
        String[] columnNames = {"Name", "Score"};
        Object[][] data = new Object[FileHandler.playerList.size()][2];
        for(int i = 0; i<FileHandler.playerList.size(); i++){
            data[i][0] = FileHandler.playerList.get(i).getGamertag();
            data[i][1] = scores[i];
        }

        JFrame jFrame=new JFrame();
        JTable jTable = new JTable(data, columnNames);
        jTable.setBounds(30,40,200,300);
        JScrollPane sp=new JScrollPane(jTable);
        jFrame.add(sp);
        jFrame.setSize(400,400);
        jFrame.setVisible(true);
    }
}
