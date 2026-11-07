package com.example.testing;
import com.example.page.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test(groups = {"smoke", "regression"})
public class ProductDetailTest extends BaseTest {
    @Test
    public void Flow_AddToCart_() 
    {
        openUrl("/login"); // get driver at baselink + login home 
        ProductDetail productDetail = new LoginPage(driver).typeLogin("admin@example.com", "Admin@123").clickProductDetail().clickChooseSize().clickAddToCart();
        //  wait.until(ExpectedConditions.urlContains("/home"));
        // 2. Capture initial badge count (e.g., 2)
        int initialCount = productDetail.getCartBadgeCount(); // getting current number on icon bag 
        productDetail.clickAddToCart()
                 .verifyBadgeIncremented(initialCount);
                 int updatedCount = productDetail.getCartBadgeCount(); // this is new number when updated 
        assertEquals(updatedCount,initialCount+1," Not success ");
    }
}
