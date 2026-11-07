package com.example.testing.admin;

import com.example.testing.*;
import com.example.page.*;
import com.example.page.admin.CategoryAdminPage;
import com.example.models.admin.NewCate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test(groups = {"smoke", "regression", "admin"})
public class CategoryTest extends BaseTest {

    @DataProvider(name = "categoryData")
    public Object[][] categoryData() {
        return new Object[][] {
            { new NewCate("Test Category 1", "test-category-1", "Description 1") },
            { new NewCate("Test Category 2", "test-category-2", "Description 2") },
            { new NewCate("Test Category 3", "test-category-3", "Description 3") }
        };
    }

    @DataProvider(name = "editCategoryData")
    public Object[][] editCategoryData() {
        return new Object[][] {
            { new NewCate("Category to Edit", "category-to-edit", "Original description"),
              new NewCate("Edited Category", "edited-category", "Edited description") }
        };
    }

    @Test
    public void smokeSearch() {
        openUrl("/login");
        var testSearch = new LoginPage(driver)
            .typeLogin("admin@example.com", "Admin@123")
            .clickAdmin()
            .clickCategory()
            .smokeCheckSearch("bags");
        assertTrue(testSearch, "Result not success");
    }

    @Test(dataProvider = "categoryData")
    public void smokeNewCate(NewCate data) {
        openUrl("/login");
        CategoryAdminPage catePage = new LoginPage(driver)
            .typeLogin("admin@example.com", "Admin@123")
            .clickAdmin()
            .clickCategory();

        int countBefore = catePage.getCateCount();
        catePage.smokeNewCate(data);
        assertTrue(catePage.checkNewCategory(data, countBefore),
            "New category was not added successfully");
    }

   
@Test(dataProvider = "editCategoryData")
public void smokeEditAndDeleteCate(
        NewCate originalData,
        NewCate editData) {

    openUrl("/login");

    CategoryAdminPage catePage = new LoginPage(driver)
        .typeLogin("admin@example.com", "Admin@123")
        .clickAdmin()
        .clickCategory();

    // CREATE
    int countBefore = catePage.getCateCount();

    catePage.smokeNewCate(originalData);

    assertTrue(
        catePage.checkNewCategory(originalData, countBefore),
        "CREATE FAILED: Category was not created correctly"
    );

    // EDIT
    catePage.checkEdit(editData, originalData.getName());

    assertTrue(
        catePage.checking_correct_info(editData),
        "EDIT FAILED: Category information is incorrect"
    );

    // DELETE
    assertTrue(
        catePage.Delete(editData.getName()),
        "DELETE FAILED: Category still exists"
    );
}

}
