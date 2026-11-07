
package com.example.page.admin;

import com.example.page.*;
import com.example.models.admin.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CategoryAdminPage extends BasePage {

    // ==================== LOCATORS ====================

    private By inputSearch = By.cssSelector(
            "input[data-testid='categories-search-input']");

    private By btnOpenNewCate = By.cssSelector(
            "button[data-testid='add-category-button']");

    private By formModal = By.cssSelector(
            "[data-testid='category-form-modal']");

    private By h2NewCate = By.xpath(
            "//div[@data-testid='category-form-modal']" +
                    "//h2[@data-testid='category-form-title' and contains(text(),'New Category')]");

    private By titleH2 = By.xpath(
            "//div[@data-testid='category-form-modal']" +
                    "//h2[@data-testid='category-form-title' and contains(text(),'Edit Category')]");

    private By inputNewName = By.cssSelector(
            "input[data-testid='category-name-input']");

    private By inputNewSlug = By.cssSelector(
            "input[data-testid='category-slug-input']");

    private By textareaNew = By.cssSelector(
            "textarea[data-testid='category-description-textarea']");

    private By submitNew = By.cssSelector(
            "button[data-testid='category-form-submit']");

    private By deleteTitle = By.xpath(
            "//h2[contains(normalize-space(), 'Delete Category')]");

    private By categoryRows = By.cssSelector(
            "tr[data-testid^='category-row']");

    public CategoryAdminPage(WebDriver driver) {
        super(driver);
    }

    // ==================== CLICK HELPERS ====================

    protected void safeClick(By by) {

        try {
            wait.until(
                    ExpectedConditions.elementToBeClickable(by)).click();

        } catch (ElementClickInterceptedException e) {

            // Re-locate the element before using JavaScript
            WebElement freshElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(by));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    freshElement);
        }
    }

    // ==================== CREATE CATEGORY ====================

    public void clickNew() {

        wait.until(
                ExpectedConditions.elementToBeClickable(btnOpenNewCate)).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(h2NewCate));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(inputNewName));
    }

    public CategoryAdminPage smokeNewCate(NewCate data) {

        clickNew();

        visiableToSendData(inputNewName, data.getName());

        visiableToSendData(inputNewSlug, data.getSlug());

        visiableToSendData(textareaNew, data.getDescription());

        wait.until(
                ExpectedConditions.elementToBeClickable(submitNew)).click();

        // Wait until the create modal disappears
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(formModal));

        return this;
    }

    // ==================== SEARCH CATEGORY ====================

    public boolean smokeCheckSearch(String content) {

        TypeInput(inputSearch, content);

        String lowerContent = content.toLowerCase();

        String current = String.format(
                "//p[contains(@data-testid, 'category-name') " +
                        "and translate(text(), " +
                        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz') = '%s']",
                lowerContent);

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(current)));

            return true;

        } catch (Exception e) {

            System.out.println(
                    "It's not returned: " + content);

            return false;
        }
    }

    // ==================== CATEGORY COUNT ====================

    public int getCateCount() {

        // Wait for the table to be available
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("tbody")));

        return driver.findElements(categoryRows).size();
    }

    // ==================== VERIFY CREATE ====================

    public boolean checkNewCategory(NewCate data, int countBefore) {

        // Wait until the category count increases
        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        categoryRows,
                        countBefore));

        int countAfter = getCateCount();

        if (countAfter != countBefore + 1) {

            System.err.printf(
                    "COUNT ERROR | Expected: %d | Actual: %d%n",
                    countBefore + 1,
                    countAfter);

            return false;
        }

        int newCateId = findCateIdByName(data.getName());

        if (newCateId == -1) {

            System.err.println(
                    "CATEGORY NOT FOUND | Name: '" + data.getName() + "'");

            return false;
        }

        String row = "//tr[@data-testid='category-row-" + newCateId + "']";

        String actualName = waitForVisible(
                By.xpath(row + "//p[1]")).getText().trim();

        if (!actualName.equalsIgnoreCase(data.getName().trim())) {

            System.err.printf(
                    "NAME ERROR | Expected: '%s' | Actual: '%s'%n",
                    data.getName(),
                    actualName);

            return false;
        }

        String actualSlug = waitForVisible(
                By.xpath(row + "//span[@data-testid='category-slug-" + newCateId + "']")).getText().trim();
        System.out.print(actualSlug);
        if (!actualSlug.equalsIgnoreCase(data.getSlug().trim())) {

            System.err.printf(
                    "SLUG ERROR | Expected: '%s' | Actual: '%s'%n",
                    data.getSlug(),
                    actualSlug);

            return false;
        }

        String actualDescription = waitForVisible(
                By.xpath(
                        row + "//span[@data-testid='category-description-" +
                                newCateId + "']"))
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

    // ==================== FIND CATEGORY ID ====================

    private int findCateIdByName(String name) {

    WebDriverWait wait = new WebDriverWait(
        driver, Duration.ofSeconds(10)
    );

    WebElement row = wait.until(d -> {
        List<WebElement> rows = d.findElements(categoryRows);

        for (WebElement r : rows) {
            String actualName = r.findElement(
                By.xpath(".//p[1]")
            ).getText().trim();

            if (actualName.equalsIgnoreCase(name.trim())) {
                return r;
            }
        }

        return null;
    });

    String testId = row.getAttribute("data-testid");

    return Integer.parseInt(
        testId.substring(testId.lastIndexOf('-') + 1)
    );
}

    // ==================== EDIT CATEGORY ====================

    public void checkEdit(NewCate data, String oldName) {

        // Step 1: Find the category ID
        int id = findCateIdByName(oldName);

        if (id == -1) {

            throw new RuntimeException(
                    "Category not found: '" + oldName +
                            "' - make sure it exists before editing");
        }

        // Step 2: Locate the edit button
        By editButton = By.xpath(
                "//tr[@data-testid='category-row-" + id + "']" +
                        "//button[contains(@data-testid, 'edit-category-button')]");

        // Step 3: Click the edit button
        WebElement editBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(editButton));

        // Keep JS here for the specific covered-button issue
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                editBtn);

        // Step 4: Wait for edit modal
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(titleH2));

        // Step 5: Update name
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(inputNewName));

        nameInput.clear();
        nameInput.sendKeys(data.getName());

        // Step 6: Update slug
        WebElement slugInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(inputNewSlug));

        slugInput.clear();
        slugInput.sendKeys(data.getSlug());

        // Step 7: Update description
        WebElement descInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(textareaNew));

        descInput.clear();
        descInput.sendKeys(data.getDescription());

        // Step 8: Submit changes
        wait.until(
                ExpectedConditions.elementToBeClickable(submitNew)).click();

        // Step 9: Wait for edit modal to disappear
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(formModal));
    }

    // ==================== VERIFY EDIT ====================

    public boolean checking_correct_info(NewCate data) {

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("tbody")));

        int idEdit = findCateIdByName(data.getName());

        if (idEdit == -1) {

            System.err.println(
                    "CATEGORY NOT FOUND | Name: '" + data.getName() + "'");

            return false;
        }

        String row = "//tr[@data-testid='category-row-" + idEdit + "']";

        String actualName = waitForVisible(
                By.xpath(row + "//p[1]")).getText().trim();

        if (!actualName.equalsIgnoreCase(data.getName().trim())) {

            System.err.printf(
                    "NAME ERROR | Expected: '%s' | Actual: '%s'%n",
                    data.getName(),
                    actualName);

            return false;
        }

        String actualSlug = waitForVisible(
                By.xpath(row + "//span[@data-testid='category-slug-"+ idEdit+ "']")).getText().trim();

        if (!actualSlug.equalsIgnoreCase(data.getSlug().trim())) {

            System.err.printf(
                    "SLUG ERROR | Expected: '%s' | Actual: '%s'%n",
                    data.getSlug(),
                    actualSlug);

            return false;
        }

        String actualDescription = waitForVisible(
                By.xpath(
                        row + "//span[@data-testid='category-description-" +
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

    // ==================== DELETE CATEGORY ====================

    public boolean Delete(String name) {

        // Step 1: Find category ID
        int id = findCateIdByName(name);

        if (id == -1) {

            throw new RuntimeException(
                    "Category not found: '" + name +
                            "' - make sure it exists before delete");
        }

        By row = By.cssSelector(
                "tr[data-testid='category-row-" + id + "']");

        By deleteButton = By.cssSelector(
                "button[data-testid='delete-category-button-" + id + "']");

        // Step 2: Open delete confirmation modal
        safeClick(deleteButton);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteTitle));

        // Step 3: Locate Delete button inside the confirmation modal
        By confirmDelete = By.xpath(
                "//h2[contains(normalize-space(), 'Delete Category')]" +
                        "/ancestor::div[.//button[normalize-space()='Delete']][1]" +
                        "//button[normalize-space()='Delete']");

        // Step 4: Confirm deletion
        safeClick(confirmDelete);

        // Step 5: Wait until modal disappears
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(deleteTitle));

        // Step 6: Wait until category row disappears
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(row));

        // Step 7: Final verification
        return driver.findElements(row).isEmpty();
    }
}
