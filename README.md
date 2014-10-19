Product Catalog for the Mobile POS Back-End
===========================================
Project: http://nordpos.mobi

## Release features
* [1.0.0](https://github.com/nordpos-mobi/product-catalog/releases/tag/1.0.0):  
  * create a Maven web application project based on Stripes Framework version 1.5.8;
  * implement version 1.4.4 of jQuery Mobile for UI;
  * persist all data from the database by jdbc from ORMLite library version 4.48;
  * realize security by Public and nonPublic ActionBean and authentication by the user password;
  * create a form for registration new users;
  * create, read, update and delete the product category;
  * generate 4-digits code of the product category by size of the product catalog;
  * create, read, update and delete the product;
  * generate EAN-13 barcode of the product;
  * calculate the all prices in BigDecimal type of the numbers;
  * draw EAN-13 barcode image by code of the product;
  * plot a pie chart for the structure of product price;
  * implement localization filter;
  * create resources for English and Russian languages.

## Installation guide
[Install servlet container](https://github.com/nordpos-mobi/product-catalog/wiki/Install-servlet-container)

[Set context parameters](https://github.com/nordpos-mobi/product-catalog/wiki/Set-context-parameters)

[Upgrade application database](https://github.com/nordpos-mobi/product-catalog/wiki/Upgrade-application-database)

## System requirements
Java virtual machine: Java Oracle Corporation 1.7(**tested**)

Java servlet container: Apache Tomcat 8.0.3(**tested**) and Apache Tomcat 7.0.55(**tested**)

## Included by
[Stripes](http://stripesframework.org) is a simply MVC framework.

[ORMLite](http://ormlite.com/) provides some simple, lightweight functionality for persisting Java objects to SQL databases.

[jQuery Mobile](http://jquerymobile.com/) is a mobile interface library.

[Flot](http://www.flotcharts.org/) is a pure JavaScript plotting library for jQuery.

[JavaEE 6](http://www.oracle.com/technetwork/java/javaee/tech/javaee6technologies-1955512.html) Web Specification APIs.

## Powered by
[Openbravo Java POS](https://code.openbravo.com/pos/devel/main/)(**tested**)

## Demo
URL: http://nordpos.mobi/product-catalog/
User name: DemoUser
User password: demopassword

## Screenshots
Samsung Galaxy Y GT-S5360 Android v.2.3.6

![Login screen](https://cloud.githubusercontent.com/assets/1005780/4693219/7af6f184-578f-11e4-8c14-ecbbb973f5b9.png)![en-samsung-gt-s5360-android-2 3 6-2](https://cloud.githubusercontent.com/assets/1005780/4693231/e20dd630-578f-11e4-84b9-fb6660cc5d8b.png)![en-samsung-gt-s5360-android-2 3 6-3](https://cloud.githubusercontent.com/assets/1005780/4693232/ec48030a-578f-11e4-87ca-5d3c1bacf227.png)![en-samsung-gt-s5360-android-2 3 6-9](https://cloud.githubusercontent.com/assets/1005780/4693240/474cc7e0-5790-11e4-93ed-d6a94c5b597c.png)

Samsung Galaxy S4 mini Duos GT-I9192 Android v.4.2.2

![en-samsung-gt-i9192-android-4 2 2-4](https://cloud.githubusercontent.com/assets/1005780/4693228/cbc74118-578f-11e4-80a7-f1c6fa296f46.png)
![en-samsung-gt-i9192-android-4 2 2-6](https://cloud.githubusercontent.com/assets/1005780/4693241/623c5976-5790-11e4-83c9-1fc22b2a097d.png)

## License
The Product Catalog for the Point of Sale Back-End is licensed under the Apache License, Version 2.0 ([ALv2](http://www.apache.org/licenses/LICENSE-2.0.html)).
