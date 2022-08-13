package ipl;

import ipl.classes.*;
import ipl.comparators.AllRounderComparator;
import ipl.comparators.BatsmanComparator;
import ipl.comparators.BowlerComparator;
import ipl.comparators.PlayerRunsPerInningsComparator;
import ipl.exceptionHandlers.IPLException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for various utility methods related to IPL class.
 */
public class IPLUtility {

    /**
     * Minimum number of matches a player must play for any prediction to be done on him.
     */
    private static final Integer matchFactor = 20;
    private static BufferedReader bufferedReader;

    /**
     * Utility method to ask the user whether he/she wants to continue with the flow or not.
     *
     * @return A boolean indicating whether the user wants to continue.
     * @throws IOException
     */
    public static boolean userChoice() throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you wish to continue? If yes, press 'y' or anything else to quit.");
        String choice = bufferedReader.readLine();
        return choice.toLowerCase().equals("y");
    }

    /**
     * @param playerRoleString String denoting the player role. (Eg. BATSMAN, BOWLER, ALL_ROUNDER, WICKET_KEEPER)
     * @return An object of enum PlayerRole
     */
    private static PlayerRole constructNewPlayerRoleObject(String playerRoleString) {
        PlayerRole playerRole = null;
        switch (playerRoleString) {
            case "BATSMAN":
                playerRole = PlayerRole.BATSMAN;
                break;
            case "BOWLER":
                playerRole = PlayerRole.BOWLER;
                break;
            case "ALL ROUNDER":
                playerRole = PlayerRole.ALL_ROUNDER;
                break;
            case "WICKET KEEPER":
                playerRole = PlayerRole.WICKET_KEEPER;
                break;
        }
        return playerRole;
    }

    /**
     * @param teams          HashMap containing all the teams mapping team name to the team.
     * @param teamName       name of the team
     * @param teamHomeGround name of the team home ground
     * @return Team object of teamName from the HashMap teams
     */
    private static Team constructNewPlayerTeamObject(HashMap<String, Team> teams, String teamName, String teamHomeGround) {
        Team playerTeam;
        if (teams.containsKey(teamName) == false) {
            playerTeam = new Team(teamName, teamHomeGround);
        } else {
            playerTeam = teams.get(teamName);
        }
        return playerTeam;
    }

    /**
     * @param playerName    name of a player
     * @param playerTeam    team of a player
     * @param playerRole    role of a player in the team
     * @param matchesPlayed total matches played by a player
     * @param runsScored    total runs scored played by a player
     * @param average       average of a player
     * @param strikeRate    strike rate of a player
     * @param wicketsTaken  total wickets taken by a player
     * @return Player object constructed with the parameter values passed
     */
    private static Player constructNewPlayerObject(String playerName, Team playerTeam, PlayerRole playerRole,
                                                   Integer matchesPlayed, Integer runsScored, Double average,
                                                   Double strikeRate, Integer wicketsTaken) {
        return new Player(playerName, playerTeam, playerRole, matchesPlayed, runsScored, average, strikeRate, wicketsTaken);
    }

    /**
     * Utility function to read data from an input file into IPL object.
     *
     * @param fileFormat specifying the file format from which data has to be read.
     * @param filePath   specifying the file path from which data has to be read.
     * @param teams      denoting the hash-map into which all the unique team objects will be inserted.
     * @param players    denoting the hash-set into which all the unique player objects will be inserted.
     * @throws IPLException
     */
    public static void readInputFileCsv(String fileFormat, String filePath, HashMap<String, Team> teams,
                                        HashSet<Player> players) throws IPLException {
        File file = new File(filePath);
        try {
            FileReader fileReader = new FileReader(file);
            final String delimiter = ",";
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            for (Integer lineNumber = 0; (line = bufferedReader.readLine()) != null; lineNumber++) {
                if (lineNumber != 0) {
                    try {
                        String lineArray[] = line.split(delimiter);
                        String playerName = lineArray[0];
                        String teamName = lineArray[1].toUpperCase();
                        PlayerRole playerRole = constructNewPlayerRoleObject(lineArray[2]);
                        Integer matchesPlayed = Integer.parseInt(lineArray[3]);
                        Integer runsScored = Integer.parseInt(lineArray[4]);
                        Double average = Double.parseDouble(lineArray[5]);
                        Double strikeRate = Double.parseDouble(lineArray[6]);
                        Integer wicketsTaken = Integer.parseInt(lineArray[7]);
                        String teamHomeGround = teamName + " home ground";
                        Team playerTeam = constructNewPlayerTeamObject(teams, teamName, teamHomeGround);
                        Player player = constructNewPlayerObject(playerName, playerTeam, playerRole,
                                matchesPlayed, runsScored, average, strikeRate, wicketsTaken);
                        players.add(player);
                        playerTeam.getPlayers().add(player);
                        teams.put(teamName, playerTeam);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        throw new IPLException("Fix line " + (lineNumber + 1));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new IPLException("ipl.classes.IPL data file in csv format is not found.");
        } catch (IOException e) {
            throw new IPLException("Error in the ipl.classes.IPL data file.");
        }
    }

    /**
     * @param teamNames     list containing all the unique team names.
     * @param teams         HashMap containing all the teams mapping team name to the team.
     * @param matchFixtures List of match objects into which match fixtures will be populated
     * @return List of matches containing suitable and appropriate match fixtures.
     */
    public static List<Match> fillMatchFixtures(List<String> teamNames, HashMap<String, Team> teams, List<Match> matchFixtures) {
        for (int indexHomeTeam = 0; indexHomeTeam < teamNames.size(); indexHomeTeam++) {
            for (int indexAwayTeam = 0; indexAwayTeam < teamNames.size(); indexAwayTeam++) {
                if (indexAwayTeam != indexHomeTeam) {
                    Team homeTeam = teams.get(teamNames.get(indexHomeTeam));
                    Team awayTeam = teams.get(teamNames.get(indexAwayTeam));
                    String playingGround = homeTeam.getHomeGround();
                    matchFixtures.add(new Match(homeTeam, awayTeam, playingGround));
                }
            }
        }
        // Normal shuffling so that each time a match fixture is generated.
        Collections.shuffle(matchFixtures);
        // Shuffling the fixtures such that no team plays two consecutive matches.
        List<Match> shuffledMatchFixtures = shuffleFixtures(matchFixtures);
        for (Integer index = 0; index < shuffledMatchFixtures.size(); index++) {
            shuffledMatchFixtures.get(index).setMatchNumber(index + 1);
        }
        return shuffledMatchFixtures;
    }

    /**
     * @param fileName      name of the file into which match fixtures are to be written
     * @param matchFixtures list of matches containing the match fixtures
     * @throws FileNotFoundException
     */
    public static void writeMatchFixtureToCsvFile(String fileName, List<Match> matchFixtures) throws FileNotFoundException {
        File matchFixtureFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(matchFixtureFile)) {
            pw.println("Match number,Team home,Team away,Ground");
            for (Match match : matchFixtures) {
                pw.println(match.getMatchNumber() + "," + match.getHomeTeam().getName() + "," + match.getAwayTeam().getName() + "," + match.getPlayingGround());
            }
        }
    }

    /**
     * @param teamName        name of a team
     * @param minWicketsTaken number of wickets as a factor denoting the result will contain players who took more
     *                        wickets than this number.
     * @param teams           HashMap containing all the teams mapping team name to the team.
     * @return returns the top bowlers who have taken minimum @param minWicketsTaken from team @param teamName
     * @throws IPLException
     */
    public static List<Player> getTopBowlersByTeamName(String teamName, Integer minWicketsTaken, HashMap<String,
            Team> teams) throws IPLException {
        List<Player> result = new ArrayList<>();
        try {
            Team team;
            if (teams.containsKey(teamName.toUpperCase())) team = teams.get(teamName.toUpperCase());
            else throw new IPLException("ipl.classes.Team " + teamName + " doesn't exists.");
            List<Player> players = team.getPlayers();
            for (Player player : players) {
                if (player.getPlayerRole().equals(PlayerRole.BOWLER) && player.getWicketsTaken() >= minWicketsTaken) {
                    result.add(player);
                }
            }
        } catch (Exception e) {
        } finally {
            return result;
        }
    }

    /**
     * @param playerName name of a player
     * @param players    hash-set containing all the unique players.
     * @return returns all the players whose name contains @param playerName
     */
    public static List<Player> getPlayerByName(String playerName, HashSet<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getName().toLowerCase().contains(playerName.toLowerCase())) {
                result.add(player);
            }
        }
        return result;
    }

    /**
     * Utility function to display profile of a player
     *
     * @param player a player object
     */
    public static void displayPlayerProfile(Player player) {
        System.out.print("Name: " + player.getName());
        System.out.print(" ipl.classes.Team: " + player.getTeam().getName());
        System.out.print(" Role: " + player.getPlayerRole());
        System.out.print(" Matches Played: " + player.getMatchesPlayed());
        System.out.print(" Runs scored: " + player.getRunsScored());
        System.out.print(" Average: " + player.getAverage());
        System.out.print(" Strike rate: " + player.getStrikeRate());
        System.out.print(" Wickets taken: " + player.getWicketsTaken() + "\n");
    }


    /**
     * @param N       denoting the number of top players that are needed to be retrieved.
     * @param players hash-set containing all the unique players.
     * @param ipl     an object of IPL class.
     * @return returns the top @param N batsmen.
     */
    public static List<Player> getNTopBatsmen(Integer N, HashSet<Player> players, IPL ipl) {
        PriorityQueue<Player> topNBatsmen = new PriorityQueue<>(N, new BatsmanComparator(ipl));
        System.out.println("Predicted top " + N + " batsmen of " + ipl.getTournamentName() + ipl.getYear().toString() + " who have played more than " + matchFactor + " matchers are: ");
        HashSet<Player> batsmen = players.stream().filter(player -> player.getMatchesPlayed() >= matchFactor)
                .collect(Collectors.toCollection(HashSet::new));
        for (Player player : batsmen) {
            if (topNBatsmen.size() < N) topNBatsmen.add(player);
            else {
                Player currentBatsmenHead = topNBatsmen.peek();
                if (currentBatsmenHead.getRunsScored() < player.getRunsScored()) {
                    topNBatsmen.poll();
                    topNBatsmen.add(player);
                } else if (currentBatsmenHead.getRunsScored().equals(player.getRunsScored())
                        && (currentBatsmenHead.playerBattingPoints() < player.playerBattingPoints())) {
                    topNBatsmen.poll();
                    topNBatsmen.add(player);
                }
            }
        }
        List<Player> topNBatsmenList = new ArrayList<>();
        while (!topNBatsmen.isEmpty()) {
            topNBatsmenList.add(topNBatsmen.poll());
        }
        return topNBatsmenList;
    }

    /**
     * @param N       denoting the number of top players that are needed to be retrieved.
     * @param players hash-set containing all the unique players.
     * @param ipl     an object of IPL class.
     * @return returns the top @param N bowlers.
     */
    public static List<Player> getTopNBowlers(Integer N, HashSet<Player> players, IPL ipl) {
        PriorityQueue<Player> topNBowlers = new PriorityQueue<>(N, new BowlerComparator(ipl));
        System.out.println("Predicted top " + N + " bowlers of " + ipl.getTournamentName() + ipl.getYear().toString() + " who have played more than " + matchFactor + " matchers are: ");
        HashSet<Player> bowlers = players.stream().filter(player -> player.getMatchesPlayed() >= matchFactor)
                .collect(Collectors.toCollection(HashSet::new));
        for (Player player : bowlers) {
            if (topNBowlers.size() < N) topNBowlers.add(player);
            else {
                Player currentBowlersHead = topNBowlers.peek();
                if (currentBowlersHead.getWicketsTaken() < player.getWicketsTaken()) {
                    topNBowlers.poll();
                    topNBowlers.add(player);
                } else if (currentBowlersHead.getWicketsTaken().equals(player.getRunsScored())
                        && (currentBowlersHead.playerBowlingPoints() < player.playerBowlingPoints())) {
                    topNBowlers.poll();
                    topNBowlers.add(player);
                }
            }
        }
        List<Player> topNBowlersList = new ArrayList<>();
        while (!topNBowlers.isEmpty()) {
            topNBowlersList.add(topNBowlers.poll());
        }
        return topNBowlersList;
    }

    /**
     * @param N       denoting the number of top players that are needed to be retrieved.
     * @param players hash-set containing all the unique players.
     * @param ipl     an object of IPL class.
     * @return returns the top @param N all-rounders.
     */
    public static List<Player> getTopNAllRounders(Integer N, HashSet<Player> players, IPL ipl) {
        PriorityQueue<Player> topNAllRounders = new PriorityQueue<>(N, new AllRounderComparator(ipl));
        System.out.println("Predicted top " + N + " all rounders of " + ipl.getTournamentName() + ipl.getYear().toString() + " who have played more than " + matchFactor + " matchers are: ");
        HashSet<Player> allRounders = players.stream().filter(player -> player.getPlayerRole() == PlayerRole.ALL_ROUNDER)
                .filter(player -> player.getMatchesPlayed() >= matchFactor)
                .collect(Collectors.toCollection(HashSet::new));
        for (Player player : allRounders) {
            if (topNAllRounders.size() < N) topNAllRounders.add(player);
            else {
                Player currentBowlersHead = topNAllRounders.peek();
                if (currentBowlersHead.playerAllRounderPoints() < player.playerAllRounderPoints()) {
                    topNAllRounders.poll();
                    topNAllRounders.add(player);
                } else if (currentBowlersHead.playerAllRounderPoints().equals(player.playerAllRounderPoints())
                        &&
                        ((currentBowlersHead.playerBattingPoints() < player.playerBattingPoints())
                                || (currentBowlersHead.playerBowlingPoints() < player.playerBowlingPoints())
                        )) {
                    topNAllRounders.poll();
                    topNAllRounders.add(player);
                }
            }
        }
        List<Player> topNAllRoundersList = new ArrayList<>();
        while (!topNAllRounders.isEmpty()) {
            topNAllRoundersList.add(topNAllRounders.poll());
        }
        return topNAllRoundersList;
    }

    /**
     * @param teams HashMap containing all the teams mapping team name to the team.
     * @return the list mapping each team to their highest possible score when they play their 11 best batsmen.
     */
    public static List<Map.Entry<String, Double>> getHighestScoresForEachTeam(HashMap<String, Team> teams) {
        HashMap<String, Double> teamsHighestScore = new HashMap<>();
        for (Map.Entry<String, Team> team : teams.entrySet()) {
            List<Player> players = new ArrayList<>();
            for (Player player : team.getValue().getPlayers()) {
                players.add(player);
            }
            Collections.sort(players, new PlayerRunsPerInningsComparator());
            Double highestScore = Double.valueOf(0);
            Double totalBallsLeft = Double.valueOf(120);
            for (Integer index = 0; index < players.size() && index < 11 && totalBallsLeft > 0; index++) {
                Player player = players.get(index);
                if (player.getMatchesPlayed() > 0) {
                    Double predictedRunsPerInnings = getPredictedRunsPerInningsByPlayer(player);
                    Double predictedBallsPerInnings = getPredictedBallsPlayedPerInnings(player);
                    if (totalBallsLeft >= predictedBallsPerInnings) {
                        highestScore += predictedRunsPerInnings;
                        totalBallsLeft -= getPredictedBallsPlayedPerInnings(player);
                    } else {
                        highestScore += totalBallsLeft * getRunsPerBall(player);
                        totalBallsLeft = Double.valueOf(0);
                    }
                }

            }
            teamsHighestScore.put(team.getKey(), highestScore);
        }
        List<Map.Entry<String, Double>> sortedTeamsHighestScore = sortTeamsHighestScoreMap(teamsHighestScore);
        return sortedTeamsHighestScore;
    }

    /**
     * @param teamsHighestScore list mapping each team to their highest possible score when they play their 11 best batsmen.
     * @return sorting the list @param teamsHighestScore
     */
    public static List<Map.Entry<String, Double>> sortTeamsHighestScoreMap(HashMap<String, Double> teamsHighestScore) {
        List<Map.Entry<String, Double>> sortedTeamsHighestScoreList = new LinkedList<>(teamsHighestScore.entrySet());

        Collections.sort(sortedTeamsHighestScoreList, (player1, player2) -> player2.getValue().compareTo(player1.getValue()));

        return sortedTeamsHighestScoreList;
    }

    /**
     * @param team        Team object for a particular team
     * @param matchFactor minimum number of matches a player has to play to be considered as a viable next gen players
     * @return list of players containing next gen players for a particular team
     */
    public static List<Player> getNextGenPlayersByTeam(Team team, Integer matchFactor) {
        List<Player> players = team.getPlayers();
        List<Player> nextGenPlayers = new ArrayList<>();
        try {
            for (Player player : players) {
                if (player.getMatchesPlayed() > 0 && player.getMatchesPlayed() <= matchFactor) {
                    Double runsPerMatch = (double) (player.getRunsScored() / player.getMatchesPlayed());
                    Double wicketsPerMatch = (double) (player.getWicketsTaken() * 1000 / player.getMatchesPlayed());
                    if (runsPerMatch >= 25.0 || wicketsPerMatch >= 500.0) {
                        nextGenPlayers.add(player);
                    }
                }
            }
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
        return nextGenPlayers;
    }

    /**
     * @param players list of players of a particular team
     * @return Player object of the highest wicket taker for the particular team
     */
    public static Player getHighestWicketTaker(List<Player> players) {
        Player highestWicketTaker = players.get(0);
        Integer maximumWickets = highestWicketTaker.getWicketsTaken();
        for (Integer index = 1; index < players.size(); index++) {
            Integer wicketsTaken = players.get(index).getWicketsTaken();
            if (maximumWickets < wicketsTaken) {
                maximumWickets = wicketsTaken;
                highestWicketTaker = players.get(index);
            }
        }
        return highestWicketTaker;
    }

    /**
     * @param players list of players of a particular team
     * @return Player object of the highest runs scored for the particular team
     */
    public static Player getHighestRunsScorer(List<Player> players) {
        Player highestRunsScorer = players.get(0);
        Integer maximumRuns = highestRunsScorer.getRunsScored();
        for (Integer index = 1; index < players.size(); index++) {
            Integer runsScored = players.get(index).getRunsScored();
            if (maximumRuns < runsScored) {
                maximumRuns = runsScored;
                highestRunsScorer = players.get(index);
            }
        }
        return highestRunsScorer;
    }

    /**
     * Utility function to get the highest wicket taker and highest runs scorer for a particular team
     *
     * @param teamName team name of a particular team
     * @param teams    HashMap containing all the teams mapping team name to the team.
     */
    public static void getHighestWicketTakerAndHighestRunScorerByTeamName(String teamName, HashMap<String, Team> teams) {
        try {
            Team team;
            if (teams.containsKey(teamName.toUpperCase())) team = teams.get(teamName.toUpperCase());
            else throw new IPLException("Team " + teamName + " doesn't exists.");
            List<Player> playersInTeam = team.getPlayers();
            Player highestWicketTaker = getHighestWicketTaker(playersInTeam);
            Player highestRunsScorer = getHighestRunsScorer(playersInTeam);
            System.out.println("Team: " + teamName);
            System.out.println("Highest wicket taker: " + highestWicketTaker.getName() + " \t Wickets taken: " + highestWicketTaker.getWicketsTaken());
            System.out.println("Highest runs scorer: " + highestRunsScorer.getName() + " \t Runs scored: " + highestRunsScorer.getRunsScored());
        } catch (Exception e) {
        }
    }

    /**
     * @param player Player object of a particular player
     * @return Returns the predicted runs the player scores in a match
     */
    public static Double getPredictedRunsPerMatchByPlayer(Player player) {
        Double strikeRate = player.getStrikeRate();
        Double totalRunsScored = (double) player.getRunsScored();
        Double totalBallsPlayed = (double) ((totalRunsScored * 100.0) / strikeRate);
        Double totalMatchesPlayed = (double) player.getMatchesPlayed();
        Double predictedBallsPlayedPerMatch = (double) (totalBallsPlayed / totalMatchesPlayed);
        Double runsPerBall = (double) (totalRunsScored / totalBallsPlayed);
        return (double) (predictedBallsPlayedPerMatch * runsPerBall);
    }

    /**
     * @param player Takes the player object for whom the prediction is done for how many runs he/she score in an inning.
     * @return Returns the predicted runs the player will score in an inning.
     */
    public static Double getPredictedRunsPerInningsByPlayer(Player player) {
        Double predictedBallsPlayedPerInnings = getPredictedBallsPlayedPerInnings(player);
        Double runsPerBall = getRunsPerBall(player);
        return (double) (predictedBallsPlayedPerInnings * runsPerBall);
    }

    /**
     * @param player Player object of a particular player
     * @return Returns the average runs a player scores per ball
     */
    public static Double getRunsPerBall(Player player) {
        Double totalRunsScored = (double) player.getRunsScored();
        Double strikeRate = player.getStrikeRate();
        Double totalBallsPlayed = (double) ((totalRunsScored * 100.0) / strikeRate);
        return (double) (totalRunsScored / totalBallsPlayed);
    }

    /**
     * @param player Player object of a particular player
     * @return Returns the predicted balls a player plays in an inning.
     */
    public static Double getPredictedBallsPlayedPerInnings(Player player) {
        Double strikeRate = player.getStrikeRate();
        Double totalRunsScored = (double) player.getRunsScored();
        Double average = player.getAverage();
        Double totalInningsPlayed = (double) (totalRunsScored / average);
        Double totalBallsPlayed = (double) ((totalRunsScored * 100.0) / strikeRate);
        Double totalMatchesPlayed = (double) player.getMatchesPlayed();
        return (double) (totalBallsPlayed / totalInningsPlayed);
    }

    /**
     * @param matchFixtures list of matches containing the match fixtures
     * @return Returns list of matches such that no team play two consecutive matches
     */
    public static List<Match> shuffleFixtures(List<Match> matchFixtures) {
        List<Match> shuffledMatchFixturesList = new ArrayList<>();
        try {
            HashMap<Map.Entry<String, String>, Match> shuffledMatchFixtures = new LinkedHashMap<>();
            String prevHomeTeam = null;
            String prevAwayTeam = null;

            while (shuffledMatchFixtures.size()<matchFixtures.size()){
                boolean flag = false;
                for(Match match: matchFixtures){
                    String currHomeTeam = match.getHomeTeam().getName();
                    String currAwayTeam = match.getAwayTeam().getName();
                    Map.Entry<String, String> currTeamsPair = Map.entry(currHomeTeam, currAwayTeam);
                    if (!shuffledMatchFixtures.containsKey(currTeamsPair)) {
                        if (prevHomeTeam == null && prevAwayTeam == null) {
                            shuffledMatchFixtures.put(currTeamsPair, match);
                            prevHomeTeam = currHomeTeam;
                            prevAwayTeam = currAwayTeam;
                            flag = true;
                            break;
                        } else if (!prevHomeTeam.equals(currHomeTeam) && !prevHomeTeam.equals(currAwayTeam) &&
                                !prevAwayTeam.equals(currHomeTeam) && !prevAwayTeam.equals(currAwayTeam)) {
                            shuffledMatchFixtures.put(currTeamsPair, match);
                            prevHomeTeam = currHomeTeam;
                            prevAwayTeam = currAwayTeam;
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag == false){
                    Collections.shuffle(matchFixtures);
                    shuffledMatchFixtures.clear();
                    prevHomeTeam = null;
                    prevAwayTeam = null;
                }
            }

            for (Map.Entry<Map.Entry<String, String>, Match> matchEntry : shuffledMatchFixtures.entrySet()) {
                shuffledMatchFixturesList.add(matchEntry.getValue());
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return shuffledMatchFixturesList;
    }

    /**
     * @param player Player object of a particular player
     * @return Returns the predicted wickets a player takes in an inning.
     */
    public static Double getPredictedWicketsPerInningsByPlayer(Player player) {
        Double totalRunsScored = (double) player.getRunsScored();
        Double average = player.getAverage();
        Double totalInningsPlayed = (double) (totalRunsScored / average);
        Double totalWicketsTaken = (double) player.getWicketsTaken();
        return (double) (totalWicketsTaken / totalInningsPlayed);
    }

    /**
     * @param matchFixtures list of matches containing the match fixtures
     * @return Returns a boolean true if no team plays 2 consecutive matches and returns false if otherwise
     */
    public static boolean checkIfFixturesAreProper(List<Match> matchFixtures) {
        String prevHomeTeam = null;
        String prevAwayTeam = null;
        for (Match match : matchFixtures) {
            String currHomeTeam = match.getHomeTeam().getName();
            String currAwayTeam = match.getAwayTeam().getName();
            if (prevHomeTeam != null && prevAwayTeam != null) {
                if (prevHomeTeam.equals(currHomeTeam) || prevHomeTeam.equals(currAwayTeam) ||
                        prevAwayTeam.equals(currHomeTeam) || prevAwayTeam.equals(currAwayTeam)) {
                    return false;
                }
            }
            prevHomeTeam = match.getHomeTeam().getName();
            prevAwayTeam = match.getAwayTeam().getName();
        }
        return true;
    }
}