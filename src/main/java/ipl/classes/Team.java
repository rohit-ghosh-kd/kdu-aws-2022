package ipl.classes;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class Team {
    private static Integer totalTeams = 0;
    private Integer id =0;
    private String name ;
    private List<Player> players = new ArrayList<>();
    private String homeGround;

    public Team(String name, List<Player> players, String homeGround) {
        this.id = ++totalTeams;
        this.name = name;
        this.players = players;
        this.homeGround = homeGround;
    }

    public Team(String name, String homeGround) {
        this.id = ++totalTeams;
        this.name = name;
        this.homeGround = homeGround;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return this.name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public String getHomeGround() {
        return this.homeGround;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setHomeGround(String homeGround) {
        this.homeGround = homeGround;
    }

    public String toString() {
        return "ipl.classes.Team(id=" + this.getId() + ", name=" + this.getName() + ", homeGround=" + this.getHomeGround() + ")";
    }
}
