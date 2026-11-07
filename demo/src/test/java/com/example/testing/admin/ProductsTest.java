package com.example.testing.admin;
import com.example.testing.*;
import com.example.page.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
@Test(groups = {"admin", "regression"})
public class ProductsTest extends BaseTest {
    @Test
    public void deleteProduct()
    { 
        openUrl("/login");
         var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin().clickProducts().deleteProductPre(1);
         assertTrue(test," Wrong Delete got bug u need to fix ");
    }
    // @Test
    // public void test()
    // {
    //     openUrl("/login");
    //      var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin().clickProducts().SearchData().getNameProduct();
    //      String expected="Classic Leather Loafer";
    //      assertEquals(test.toUpperCase(),expected.toUpperCase()," DO not match lol dumb");
    // }
}