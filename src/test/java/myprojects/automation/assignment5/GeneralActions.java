package myprojects.automation.assignment5;


import com.github.javafaker.Faker;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExec;
    private String comfTitle;
    private String productURL;

    //frontend site
    private By allProductsLink = By.cssSelector(".all-product-link");

    //all products page
    private By searchField = By.className("ui-autocomplete-input");
    private By productLinks = By.cssSelector(".product-title > a");
    private By submitSearch = By.xpath("//button[@type='submit']");

    //current product page
    private By productDetails = By.xpath("//*[@href='#product-details']");
    private By productQuantity = By.cssSelector(".product-quantities > span");
    private By isProductAvailable = By.id("product-availability");
    private By productPrice = By.cssSelector("span[content]");
    private By addToCartButton = By.cssSelector(".add-to-cart");
//    private By addToCard = By.xpath("//*[@data-button-action='add-to-cart']");
    private By goToShopCartButton = By.cssSelector("a.btn-primary");

    //cart page
    private By nameInCart = By.cssSelector(".product-line-info > a");
    private By priceInCart = By.cssSelector(".product-price"); //.product-price > strong
    private By quantityInCart = By.cssSelector(".form-control");
    private By confirmInCart = By.cssSelector("a.btn-primary");

    //confirm order
    private By firstname = By.name("firstname");
    private By lastname = By.name("lastname");
    private By email = By.name("email");
    private By nextButton1 = By.xpath("//*[@data-link-action='register-new-customer']");
    private By address = By.name("address1");
    private By postcode = By.name("postcode");
    private By city = By.name("city");
    private By nextButton2 = By.name("confirm-addresses");
    private By nextButton3 = By.name("confirmDeliveryOption");
    private By paymentOption2 = By.id("payment-option-2");
    private By confirmCheckbox = By.xpath("//*[@id='conditions_to_approve[terms-and-conditions]']");
    private By confirmButton = By.cssSelector("#payment-confirmation button");

    //success confirmation form
    private By successTitle = By.cssSelector(".h1.card-title");
    private By successName = By.cssSelector("div.details");
    private By successPrice = By.cssSelector("div.text-xs-left");
    private By successQuantity = By.cssSelector("div.col-xs-2");




    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
        jsExec = (JavascriptExecutor) driver;
    }

    public void openRandomProduct() {
        driver.get(Properties.getBaseUrl());
        waitForContentLoad();
//        driver.findElement(allProductsLink).click();
        scrollToElement(driver.findElement(allProductsLink)).click();
        List<WebElement> productsElements = driver.findElements(productLinks);
        WebElement product = productsElements.get(new Random().nextInt(productsElements.size()));
        scrollToElement(product).click();
//        product.click();
//        wait.until(ExpectedConditions.elementToBeClickable(product))
//                .click();
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
        public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        waitForContentLoad();

        String name = driver.getTitle();
        float price = DataConverter.parsePriceValue(driver.findElement(productPrice).getText());
//                Float.parseFloat(
//                driver.findElement(productPrice)
//                        .getAttribute("content"));
        int quantity;
        String isStock = driver.findElement(isProductAvailable).getText();

        if (isStock.contains("Нет в наличии")) {
            quantity = 0;
            CustomReporter.logAction("The product is not available, searching for another");
            openRandomProduct();
            getOpenedProductInfo();
        }
        else {
            scrollToElement(driver.findElement(productDetails)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(productQuantity));
            quantity = DataConverter.parseStockValue(driver.findElement(productQuantity).getText());
//            quantity = Integer.parseInt(
//                    driver.findElement(productQuantity)
//                            .getText().replaceAll("[\\D]", "").trim());

            productURL = driver.getCurrentUrl();
            CustomReporter.logAction("Product name - " + name);
            CustomReporter.logAction(String.format("Product URL - %s", productURL));
        }

        return new ProductData(name, quantity, price);
    }

    public ProductData addToCardAndCheckProduct() {
        scrollToElement(driver.findElement(addToCartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(goToShopCartButton)).click();
        waitForContentLoad();

        String name = driver.findElement(nameInCart).getText();
        float price = DataConverter.parsePriceValue(driver.findElement(priceInCart).getText());
        int quantity = Integer.parseInt(
                driver.findElement(quantityInCart).getAttribute("value"));

        return new ProductData(name, quantity, price);
    }

    /**
     * http://dius.github.io/java-faker/
     */
    public ProductData confirmOrder() {
        Faker fakerRu = new Faker(new Locale("ru", "ru"));
        Faker fakerUa = new Faker(new Locale("uk", "ua"));
        Faker fakerEn = new Faker(new Locale("en", "us"));
        scrollToElement(driver.findElement(confirmInCart)).click();

        waitForContentLoad();
        scrollToElement(driver.findElement(firstname)).sendKeys(fakerRu.name().firstName());
        scrollToElement(driver.findElement(lastname)).sendKeys(fakerRu.name().lastName());
        scrollToElement(driver.findElement(email)).sendKeys(fakerEn.internet().emailAddress());
        scrollToElement(driver.findElement(nextButton1)).click();

        waitForContentLoad();
        scrollToElement(driver.findElement(address)).sendKeys(fakerUa.address().fullAddress());
        scrollToElement(driver.findElement(postcode)).sendKeys(fakerUa.address().zipCode());
        scrollToElement(driver.findElement(city)).sendKeys(fakerUa.address().cityName());
        scrollToElement(driver.findElement(nextButton2)).click();

        waitForContentLoad();
        scrollToElement(driver.findElement(nextButton3)).click();

        waitForContentLoad();
        scrollToElement(driver.findElement(paymentOption2)).click();
        scrollToElement(driver.findElement(confirmCheckbox)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        scrollToElement(driver.findElement(confirmButton)).click();

        waitForContentLoad();
        String title = driver.findElement(successTitle).getText().toLowerCase();
        comfTitle = title;
        String name = driver.findElement(successName).getText();
        float price = DataConverter.parsePriceValue(driver.findElement(successPrice).getText());
        int quantity = Integer.parseInt(driver.findElement(successQuantity).getText());

        return new ProductData(name, quantity, price);
    }

    // $('element').is(':visible')
    public boolean isMobileSiteVersion() {
        return (boolean) jsExec.executeScript("return  $('#_mobile_logo').is(':visible')");
    }

    /**
     * Scrolls to element and returns him.
     */
    public WebElement scrollToElement(WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView();", element);
//        jsExec.executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    public void waitForContentLoad() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        Boolean isReady = false;
        while (!isReady) {
            isReady = executor.executeScript("return document.readyState").equals("complete");
        }
    }

    public String getComfTitle() {
        return comfTitle.toLowerCase();
    }

    public String getProductURL() {
        return productURL;
    }

}
