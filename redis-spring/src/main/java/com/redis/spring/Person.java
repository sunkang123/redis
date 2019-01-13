package com.redis.spring;

/**
 * @Project: redis
 * @description:
 * @author: sunkang
 * @create: 2019-01-12 22:46
 * @ModificationHistory who      when       What
 **/
public class Person {
    private    String firstname;
    private   String lastname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
