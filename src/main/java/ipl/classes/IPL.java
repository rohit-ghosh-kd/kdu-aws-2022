package ipl.classes;

import ipl.IPLUtility;
import ipl.exceptionHandlers.IPLException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;

@Data
@NoArgsConstructor
public class IPL {
    private String tournamentName;
    private Integer year;
    private HashMap<String, Team> teams = new HashMap<>();
    private HashSet<Player> players = new HashSet<>();
    private List<Match> matchFixtures = new ArrayList<>();

    public IPL(String tournamentName, Integer year, HashMap<String, Team> teams, HashSet<Player> players, List<Match> matchFixtures) {
        this.tournamentName = tournamentName;
        this.year = year;
        this.teams = teams;
        this.players = players;
        this.matchFixtures = matchFixtures;
    }

    public IPL(String tournamentName, Integer year) {
        this.tournamentName = tournamentName;
        this.year = year;
    }

    public void printAllTeams(){
        System.out.println("Printing all teams of " + tournamentName + year.toString());
        for(Map.Entry<String, Team> team: teams.entrySet()){
            System.out.println(team.getValue().toString());
        }
    }

    public void  printAllPlayers(){
        System.out.println("Printing all players of " + tournamentName + year.toString());
        for (Player player: players){
            System.out.println(player);
        }
    }

    public void printAllMatchFixtures(){
        System.out.println("Printing all match fixtures of " + tournamentName + year.toString());
        for(Match match: matchFixtures){
            System.out.println(match);
        }
    }

    /**
     * Reads the ipl.classes.IPL data file into Java object.
     * @param fileFormat takes the file format from which data has to be inserted. for e.g. csc, txt, etc.
     * @param filePath takes the file path from which data has to be inserted.
     * @throws IPLException
     */
    public void readInputFile(String fileFormat, String filePath) throws IPLException {
        switch (fileFormat.toLowerCase()){
            case "csv":
                IPLUtility.readInputFileCsv(fileFormat, filePath, this.teams, this.players);
                break;
            default:
                throw new IPLException("Incompatible file format provided.");

        }
    }

    public void createMatchFixtures(){
        this.matchFixtures = IPLUtility.fillMatchFixtures(new ArrayList<>(teams.keySet()), teams, matchFixtures);
    }

    public void writeMatchFixtureToCsvFile(String fileName) throws FileNotFoundException {
        IPLUtility.writeMatchFixtureToCsvFile(fileName, matchFixtures);
    }
    public void getTopBowlersByTeamName(String teamName, Integer minWicketsTaken) throws IPLException {
        List<Player> topBowlersByTeamName = IPLUtility.getTopBowlersByTeamName(teamName, minWicketsTaken, teams);
        if(topBowlersByTeamName.size() > 0){
            System.out.println("Top bowlers of team " + teamName + " who took more than " + minWicketsTaken + " are :");
            for(Player player: topBowlersByTeamName){
                IPLUtility.displayPlayerProfile(player);
            }
        }
    }

    public void searchPlayerByName(String playerName){
        List<Player> searchedPlayersByName = IPLUtility.getPlayerByName(playerName, players);
        System.out.println("Players whose name contains " + playerName + " are: ");
        for(Player player: searchedPlayersByName){
            IPLUtility.displayPlayerProfile(player);
        }
    }

    public void getNTopPlayers(Integer N, Integer year){
        List<Player> topBatsmen = IPLUtility.getNTopBatsmen(N, players, this);
        for(Player player: topBatsmen){
            IPLUtility.displayPlayerProfile(player);
        }
        List<Player> topBowlers = IPLUtility.getTopNBowlers(N, players, this);
        for(Player player: topBowlers){
            IPLUtility.displayPlayerProfile(player);
        }
        List<Player> topAllRounders = IPLUtility.getTopNAllRounders(N, players, this);
        for(Player player: topAllRounders){
            IPLUtility.displayPlayerProfile(player);
        }
    }

    public void topNHighestScoringTeams(Integer N){
        List<Map.Entry<String, Double>> teamsHighestScore = IPLUtility.getHighestScoresForEachTeam(teams);
        System.out.println("The " + N.toString() + " highest scoring teams would be: ");
        System.out.println("Team : Predicted Score");
        for(Integer index=0; index<teamsHighestScore.size() && index<N; index++){
            Map.Entry<String, Double> team = teamsHighestScore.get(index);
            String teamName = team.getKey();
            Integer totalRuns = team.getValue().intValue();
            System.out.println(teamName + " : " + totalRuns.toString());
        }
    }

    public void getNextGenPlayersForEachTeam(Integer matchFactor){
        for(Map.Entry<String, Team> team: teams.entrySet()){
            List<Player> nextGenPlayersByTeam = IPLUtility.getNextGenPlayersByTeam(team.getValue(), matchFactor);
            System.out.println("Next generation players of " + team.getKey() + " are: ");
            for (Player player: nextGenPlayersByTeam){
                IPLUtility.displayPlayerProfile(player);
            }
        }
    }

    public void getHighestWicketTakerAndHighestRunScorerByTeamName(String teamName){
        IPLUtility.getHighestWicketTakerAndHighestRunScorerByTeamName(teamName, teams);
    }

    public static void menuDrivenProgram(IPL ipl) throws IOException, IPLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menu: ");
        System.out.println("1. Given the name of a team, return all the bowlers who have taken at least 40 wickets.");
        System.out.println("2. Search a player feature: The user will enter the player’s name or a part of his name to\n" +
                "his profile and if two or more players have the same name, display each profile.");
        System.out.println("3. Given a team display the details of the highest wicket-taker and highest run-scorer.");
        System.out.println("4. Fetch the top 3 batsmen, top 3 bowlers, and top 3 all-rounders of the season.");
        System.out.println("5. Hypothetically if each team plays their 11 best batsmen in a match which two teams\n" +
                "would score the highest and what would be their predicted score.");
        System.out.println("6. Find the next-gen of players for each team, i.e., the players who have performed well\n" +
                "enough given that they have played very few matches. (Have some qualifying criteria\n" +
                "based on matches played, runs, wickets).");

        while (IPLUtility.userChoice()){
            System.out.println("Enter your choice: ");
            String choice = bufferedReader.readLine();
            switch (choice){
                case "1":
                    System.out.print("Enter team name: ");
                    String teamName = bufferedReader.readLine();
                    ipl.getTopBowlersByTeamName(teamName, 40);
                    break;
                case "2":
                    System.out.print("Enter player name: ");
                    String playerName = bufferedReader.readLine();
                    ipl.searchPlayerByName(playerName);
                    break;
                case "3":
                    System.out.println("Enter team: ");
                    teamName = bufferedReader.readLine();
                    ipl.getHighestWicketTakerAndHighestRunScorerByTeamName(teamName);
                    break;
                case "4":
                    ipl.getNTopPlayers(3, ipl.getYear());
                    break;
                case "5":
                    ipl.topNHighestScoringTeams(2);
                    break;
                case "6":
                    ipl.getNextGenPlayersForEachTeam(20);
                    break;
                default:
                    System.out.println("Invalid input choice given.");
            }
        }
    }

    public void checkIfFixturesAreProper(){
        System.out.println("Are fixtures proper: " + IPLUtility.checkIfFixturesAreProper(this.matchFixtures));
    }
}
