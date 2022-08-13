package ipl.comparators;

import ipl.IPLUtility;
import ipl.classes.IPL;
import ipl.classes.Player;

import java.util.Comparator;

public class BowlerComparator implements Comparator<Player> {

    private final Integer maximumInningsInSeasonPerPlayer;

    public BowlerComparator(IPL ipl){
        maximumInningsInSeasonPerPlayer = (ipl.getTeams().size() - 1) * 2;
    }
    @Override
    public int compare(Player player1, Player player2) {
        Double predictedWicketsPerInnings1 = IPLUtility.getPredictedWicketsPerInningsByPlayer(player1);
        Double predictedSeasonWicketsByPlayer1 = predictedWicketsPerInnings1 * maximumInningsInSeasonPerPlayer;
        Double predictedWicketsPerInnings2 = IPLUtility.getPredictedWicketsPerInningsByPlayer(player2);
        Double predictedSeasonWicketsByPlayer2 = predictedWicketsPerInnings2 * maximumInningsInSeasonPerPlayer;
        if(predictedSeasonWicketsByPlayer1 > predictedSeasonWicketsByPlayer2) return 1;
        else if(predictedSeasonWicketsByPlayer1 < predictedSeasonWicketsByPlayer2) return -1;
        return 0;
    }
}
