package ipl.comparators;

import ipl.IPLUtility;
import ipl.classes.Player;

import java.util.Comparator;

public class PlayerRunsPerInningsComparator implements Comparator<Player> {
    @Override
    public int compare(Player player1, Player player2) {
        Double predictedRunsPerInnings1 = Double.valueOf(0);
        if(player1.getMatchesPlayed() > 0){
            predictedRunsPerInnings1 = IPLUtility.getPredictedRunsPerInningsByPlayer(player1);
        }
        Double predictedRunsPerInnings2 = Double.valueOf(0);
        if(player2.getMatchesPlayed() > 0){
            predictedRunsPerInnings2 = IPLUtility.getPredictedRunsPerMatchByPlayer(player2);
        }
        return (int)(predictedRunsPerInnings1 - predictedRunsPerInnings2);
    }
}
