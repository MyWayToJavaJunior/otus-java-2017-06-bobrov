package com.lwerl.orm;

import com.lwerl.orm.model.AddressDataSet;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.orm.service.DBService;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.orm.service.HibernateService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DBService service = DBServiceImpl.getInstance();

//        for (int i = 0; i < 50; i++) {
//            UserDataSet user = new UserDataSet("Nick", 28);
//
//            PhoneDataSet phone1 = new PhoneDataSet();
//            phone1.setPhone("123456789");
//            phone1.setUser(user);
//
//            PhoneDataSet phone2 = new PhoneDataSet();
//            phone2.setPhone("987654321");
//            phone2.setUser(user);
//
//            AddressDataSet address = new AddressDataSet();
//            address.setAddress("Mother Russia");
//            address.setUser(user);
//
//            List<PhoneDataSet> phones = Arrays.asList(phone1, phone2);
//
//            user.setAddress(address);
//            user.setPhones(phones);
//
//            service.saveUser(user);
//        }

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < 100; i++) {
            Long id = (long) (random.nextInt(50) + 1);
            UserDataSet user = service.getUser(id);
            user.setName("Evicted");
            service.saveUser(user);
        }

        System.out.println("Hit: " + service.getCache().getHitCount());
        System.out.println("Miss: " + service.getCache().getMissCount());

        Thread.sleep(510);

        for (int i = 0; i < 200; i++) {
            Long id = (long) (random.nextInt(50) + 1);
            service.getUser(id);
        }

        System.out.println("Hit: " + service.getCache().getHitCount());
        System.out.println("Miss: " + service.getCache().getMissCount());

        Thread.sleep(510);

        for (int i = 1; i <= 50; i++) {
            service.getUser((long) i);
        }

        System.out.println("Hit: " +service.getCache().getHitCount());
        System.out.println("Miss: " +service.getCache().getMissCount());

        HibernateService.getInstance().close();
    }
}
