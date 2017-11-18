package model.old.Customers;

import model.old.Evaluation.Points;

import java.util.ArrayList;

public class Team {
    private ArrayList<Person> members;
    private Points points;



    // --------------------------------------------- onstructor --------------------------------------------------------
    public Team(){
        this(new ArrayList<Person>(),5, 10);
    }

    public Team (ArrayList<Person> members, int numberRounds, int questionsPerRound){
        this.members = members;
        points = new Points(numberRounds, questionsPerRound);
    }






    // -------------------------------------------- getter & setter ----------------------------------------------------

    public Boolean[] getRounds(int round){
        return points.getRounds(round);
    }

    public long getPoints(int round){
        return points.getPoints(round);
    }

    public void setAnswer(int round, int questionNumber, boolean right){
        points.setAnswer(round, questionNumber, right);
    }






    //-------------------------------------------- adjust Team ---------------------------------------------------------
    public void addMember(Person person){
        members.add(person);
    }

    public Person deleteMember(Person person){
        if(!members.contains(person)) return null;
        members.remove(person);
        return person;
    }




}
