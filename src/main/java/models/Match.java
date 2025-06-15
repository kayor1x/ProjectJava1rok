package models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournaments tournament;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Column
    private java.time.LocalDateTime dateTime;
    @Column
    private String result;
    @Column
    private String round;
    @Column
    private String bracket;
    @Column
    private String stage = "Play-off";

    public Match() {}

    public Match(Team team1, Team team2, LocalDateTime dateTime, String result, String round, String bracket) {
        this.setTeam1(team1);
        this.setTeam2(team2);
        this.setDateTime(dateTime);
        this.setResult(result);
        this.setRound(round);
        this.setBracket(bracket);
    }
    public Long getId() { return id; }
    public Tournaments getTournaments() { return tournament; }
    public void setTournament(Tournaments tournament) { this.tournament = tournament; }
    public Team getTeam1() {
        return team1;
    }
    public void setTeam1(Team team1) {
        this.team1 = team1;
    }
    public Team getTeam2() {
        return team2;
    }
    public void setTeam2(Team team2) {
        this.team2 = team2;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getRound() {
        return round;
    }
    public void setRound(String round) {
        this.round = round;
    }
    public String getBracket() {
        return bracket;
    }
    public void setBracket(String bracket) {
        this.bracket = bracket;
    }
    public String getStage() {
        return stage;
    }
    @Override
    public String toString() {
        return "Match{" +
                "tournament=" + tournament +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", dateTime=" + dateTime +
                ", result='" + result + '\'' +
                ", round='" + round + '\'' +
                ", bracket='" + bracket + '\'' +
                ", stage='" + stage + '\'' +
                '}';
    }

}
