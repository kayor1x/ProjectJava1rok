package models;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column
    private String role;

    public Player() {
        super("", 0);
    }
    public Player(String name, int age, String nickname, Team team, String role) {
        super(name, age);
        this.setNickname(nickname);
        this.setTeam(team);
        this.setRole(role);
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "Player{" +
                "name='" + getName() + '\'' +
                ", age=" + getAge() +
                ", nickname='" + nickname + '\'' +
                ", team='" + team + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
