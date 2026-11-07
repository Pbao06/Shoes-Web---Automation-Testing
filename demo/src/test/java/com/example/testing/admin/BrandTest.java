package com.example.testing.admin;

import com.example.testing.*;
import com.example.page.*;
import com.example.page.admin.*;
import com.example.models.admin.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
@Test(groups = {"smoke", "regression", "admin"})

public class BrandTest extends BaseTest {
    @DataProvider(name = "BrandData")
    public Object[][] categoryData() {
        return new Object[][] {
           {
            new Brand("Test Brand 1", "Brand"),
            new Brand("Edited Brand 1", "Brand")
            },
            {
                new Brand("Test Brand 2", "Brand"),
                new Brand("Edited Brand 2", "Brand")
            }
        };
    }
    @Test(dataProvider="BrandData")
    public void smokeNewBrandTesting(Brand rawData,Brand editData)
    {
        openUrl("/login");
        BrandPage brandPage = new LoginPage(driver)
            .typeLogin("admin@example.com", "Admin@123")
            .clickAdmin()
            .clickBrand();

        int countBefore = brandPage.getCateCount();
        brandPage.newBrand(rawData);
        brandPage.checkNewBrand(rawData, countBefore);
        // after we create we -> edit right at instance we just created 
        brandPage.editBrand(editData,rawData.getName());
        brandPage.check_info_edit(editData);
        assertTrue(brandPage.delete(editData.getName())," Message -> wrong ");
    }

    
}
