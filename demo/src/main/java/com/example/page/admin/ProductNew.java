package com.example.page.admin;

import com.example.page.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import com.example.models.admin.*;

public class ProductNew extends BasePage {

    private By inputName = By.xpath("//span[contains(text(),'Product Name')]/../input");
    private By inputSlug = By.xpath("//span[normalize-space()='Slug']/../input");
    private By inputDescription = By.xpath("//span[normalize-space()='Description']/../textarea");
    private By inputPrice = By.xpath("//span[contains(text(),'Price')]/../input");

    private By inputSalePrice = By.xpath("//span[contains(text(),'Sale Price')]/../input");

    private By inputColor = By.xpath("//span[contains(text(),'Color')]/../input");

    private By selectBrand = By.xpath("//span[contains(text(),'Brand')]/../Select");
    private String optionBrand="//span[contains(text(),'Brand')]/../Select/option[contains(text(),'%s')]";

    private By selectCategory = By.xpath("//span[contains(text(),'Category')]/../Select");
    private String optionCate= "//span[contains(text(),'Category')]/../Select/option[contains(text(),'%s')]";
    // img attach file 
    private By btn_File_Img=By.cssSelector("input[type='file']");
     private By img_Banner=By.cssSelector("img[alt='Indie Game Background']");


    // button -> save product 
    private By btn_Create_Product= By.xpath("//button[contains(text(),'Create Product')]");

    public ProductNew(WebDriver driver) {
        super(driver);

    }
    public ProductsAdminPage fillOutData(NewProduct data)
    {
        visiableToSendData(inputName,data.getName());
        visiableToSendData(inputSlug,data.getSlug());
        visiableToSendData(inputDescription,data.getDescription());
        visiableToSendData(inputPrice,String.valueOf(data.getPrice()));
        visiableToSendData(inputSalePrice,String.valueOf(data.getSalePrice()));
        visiableToSendData(inputColor,data.getColor());
        // selectopiton  below
        SelectOption(selectBrand,optionBrand ,data.getBrand());
        SelectOption(selectCategory,optionCate ,data.getCategory());
        TypeClick(btn_Create_Product);

        // After clicking Create Product, app redirects to products list page
        wait.until(ExpectedConditions.urlContains("/admin/products"));
        return new ProductsAdminPage(driver);

    }
    public void  uploadBannerImage(String filePath) {
        // Use the BasePage function directly!
        uploadFileAndWait(btn_File_Img, filePath, img_Banner);
    }

}
