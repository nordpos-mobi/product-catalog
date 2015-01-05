Product Catalog for the Mobile POS Back-End
===========================================
Project: http://nordpos.mobi

## Release and Bug fixes ##
* [1.2.0](https://github.com/nordpos-mobi/product-catalog/releases/tag/1.2.0):
  * DAO layer moved to [mobi.nordpos.dao.*](https://github.com/nordpos-mobi/nordpos-dao) packages and changed structure of the source code;
  * fixed critical bug "Not close connection after get foreign collection.";
  * change the image of login user in view panel; 
  * upgraded jQuery Mobile to version 1.4.5; 
  * set  by default Apache Derby JDBC parameters;
  * and more other.
* [1.1.1](https://github.com/nordpos-mobi/product-catalog/releases/tag/1.1.1):
  * check the image of the product and category, that it is not null;
  * remove a dot in the values of the layout component name;
  * update the dependencies and resource properties for enable slf4j logger.
* [1.1.0](https://github.com/nordpos-mobi/product-catalog/releases/tag/1.1.0):
  * upload images for the icons of product categories;
  * upload and preview images of the product;
  * one source code of a Java servlete for [NORD POS](http://github.com/nordpos/nordpos), [Openbravo POS](http://sourceforge.net/projects/openbravopos/), [uniCenta oPOS](http://sourceforge.net/projects/unicentaopos/) and [Wanda POS](https://sourceforge.net/projects/wandaposdapos/) applications;
  * replace Java library barcode4j to jQuery plug-in for render EAN-13 barcode;
  * expanded scope of the public pages;
  * mailto.address and tel.number button links of footer set in the localization resources.
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

## Download
http://sourceforge.net/projects/nordpos/files/product-catalog/

## Forum
http://sourceforge.net/p/nordpos/discussion/product-catalog/

## Wiki 
[Installation guide](https://github.com/nordpos-mobi/product-catalog/wiki)

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
[NORD POS](http://github.com/nordpos/nordpos)(**tested**) [Openbravo Java POS](http://sourceforge.net/projects/openbravopos/)(**tested**) [uniCenta oPOS](http://sourceforge.net/projects/unicentaopos/)(**tested**) [Wanda POS](http://sourceforge.net/projects/wandaposdapos/)(**tested**)

## Demo
URL: http://nordpos.mobi/product-catalog/
Name: **DemoUser** Password: **demopassword**

## Screenshots
Samsung Galaxy Y GT-S5360 Android v.2.3.6

![Login screen](https://cloud.githubusercontent.com/assets/1005780/4693219/7af6f184-578f-11e4-8c14-ecbbb973f5b9.png) ![Product categories](https://cloud.githubusercontent.com/assets/1005780/4978221/f6c3e136-68ed-11e4-81e0-19a35ffd018e.png) ![Product image preview](https://cloud.githubusercontent.com/assets/1005780/4978222/f6cd5360-68ed-11e4-9fd7-d9eb077af408.png)

![Edit product](https://cloud.githubusercontent.com/assets/1005780/4693240/474cc7e0-5790-11e4-93ed-d6a94c5b597c.png) ![Product barcode](https://cloud.githubusercontent.com/assets/1005780/4693256/4e7e904c-5791-11e4-94c9-a23497b7f48f.png) ![Price pie chart](https://cloud.githubusercontent.com/assets/1005780/4693257/4e83a2d0-5791-11e4-8dc7-73da1aa19ddb.png)

Samsung Galaxy S4 mini Duos GT-I9192 Android v.4.2.2

![Poducts list](https://cloud.githubusercontent.com/assets/1005780/4693228/cbc74118-578f-11e4-80a7-f1c6fa296f46.png)

## License
The Product Catalog for the Point of Sale Back-End is licensed under the Apache License, Version 2.0 ([ALv2](http://www.apache.org/licenses/LICENSE-2.0.html)).
