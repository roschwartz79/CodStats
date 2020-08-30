import javax.swing.*;
import java.awt.print.PrinterException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    public static DBHandler DBHandler = new DBHandler();
    public static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws Exception{

        // Disable Mongo logging except for severe statements
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        while(true){
            System.out.println("\nPick an option:\n(1) Get an existing players data\n(2) Add a new player" +
                    "\n(3) Update a player in the DB\n(4) Update all players\n(5) Compute and print scores" +
                    "\n(7) exit");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    System.out.println("Enter the gamertag of the player you want to view: ");
                    String gamertagInput1 = scanner.nextLine();
                    DBHandler.getPlayerData(gamertagInput1);
                    break;
                case "2":
                    System.out.println("Enter the gamertag of the player you want to add: ");
                    String gamertagInput2 = scanner.nextLine();
                    System.out.println("Enter the platform: Xbox (xbl) Playstation (psn) Activision name (uno) or Steam (steam): ");
                    String platformInput2 = scanner.nextLine();
                    DBHandler.addPlayerData(gamertagInput2, platformInput2);
                    break;
                case "3":
                    System.out.println("Enter the gamertag of the player you want to update: ");
                    String gamertagInput3 = scanner.nextLine();
                    System.out.println("Enter the platform: Xbox (xbl) Playstation (psn) Activision name (uno) or Steam (steam): ");
                    String platformInput3 = scanner.nextLine();
                    DBHandler.updatePlayerData(gamertagInput3, platformInput3);
                    break;
                case "4":
                    DBHandler.updateAll();
                    break;
                case "5":
                    double[] scores = DBHandler.updateAndGetScores();
                    //printScores(scores);
                    break;
                case "7":
                    System.out.println("Exiting from mongodb....");
                    DBHandler.closeDBConn();
                    System.exit(0);
            }

        }
 }

    // Print out the data
    public static void printScores(double[] scores) throws PrinterException {
        String[] columnNames = {"Name", "Score"};
        Object[][] data = new Object[DBHandler.playerList.size()][2];
        for(int i = 0; i< DBHandler.playerList.size(); i++){
            data[i][0] = DBHandler.playerList.get(i).getGMTG();
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
