package models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String region;

    @OneToOne
    @JoinColumn(name = "coach_id")
    private Person coach;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @Column
    private String logoUrl;

    public Team() {}

    public Team(String name, String region, Person coach, String logoUrl) {
        this.setName(name);
        this.setRegion(region);
        this.setCoach(coach);
        this.setLogoUrl(logoUrl);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public Person getCoach() {
        return coach;
    }
    public void setCoach(Person coach) {
        this.coach = coach;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    public Long getId() { return id; }
    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", coach=" + coach +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }

}
