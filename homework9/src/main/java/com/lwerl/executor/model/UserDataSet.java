package com.lwerl.executor.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet{

    @Column(name="name")
    private String name;

    @Column(name="age")
    private Integer age;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
