package com.example.testing.admin;
import com.example.testing.*;
import com.example.page.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
@Test(groups = {"admin", "regression"})
public class OrderTest extends BaseTest{
    @Test
    public void check_Url()
    {
        openUrl("/login");
         var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin().clickOrder();
         assertTrue(driver.getCurrentUrl().contains("/order"));
    }
}
