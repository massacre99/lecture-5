package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        driver.get(Properties.getBaseUrl());
        actions.waitForContentLoad();
        Assert.assertEquals(actions.isMobileSiteVersion(), isMobileTesting);
        CustomReporter.logAction(String.format("Navigate to %s site version", isMobileTesting ? "Mobile" : "Desktop"));
    }

    @Test(dependsOnMethods = "checkSiteVersion")
    public void createNewOrder() {
        actions.openRandomProduct();
        ProductData selectedProduct = actions.getOpenedProductInfo();
        ProductData inCartProduct = actions.addToCardAndCheckProduct();

        Assert.assertEquals(selectedProduct.getName(), inCartProduct.getName(), "Names not equals!");
        Assert.assertEquals(selectedProduct.getPrice(), inCartProduct.getPrice(), "Prices not equals");
        Assert.assertEquals(1, inCartProduct.getQty(), "Quantity not equals");

        CustomReporter.logAction("Names, Quantity and Prices are equals in shop and cart");

        ProductData confirmedProduct = actions.confirmOrder();

        Assert.assertTrue(actions.getComfTitle().contains("заказ подтверждён"), "Order not confirmed");
        Assert.assertTrue(confirmedProduct.getName().startsWith(selectedProduct.getName()), "Not my product");

        Assert.assertEquals(selectedProduct.getPrice(), confirmedProduct.getPrice(), "Prices not equals");
        Assert.assertEquals(1, confirmedProduct.getQty(), "Quantity not equals");

        CustomReporter.logAction("Names, Quantity and Prices are equals in shop and confirm order form");

        driver.get(actions.getProductURL());

        ProductData updSelectedProduct = actions.getOpenedProductInfo();

        Assert.assertTrue(selectedProduct.getQty() - updSelectedProduct.getQty() == 1, "SMTH going wrong, must be 1");

    }

}
