package com.example.testing.admin;
import com.example.testing.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.example.models.admin.*;
import static org.testng.Assert.assertTrue;
import com.example.page.LoginPage;
import com.example.page.admin.*;

@Test(groups = {"admin", "regression"})
public class ProductNewTest extends BaseTest {


    // public ProductsAdminPage b1;
    // public ProductNew b2;

    @DataProvider(name="DataProductNew")
    public Object[][] getDataProductEdit()
    {
        return new  Object[][] 
        {
          {
                new NewProduct(
                    "Test 1",
                    "phan-bao",
                    "Test Description",
                    450,
                    440, // sale price must not exceed regular price 
                    "Black,White",
                    "Nova",
                    "Bags",
                    "c:\\Users\\phang\\OneDrive\\Pictures\\Indie Game Background.jfif"
                )
            }  
        };
    }
    @Test(dataProvider="DataProductNew")
    public void TestingSmoke(NewProduct data)
    {
        openUrl("/login");
        ProductsAdminPage b1 = new LoginPage(driver)
            .typeLogin("admin@example.com", "Admin@123")
            .clickAdmin()
            .clickProducts();

        // Capture product count BEFORE adding
        int countBefore = b1.getProductCount();

        // Add new product → fillOutData returns ProductsAdminPage after redirect
        ProductsAdminPage b2 = b1.clickAddProduct().fillOutData(data);

        // Verify: count increased by 1 + product data matches
        assertTrue(b2.checkNewProduct(data, countBefore),
                "New product was not added successfully");
    }
}
