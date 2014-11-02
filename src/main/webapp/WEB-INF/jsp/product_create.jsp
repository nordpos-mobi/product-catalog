<%--
    Document   : product_create
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network
    License    : Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="Product Create"
                       pageid="ProductCreate">

    <stripes:layout-component name="button.return">
        <sdynattr:link href="/Welcome.action"
                       class="ui-btn ui-corner-all ui-icon-home ui-btn-icon-notext">
            <fmt:message key="label.home" />
        </sdynattr:link>                  
        <sdynattr:link href="/CategoryProductList.action"
                       class="ui-btn ui-corner-all ui-icon-bars ui-btn-icon-left">
            <stripes:param name="category.id" value="${actionBean.product.productCategory.id}"/>
            <c:out value="${actionBean.product.productCategory.name}"/>
        </sdynattr:link>                   
    </stripes:layout-component>

    <stripes:layout-component name="header.title">
        <fmt:message key="label.ProductCreate" />
    </stripes:layout-component>

    <stripes:layout-component name="button.action">
        
    </stripes:layout-component>

    <stripes:layout-component name="content">
        <sdynattr:form action="/ProductCreate.action" data-ajax="false">
            <div>                
                <stripes:hidden name="product.productCategory.id" value="${actionBean.product.productCategory.id}"/>
                <stripes:hidden name="product.productCategory.name" value="${actionBean.product.productCategory.name}"/>
            </div>
            <ul data-role="listview" data-inset="true">                
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.name" for="productName" />
                    <input name="product.name" id="productName" type="text"
                           placeholder="<fmt:message key='label.ProductName.enter' />" 
                           value=""
                           data-clear-btn="true">
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.reference" for="productReference" />
                    <input name="product.reference" id="productReference" type="text"
                           placeholder="<fmt:message key='label.ProductReference.enter' />"
                           value="${actionBean.product.reference}"
                           data-clear-btn="true">
                </li>                
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.code" for="productCode" />
                    <input name="product.code" id="productCode" type="text"
                           placeholder="<fmt:message key='label.ProductCode.enter' />"
                           value="${actionBean.product.code}"
                           data-clear-btn="true">
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.priceBuy" for="productPriceBuy"/>
                    <input name="product.priceBuy" id="productPriceBuy" type="number"
                           placeholder="<fmt:message key='label.ProductBuyPrice.enter' />"
                           step="0.01"                           
                           value="0.00"
                           data-clear-btn="true">
                </li>                
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.priceSell" for="productPriceSell"/>
                    <input name="product.priceSell" id="productPriceSell" type="number"
                           placeholder="<fmt:message key='label.ProductSellPrice.enter' />"
                           step="0.01"                           
                           value="0.00"
                           data-clear-btn="true">
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.taxCategory" for="productTaxCategory" class="select"/>
                    <sdynattr:select name="product.taxCategory.id" id="productTaxCategory">
                        <c:forEach items="${actionBean.taxCategoryList}" var="taxCategory">
                            <stripes:option value="${taxCategory.id}">
                                <c:out value="${taxCategory.name}"/>
                            </stripes:option>
                        </c:forEach>
                    </sdynattr:select>
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.Product.taxInclude" for="productIsTaxInclude"/>
                    <sdynattr:select name="isTaxInclude" id="productIsTaxInclude" data-role="slider">
                        <stripes:option value="false">
                            <fmt:message key="no"/>
                        </stripes:option>    
                        <stripes:option value="true" selected="true">
                            <fmt:message key="yes"/>
                        </stripes:option> 
                    </sdynattr:select>
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.ProductImage.file" for="productImageFile" />                    
                    <stripes:file name="imageFile" id="productImageFile" /> 
                </li>
                <li class="ui-body ui-body-b">
                    <fieldset class="ui-grid-a">
                        <div class="ui-block-a">
                            <sdynattr:reset name="clear" data-theme="b"/>
                        </div>
                        <div class="ui-block-b">
                            <sdynattr:submit name="add" data-theme="a"/>
                        </div>
                    </fieldset>
                </li>
            </ul>        
        </sdynattr:form>
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
