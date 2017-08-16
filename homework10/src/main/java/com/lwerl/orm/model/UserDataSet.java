package com.lwerl.orm.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"user\"")
public class UserDataSet extends DataSet{

    @Column(name="name")
    private String name;

    @Column
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneDataSet> phones;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressDataSet address;

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

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }
}
