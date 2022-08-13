package ipl.classes;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {
    private static Integer totalPlayers = 0;
    private Integer id = 0;
    private String name = null;
    private Team team = null;
    private PlayerRole playerRole = null;
    private Integer matchesPlayed = null;
    private Integer runsScored = null;
    private Double average = null;
    private Double strikeRate = null;
    private Integer wicketsTaken = null;

    private static final Double weightRunsScored = 100.0;
    private static final Double weightAverage = 70.0;
    private static final Double weightStrikeRate = 40.0;
    private static final Double weightWicketsTaken = 100.0;
    private static final Double weightMatchesPlayed = 2.0;
    private static final Double weightBowlingTotal = 3000.0;
    private static final Double weightBattingTotal = 500.0;
    public Player(String name, Team team, PlayerRole playerRole, Integer matchesPlayed, Integer runsScored, Double average, Double strikeRate, Integer wicketsTaken) {
        this.id = ++totalPlayers;
        this.name = name;
        this.team = team;
        this.playerRole = playerRole;
        this.matchesPlayed = matchesPlayed;
        this.runsScored = runsScored;
        this.average = average;
        this.strikeRate = strikeRate;
        this.wicketsTaken = wicketsTaken;
    }

    public Player(String name, PlayerRole playerRole, Integer matchesPlayed, Integer runsScored, Double average, Double strikeRate, Integer wicketsTaken) {
        this.id = ++totalPlayers;
        this.name = name;
        this.playerRole = playerRole;
        this.matchesPlayed = matchesPlayed;
        this.runsScored = runsScored;
        this.average = average;
        this.strikeRate = strikeRate;
        this.wicketsTaken = wicketsTaken;
    }

    public Double playerBattingPoints() {
        return (weightRunsScored * this.getRunsScored() + weightAverage * this.getAverage() +
                weightStrikeRate * this.getStrikeRate()) / (weightMatchesPlayed * this.getMatchesPlayed());
    }

    public Double playerBowlingPoints() {
        return (weightRunsScored * this.getWicketsTaken()) / (weightMatchesPlayed * this.getMatchesPlayed());
    }

    public Double playerAllRounderPoints(){
        return weightBattingTotal*this.playerBattingPoints() + weightBowlingTotal*this.playerBowlingPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null || this.getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return this.id==player.id && this.name==player.name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ipl.classes.Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team=" + team.getName() +
                ", playerRole=" + playerRole +
                ", matchesPlayed=" + matchesPlayed +
                ", runsScored=" + runsScored +
                ", average=" + average +
                ", strikeRate=" + strikeRate +
                ", wicketsTaken=" + wicketsTaken +
                '}';
    }
}
