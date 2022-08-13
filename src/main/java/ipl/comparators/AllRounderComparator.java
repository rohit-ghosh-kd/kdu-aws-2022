package ipl.comparators;

import java.util.Comparator;

import ipl.IPLUtility;
import ipl.classes.IPL;
import ipl.classes.Player;
import ipl.classes.PlayerRole;

public class AllRounderComparator implements Comparator<Player> {

    private final Integer maximumInningsInSeasonPerPlayer;
    public AllRounderComparator(IPL ipl){
        maximumInningsInSeasonPerPlayer = (ipl.getTeams().size() - 1) * 2;
    }

    @Override
    public int compare(Player player1, Player player2) {
        Double predictedRunsPerInnings1 = IPLUtility.getPredictedRunsPerInningsByPlayer(player1);
        Double predictedSeasonRunsByPlayer1 = predictedRunsPerInnings1 * maximumInningsInSeasonPerPlayer;
        Double predictedRunsPerInnings2 = IPLUtility.getPredictedRunsPerInningsByPlayer(player2);
        Double predictedSeasonRunsByPlayer2 = predictedRunsPerInnings2 * maximumInningsInSeasonPerPlayer;

        Double predictedWicketsPerInnings1 = IPLUtility.getPredictedWicketsPerInningsByPlayer(player1);
        Double predictedSeasonWicketsByPlayer1 = predictedWicketsPerInnings1 * maximumInningsInSeasonPerPlayer;
        Double predictedWicketsPerInnings2 = IPLUtility.getPredictedWicketsPerInningsByPlayer(player2);
        Double predictedSeasonWicketsByPlayer2 = predictedWicketsPerInnings2 * maximumInningsInSeasonPerPlayer;

        Double predictedAllRoundScoreByPlayer1 = predictedSeasonRunsByPlayer1 + predictedSeasonWicketsByPlayer1;
        Double predictedAllRoundScoreByPlayer2 = predictedSeasonRunsByPlayer2 + predictedSeasonWicketsByPlayer2;

        if(player1.getPlayerRole() == PlayerRole.ALL_ROUNDER && player2.getPlayerRole() != PlayerRole.ALL_ROUNDER) return 2;
        else if(predictedAllRoundScoreByPlayer1 > predictedAllRoundScoreByPlayer2) return 1;
        else if(predictedAllRoundScoreByPlayer1 < predictedAllRoundScoreByPlayer2) return -1;
        return 0;
    }
}
