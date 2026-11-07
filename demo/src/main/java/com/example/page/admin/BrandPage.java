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

public class BrandPage extends BasePage {

        // some field not change
        private By btnAdd = By.cssSelector("button[data-testid='add-brand-button']");
        private By BrandRows = By.cssSelector(
                        "tr[data-testid^='brand-row']");

        public BrandPage(WebDriver driver) {
                super(driver);
        }

        public void clickAdd() {
                presenceOfElementLocated(btnAdd).click();
                waitForVisible(By.cssSelector("h2[data-testid='brand-form-title']"));
        }

        public void newBrand(Brand data) {
                clickAdd();
                presenceOfElementLocatedSendData(By.cssSelector("input[data-testid='brand-name-input']"),
                                data.getName());
                presenceOfElementLocatedSendData(By.cssSelector("textarea[data-testid='brand-description-textarea']"),
                                data.getDescription());
                presenceOfElementLocated(By.cssSelector("button[data-testid='brand-form-submit']")).click();
                // here we need wait until -> the element show up in table ->

                String raw = "//tr[contains(@data-testid,'brand-row')] //p[contains(@data-testid,'brand-name') and contains(text(),'%s')]";
                String getThisString = String.format(raw, data.getName());
                waitForVisible(By.xpath(getThisString));
        }
        // ==================== BRAND COUNT ====================

        public int getCateCount() {

                // Wait for the table to be available
                wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                                By.cssSelector("tbody")));

                return driver.findElements(BrandRows).size();
        }

        private int findBrandIdByName(String name) {

                WebDriverWait wait = new WebDriverWait(
                                driver, Duration.ofSeconds(10));

                WebElement row = wait.until(d -> {
                        List<WebElement> rows = d.findElements(BrandRows);

                        for (WebElement r : rows) {
                                String actualName = r.findElement(
                                                By.xpath(".//p[1]")).getText().trim();

                                if (actualName.equalsIgnoreCase(name.trim())) {
                                        return r;
                                }
                        }

                        return null;
                });

                String testId = row.getAttribute("data-testid");

                return Integer.parseInt(
                                testId.substring(testId.lastIndexOf('-') + 1));
        }

        public boolean checkNewBrand(Brand data, int countBefore) {

                // Wait until the category count increases
                wait.until(
                                ExpectedConditions.numberOfElementsToBeMoreThan(
                                                BrandRows,
                                                countBefore));

                int countAfter = getCateCount();

                if (countAfter != countBefore + 1) {

                        System.err.printf(
                                        "COUNT ERROR | Expected: %d | Actual: %d%n",
                                        countBefore + 1,
                                        countAfter);

                        return false;
                }

                int newBrandId = findBrandIdByName(data.getName());

                if (newBrandId == -1) {

                        System.err.println(
                                        "CATEGORY NOT FOUND | Name: '" + data.getName() + "'");

                        return false;
                }

                String row = "//tr[@data-testid='brand-row-" + newBrandId + "']";

                String actualName = waitForVisible(
                                By.xpath(row + "//p[1]")).getText().trim();

                if (!actualName.equalsIgnoreCase(data.getName().trim())) {

                        System.err.printf(
                                        "NAME ERROR | Expected: '%s' | Actual: '%s'%n",
                                        data.getName(),
                                        actualName);

                        return false;
                }

                String actualDescription = waitForVisible(
                                By.xpath(
                                                row + "//span[@data-testid='brand-description-" +
                                                                newBrandId + "']"))
                                .getText().trim();

                if (!actualDescription.equalsIgnoreCase(
                                data.getDescription().trim())) {

                        System.err.printf(
                                        "DESCRIPTION ERROR | Expected: '%s' | Actual: '%s'%n",
                                        data.getDescription(),
                                        actualDescription);

                        return false;
                }

                return true;
        }

        public void editBrand(Brand data, String Name) {
                int id = findBrandIdByName(Name);
                if (id == -1) {
                        System.out.print(" The id we not found -> or not match ");
                        return;
                }
                // because we attach specific -> field in each element -> not need use xpath to
                // find
                String addressEditBtn = "button[data-testid='edit-brand-button-" + id + "']";
                waitForVisible(By.cssSelector(addressEditBtn)).click();
                waitForVisible(By.cssSelector("h2[data-testid='brand-form-title']"));
                visiableClearThenSendData(By.cssSelector("input[data-testid='brand-name-input']"), data.getName());
                visiableClearThenSendData(By.cssSelector("textarea[data-testid='brand-description-textarea']"),
                                data.getDescription());
                waitForVisible(By.cssSelector("button[data-testid='brand-form-submit']")).click();
                // we are check again to ensure -> the name we just edit -> exist in table
                String raw = "//tr[contains(@data-testid,'brand-row')] //p[contains(@data-testid,'brand-name') and contains(text(),'%s')]";
                String getThisString = String.format(raw, data.getName());
                waitForVisible(By.xpath(getThisString));
        }

        public boolean check_info_edit(Brand data) {
                wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                                By.cssSelector("tbody")));

                int idEdit = findBrandIdByName(data.getName());

                if (idEdit == -1) {

                        System.err.println(
                                        "CATEGORY NOT FOUND | Name: '" + data.getName() + "'");

                        return false;
                }

                String row = "//tr[@data-testid='brand-row-" + idEdit + "']";

                String actualName = waitForVisible(
                                By.xpath(row + "//p[1]")).getText().trim();

                if (!actualName.equalsIgnoreCase(data.getName().trim())) {

                        System.err.printf(
                                        "NAME ERROR | Expected: '%s' | Actual: '%s'%n",
                                        data.getName(),
                                        actualName);

                        return false;
                }
                String actualDescription = waitForVisible(
                                By.xpath(
                                                row + "//span[@data-testid='brand-description-" +
                                                                idEdit + "']"))
                                .getText().trim();

                if (!actualDescription.equalsIgnoreCase(
                                data.getDescription().trim())) {

                        System.err.printf(
                                        "DESCRIPTION ERROR | Expected: '%s' | Actual: '%s'%n",
                                        data.getDescription(),
                                        actualDescription);

                        return false;
                }

                return true;
        }

        public boolean delete(String name) {
                
                        int id = findBrandIdByName(name);
                        String btnDelete = "button[data-testid='delete-brand-button-" + id + "']";
                        waitForVisible(By.cssSelector(btnDelete)).click();
                        waitForVisible(By.xpath("//h2[contains(text(),'Delete Brand?')]"));
                        waitForClickable(By.xpath("//button[contains(text(),'Delete')]")).click();
                        String raw = "//tr[contains(@data-testid,'brand-row')] //p[contains(@data-testid,'brand-name') and contains(text(),'%s')]";
                        String getThisString = String.format(raw, name);
                try
                {
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(getThisString)));
                        return true;
                }catch(Exception e)
                {
                        return false;
                }
        }
}
