import com.lwerl.atm.ATM;
import com.lwerl.atm.StandardATM;

/**
 * Created by lWeRl on 15.07.2017.
 */
public class Main {
    public static void main(String[] args) {
        ATM atm = new StandardATM(100);
        atm.start();
    }
}
