package ipl.comparators;

import ipl.classes.Player;

import java.util.Comparator;

public class PlayerAverageComparator implements Comparator<Player> {

    @Override
    public int compare(Player player1, Player player2) {
        Double average1 = player1.getAverage();
        Double average2 = Double.valueOf(0);
        return (int)(average1 - average2);
    }
}
