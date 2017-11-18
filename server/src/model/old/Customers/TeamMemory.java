package model.old.Customers;

import java.util.ArrayList;

public class TeamMemory {

    public static ArrayList<Team> teams;

    public TeamMemory(){
        this.teams = new ArrayList<>();
    }

    public TeamMemory(ArrayList<Team> teams){
        this.teams = teams;
    }


    // --------------------------------------------------- adjust teams ------------------------------------------------

    public void addTeam(Team team){
        this.teams.add(team);
    }

    public Team removeTeam(Team team){
        if(!teams.contains(team)) return null;
        teams.remove(team);
        return team;
    }



    // -------------------------------------------------- adjust points ------------------------------------------------
}
