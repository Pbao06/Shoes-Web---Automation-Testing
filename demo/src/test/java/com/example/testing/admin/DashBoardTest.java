package com.example.testing.admin;
import com.example.testing.*;
import com.example.page.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test(groups = {"smoke", "regression", "admin"})
public class DashBoardTest extends BaseTest {
    @Test
    public void check()
    {
         openUrl("/login");
         var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin();
         assertTrue(driver.getCurrentUrl().contains("/admin"));
    }
}
