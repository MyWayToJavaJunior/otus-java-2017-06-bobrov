import model.AddressDataSet;
import model.PhoneDataSet;
import model.UserDataSet;
import service.DBService;
import service.DBServiceImpl;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBService service = DBServiceImpl.getInstance();

        UserDataSet user = new UserDataSet();

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setPhone("123456789");
        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setPhone("987654321");
        List<PhoneDataSet> phones = Arrays.asList(phone1, phone2);

        AddressDataSet address = new AddressDataSet();
        address.setAddress("Unknown");

        user.setName("Nick");
        user.setAge(28);
        user.setAddress(address);
//        user.setPhones(phones);

        service.saveUser(user);

    }
}
