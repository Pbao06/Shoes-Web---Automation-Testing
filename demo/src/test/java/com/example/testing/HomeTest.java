package com.example.testing;
import com.example.page.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
@Test(groups = {"regression"})
public class HomeTest extends BaseTest {

    
    @Test
    public void search_page()
    {
        driver.get("https://shoes-shop-bice.vercel.app/home");

         new HomePage(driver).clickSearch();

        
         assertTrue(driver.getCurrentUrl().contains("Search"));
    }
    // @Test
    // public void Test_ProductDetail()
    // {
    //     driver.get("https://shoes-shop-bice.vercel.app/home");

    //     new HomePage(driver).clickProductDetail();
    //     wait.until(ExpectedConditions.urlContains("/product/4"));
    //      assertTrue(driver.getCurrentUrl().contains("/product/4"));
    // }
}
