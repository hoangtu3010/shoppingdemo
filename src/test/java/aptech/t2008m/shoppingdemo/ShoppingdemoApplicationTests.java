package aptech.t2008m.shoppingdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ShoppingdemoApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        String[] roleStr = new String[2];

        roleStr[0] = "admin";
        roleStr[1] = "user";

        System.out.println(roleStr);
    }
}
