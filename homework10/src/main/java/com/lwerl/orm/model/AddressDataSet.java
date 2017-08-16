package com.lwerl.orm.model;

import javax.persistence.*;

@Entity
@Table(name = "\"address\"")
public class AddressDataSet extends DataSet {

    @Column
    private String address;

    @OneToOne
    private UserDataSet user;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }
}
