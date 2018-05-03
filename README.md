# prestashop-t5

Adds random product in card, fills address and purchase it.
Next checks this product quantity changed by one.

Runs on Chrome, Headless Chrome, Mobile Chrome, FF, IE.

Run without Grid: mvn clean test -PSeleniumTests

Run with Grid:

1. Start Grid Server start-hub.cmd

2. Start Node start-node.cmd

3. mvn clean test -PSelenuimGridTests

