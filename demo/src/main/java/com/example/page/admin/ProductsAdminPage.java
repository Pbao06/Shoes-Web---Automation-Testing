package com.example.page.admin;

import com.example.page.*;
import com.example.models.admin.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class ProductsAdminPage extends BasePage {

    private By inputSearch = By.cssSelector("input[placeholder='Search products...']");
    // specific product/id/2
    private By valueName = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Product')]/preceding-sibling::th)+1] //p[1]");
    private By valueSlug = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Product')]/preceding-sibling::th)+1] //p[2]");
    private By valueBrand = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Brand')]/preceding-sibling::th) + 1 ]");
    private By valueCate = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Category')]/preceding-sibling::th)+1]");
    private By valueSalePrice = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Price')]/preceding-sibling::th)+1] //span[1]");
    private By valuePrice = By.xpath(
            "//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Price')]/preceding-sibling::th)+1] //span[2]");
    private By iconEdit = By.xpath("//a[@href='/admin/products/2/edit']");
    // delete part
    private String deleteIcon = " //tr[@data-testid='product-row-2'] //button[@aria-label='Delete product']";
    private By deleteButtonModel = By.xpath(" //button[normalize-space()='Delete']");
    // adding product
    private By newProductBtn = By.cssSelector("a[href='/admin/products/new']");

    // this is for new product actually
    // name

    public ProductsAdminPage(WebDriver driver) {
        super(driver);
    }

    public boolean deleteProductPre(int id) // id user want to delete
    {

        String newDeleteString = deleteIcon.replace("2", String.valueOf(id));
        waitForVisible(By.xpath(newDeleteString)).click();
        waitForVisible(deleteButtonModel).click();
        WebDriverWait waitTemp = new WebDriverWait(
                driver,
                Duration.ofSeconds(50));
        String exampleDeleted = "//tr[@data-testid='product-row-anything']";
        String actualDeleted = exampleDeleted.replace("anything", String.valueOf(id));
        try {
            return waitTemp.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(actualDeleted)));

        } catch (TimeoutException e) {
            return false;
        }
    }

    public ProductNew clickAddProduct() {
        waitForClickable(newProductBtn).click();
        // wait until any element of product new page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'New Product')]")));
        return new ProductNew(driver);
    }

    public ProductsAdminPage SearchData() {
        waitForVisible(inputSearch).sendKeys("Classic");
        return this;
    }

    public String getNameProduct() {
        return waitForVisible(valueName).getText();
    }

    public ProductEdit clickProductEdit() {
        waitForClickable(iconEdit).click();
        wait.until(ExpectedConditions.urlContains("/products/2/edit"));
        return new ProductEdit(driver);
    }

    private String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }

    public boolean isEditSuccess(ProductEditClass data) {
        // 1. Lấy và chuẩn hóa dữ liệu từ UI (chuyển hết về lower case & trim)
        String actualName = waitForVisible(valueName).getText().trim().toLowerCase();
        String actualSlug = waitForVisible(valueSlug).getText().trim().toLowerCase();
        String actualBrand = waitForVisible(valueBrand).getText().trim().toLowerCase();
        String actualCate = waitForVisible(valueCate).getText().trim().toLowerCase();

        String actualPrice = waitForVisible(valuePrice).getText().trim();
        String actualSalePrice = waitForVisible(valueSalePrice).getText().trim();

        // 2. Chuẩn hóa dữ liệu Parameter đầu vào (chuyển về lower case & format giá
        // tiền)
        String expectedName = data.getProductName().trim().toLowerCase();
        String expectedSlug = data.getSlug().trim().toLowerCase();
        String expectedBrand = data.getBrand().trim().toLowerCase();
        String expectedCategory = data.getCategory().trim().toLowerCase();

        String expectedPrice = formatCurrency(data.getPrice());
        String expectedSalePrice = formatCurrency(data.getSalePrice());

        // 3. Kiểm tra từng trường
        if (!actualName.contains(expectedName)) {
            System.err.printf("❌ NAME LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedName, actualName);
            return false;
        }

        if (!actualSlug.contains(expectedSlug)) {
            System.err.printf("❌ SLUG LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedSlug, actualSlug);
            return false;
        }

        if (!actualBrand.contains(expectedBrand)) {
            System.err.printf("❌ BRAND LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedBrand, actualBrand);
            return false;
        }

        if (!actualCate.contains(expectedCategory)) {
            System.err.printf("❌ CATEGORY LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedCategory, actualCate);
            return false;
        }

        if (!actualPrice.contains(expectedPrice)) {
            System.err.printf("❌ PRICE LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedPrice, actualPrice);
            return false;
        }

        if (!actualSalePrice.contains(expectedSalePrice)) {
            System.err.printf("❌ SALE PRICE LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedSalePrice,
                    actualSalePrice);
            return false;
        }

        return true;
    }

    // Count total products shown in the table
    public int getProductCount() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("tr[data-testid^='product-row-']")));
        return driver.findElements(
                By.cssSelector("tr[data-testid^='product-row-']")).size(); // use size because it is list (findElements)
    }

    // Find a product's ID by its name; returns -1 if not found
    public int findProductIdByName(String productName) {
        List<WebElement> rows = driver.findElements(
                By.cssSelector("tr[data-testid^='product-row-']"));
        for (WebElement row : rows) {
            String testId = row.getAttribute("data-testid");
            int productId = Integer.parseInt(testId.replace("product-row-", ""));
            WebElement nameElement = row.findElement(By.xpath(".//p[1]"));
            if (nameElement.getText().trim().equals(productName)) {
                return productId;
            }
        }
        return -1;
    }

    // Verify new product was added: count increased by 1 AND all fields match
    public boolean checkNewProduct(NewProduct data, int countBefore) {
        // 1. Verify count increased by 1
        int countAfter = getProductCount();
        if (countAfter != countBefore + 1) {
            System.err.printf("❌ COUNT LỆCH | Expected: %d | Actual: %d%n", countBefore + 1, countAfter);
            return false;
        }

        // 2. Find product by name → extract its ID from data-testid
        int productId = findProductIdByName(data.getName());
        if (productId == -1) {
            System.err.printf("❌ SẢN PHẨM KHÔNG TÌM THẤY | Name: '%s'%n", data.getName());
            return false;
        }

        String row = "//tr[@data-testid='product-row-" + productId + "']";

        // 3. Verify individual fields (lowercase text to avoid case mismatches with UI)
        String actualName = waitForVisible(By.xpath(row + "//p[1]")).getText().trim().toLowerCase();
        if (!actualName.equals(data.getName().toLowerCase())) {
            System.err.printf("❌ NAME LỆCH | Expected: '%s' | Actual: '%s'%n", data.getName(), actualName);
            return false;
        }

        String actualSlug = waitForVisible(By.xpath(row + "//p[2]")).getText().trim().toLowerCase();
        if (!actualSlug.equals(data.getSlug().toLowerCase())) {
            System.err.printf("❌ SLUG LỆCH | Expected: '%s' | Actual: '%s'%n", data.getSlug(), actualSlug);
            return false;
        }

        String actualBrand = waitForVisible(By.xpath(row + "/td[2]")).getText().trim().toLowerCase();
        if (!actualBrand.equals(data.getBrand().toLowerCase())) {
            System.err.printf("❌ BRAND LỆCH | Expected: '%s' | Actual: '%s'%n", data.getBrand(), actualBrand);
            return false;
        }

        String actualCategory = waitForVisible(By.xpath(row + "/td[3]")).getText().trim().toLowerCase();
        if (!actualCategory.equals(data.getCategory().toLowerCase())) {
            System.err.printf("❌ CATEGORY LỆCH | Expected: '%s' | Actual: '%s'%n", data.getCategory(), actualCategory);
            return false;
        }

        // Price: span[1] = sale price (bold), span[2] = regular price (strikethrough)
        String actualSalePrice = waitForVisible(By.xpath(row + "/td[4]//span[1]")).getText().trim();
        String expectedSalePrice = String.format("$%.2f", data.getSalePrice());
        if (!actualSalePrice.equals(expectedSalePrice)) {
            System.err.printf("❌ SALE PRICE LỆCH | Expected: '%s' | Actual: '%s'%n", expectedSalePrice,
                    actualSalePrice);
            return false;
        }

        String actualRegularPrice = waitForVisible(By.xpath(row + "/td[4]//span[2]")).getText().trim();
        String expectedRegularPrice = String.format("$%.2f", data.getPrice());
        if (!actualRegularPrice.equals(expectedRegularPrice)) {
            System.err.printf("❌ REGULAR PRICE LỆCH | Expected: '%s' | Actual: '%s'%n", expectedRegularPrice,
                    actualRegularPrice);
            return false;
        }
        return true;
    }
}
