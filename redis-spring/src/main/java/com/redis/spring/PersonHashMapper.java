package com.redis.spring;

import org.springframework.data.redis.hash.HashMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: redis
 * @description:
 * @author: sunkang
 * @create: 2019-01-12 22:51
 * @ModificationHistory who      when       What
 **/
public class PersonHashMapper implements HashMapper<Person,String,String> {
    @Override
    public Map<String, String> toHash(Person person) {

        Map<String,String> map = new HashMap<String,String>();
        map.put("firstname",person.getFirstname());
        map.put("lastname",person.getLastname());

        return map;
    }

    @Override
    public Person fromHash(Map<String, String> map) {
        Person person = new Person();
        person.setFirstname( map.get("firstname"));
        person.setLastname( map.get("lastname"));
        return person;
    }
}
