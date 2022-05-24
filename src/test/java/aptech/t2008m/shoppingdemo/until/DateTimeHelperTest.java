package aptech.t2008m.shoppingdemo.until;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeHelperTest {
    public static LocalDateTime convert(){
        return DateTimeHelper.convertStringToLocalDateTime("2022-05-18");
    }

    public static void main(String[] args) {
        System.out.println(convert());
    }
}