import com.lwerl.department.ATMDepartment;
import com.lwerl.department.ATMInfo;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<ATMInfo> atmInfoList = new ArrayList<>();
        atmInfoList.add(new ATMInfo(150, "key1"));
        atmInfoList.add(new ATMInfo(100, "key2"));
        atmInfoList.add(new ATMInfo(50, "key3"));
        ATMDepartment atmDepartment = new ATMDepartment(atmInfoList);
        System.out.println(atmDepartment.getAllBalance());
        atmDepartment.getAtmInfoList().get(0).getAtm().executeFromMenu();
        System.out.println(atmDepartment.getAllBalance());
        atmDepartment.getAtmInfoList().get(0).getAtm().executeFromMenu();
        System.out.println(atmDepartment.getAllBalance());
        atmDepartment.initAllATM();
        System.out.println(atmDepartment.getAllBalance());
    }
}
