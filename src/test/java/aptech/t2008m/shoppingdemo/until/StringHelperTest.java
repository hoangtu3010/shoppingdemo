package aptech.t2008m.shoppingdemo.until;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTest {
    @Test
    public void testSlug(){
        System.out.println(StringHelper.toSlug("Welcome to VietNam"));
    }
}