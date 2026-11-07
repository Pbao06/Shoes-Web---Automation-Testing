package com.example.page.admin;

import com.example.page.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashBoardPage extends BasePage {
    private By productPage = By.xpath("//a[@href='/admin/products']");
    private By orderPage = By.cssSelector("a[href='/admin/order']");

    public DashBoardPage(WebDriver driver) {
        super(driver);
    }

    public ProductsAdminPage clickProducts() {
        waitForVisible(productPage).click();
        wait.until(ExpectedConditions.urlContains("/products"));
        return new ProductsAdminPage(driver);
    }

    public OrderPage clickOrder() {
        waitForVisible(orderPage).click();
        wait.until(ExpectedConditions.urlContains("/order"));
        return new OrderPage(driver);
    }

    public CategoryAdminPage clickCategory() {
        waitForVisible(By.xpath("//a[@href='/admin/categories']")).click();
        // wait for a element in page CategoryPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='categories-title']")));
        return new CategoryAdminPage(driver);
    }

    public BrandPage clickBrand() {
        waitForVisible(By.xpath("//a[@href='/admin/brands']")).click();
        // Wait until show up a element in brands page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='brands-title']")));
        return new BrandPage(driver);
    }
}
