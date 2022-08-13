package ipl.comparators;

import ipl.classes.Player;

import java.util.Comparator;
import ipl.IPLUtility;
import ipl.classes.IPL;
public class BatsmanComparator implements Comparator<Player> {

    private final Integer maximumInningsInSeasonPerPlayer;

    public BatsmanComparator(IPL ipl) {
        maximumInningsInSeasonPerPlayer = (ipl.getTeams().size() - 1) * 2;
    }

    @Override
    public int compare(Player player1, Player player2) {
        Double predictedRunsPerInnings1 = IPLUtility.getPredictedRunsPerInningsByPlayer(player1);
        Double predictedSeasonRunsByPlayer1 = predictedRunsPerInnings1 * maximumInningsInSeasonPerPlayer;
        Double predictedRunsPerInnings2 = IPLUtility.getPredictedRunsPerInningsByPlayer(player2);
        Double predictedSeasonRunsByPlayer2 = predictedRunsPerInnings2 * maximumInningsInSeasonPerPlayer;
        if(predictedSeasonRunsByPlayer1 > predictedSeasonRunsByPlayer2) return 1;
        else if(predictedSeasonRunsByPlayer1 < predictedSeasonRunsByPlayer2) return -1;
        return 0;
    }
}
