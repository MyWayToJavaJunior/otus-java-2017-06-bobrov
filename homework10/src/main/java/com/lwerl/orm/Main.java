package com.lwerl.orm;

import com.lwerl.orm.model.AddressDataSet;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.orm.service.DBService;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.orm.service.HibernateService;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBService service = DBServiceImpl.getInstance();
        UserDataSet user = new UserDataSet("Nick", 28);

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setPhone("123456789");
        phone1.setUser(user);

        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setPhone("987654321");
        phone2.setUser(user);

        AddressDataSet address = new AddressDataSet();
        address.setAddress("Mother Russia");
        address.setUser(user);

        List<PhoneDataSet> phones = Arrays.asList(phone1, phone2);

        user.setAddress(address);
        user.setPhones(phones);

        service.saveUser(user);

        HibernateService.getInstance().close();
    }
}
