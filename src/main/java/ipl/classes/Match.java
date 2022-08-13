package ipl.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Match {
    private static Integer totalMatches = 0;
    private Integer matchNumber;
    private Team homeTeam = new Team();
    private Team awayTeam = new Team();
    private String playingGround;

    public Match(Team homeTeam, Team awayTeam, String playingGround) {
        this.matchNumber = ++totalMatches;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.playingGround = playingGround;
    }

    @Override
    public String toString() {
        return "ipl.classes.Match{" +
                "matchNumber=" + matchNumber +
                ", homeTeam=" + homeTeam.getName() +
                ", awayTeam=" + awayTeam.getName() +
                ", playingGround='" + playingGround + '\'' +
                '}';
    }
}
