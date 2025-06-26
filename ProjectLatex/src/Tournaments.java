package models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class Tournaments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToMany
    @JoinTable(
        name = "tournament_teams",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches;

    @Column
    private LocalDate start_date;
    @Column
    private LocalDate end_date;
    @Column
    private String prize_pool;
    @Column
    private String type;
    @Column
    private String location;


    public Tournaments() {}

    public Tournaments(String name, Organizer organizer, LocalDate startDate, LocalDate endDate, String prizePool, String type, String location) {
        this.setName(name);
        this.setOrganizer(organizer);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setPrizePool(prizePool);
        this.setType(type);
        this.setLocation(location);
    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public String getPrizePool() {
        return prize_pool;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public List<Team> getTeams() { return teams; }

    public List<Match> getMatches() { return matches; }

    @Override
    public String toString() {
        return "Tournament{" +
                "name='" + name + '\'' +
                ", organizer='" + organizer + '\'' +
                ", startDate=" + start_date +
                ", endDate=" + end_date +
                ", prizePool='" + prize_pool + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getTournamentDetails() {
        return "Tournament: " + name + "\n" +
                "Organizer: " + organizer + "\n" +
                "Start Date: " + start_date + "\n" +
                "End Date: " + end_date + "\n" +
                "Prize Pool: " + prize_pool + "\n" +
                "Type: " + type + "\n" +
                "Location: " + location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void setStartDate(LocalDate startDate) {
        this.start_date = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.end_date = endDate;
    }

    public void setPrizePool(String prizePool) {
        this.prize_pool = prizePool;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTeams(List<Team> teams) { this.teams = teams; }
    public void setMatches(List<Match> matches) { this.matches = matches; }
}