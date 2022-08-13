package ipl.comparators;

import ipl.IPLUtility;
import ipl.classes.Player;

import java.util.Comparator;

public class PlayerRunsPerMatchComparator implements Comparator<Player> {

    @Override
    public int compare(Player player1, Player player2) {
        Double predictedRunsPerMatch1 = Double.valueOf(0);
        if(player1.getMatchesPlayed() > 0){
            predictedRunsPerMatch1 = IPLUtility.getPredictedRunsPerMatchByPlayer(player1);
        }
        Double predictedRunsPerMatch2 = Double.valueOf(0);
        if(player2.getMatchesPlayed() > 0){
            predictedRunsPerMatch2 = IPLUtility.getPredictedRunsPerMatchByPlayer(player2);
        }
        return (int)(predictedRunsPerMatch1 - predictedRunsPerMatch2);
    }
}
