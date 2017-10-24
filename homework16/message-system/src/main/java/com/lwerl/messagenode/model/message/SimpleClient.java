package com.lwerl.messagenode.model.message;

import com.lwerl.messagenode.model.Address;
import com.lwerl.messagenode.model.Addressee;

/**
 * Created by lWeRl on 24.10.2017.
 */
public class SimpleClient implements Addressee {

    private Address address;

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    public void printMessage(String text){
        System.out.println(text);
    }
}
