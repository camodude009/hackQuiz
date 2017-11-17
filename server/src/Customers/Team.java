package Customers;

import java.util.ArrayList;

public class Team {
    private static ArrayList<Person> members;

    public Team(){
        this(new ArrayList<Person>());
    }

    public Team (ArrayList<Person> members){
        this.members = members;
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
