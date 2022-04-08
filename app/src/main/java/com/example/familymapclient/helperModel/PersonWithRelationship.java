package com.example.familymapclient.helperModel;

import Model.Person;

public class PersonWithRelationship {
    private final Person person;
    private final String relationship;
    public PersonWithRelationship(Person person, String relationship){
        this.person = person;
        this.relationship = relationship;
    }

    public Person getPerson() {
        return person;
    }

    public String getRelationship(){
        return relationship;
    }
}
