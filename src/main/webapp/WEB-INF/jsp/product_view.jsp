<%--
    Document   : product_view
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network
    License    : Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="Product View"
                       pageid="ProductView">

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
        <c:out value="${actionBean.product.name}"/>
    </stripes:layout-component>

    <stripes:layout-component name="button.action">
        <sdynattr:link href="/ProductChange.action"
                       class="ui-btn ui-corner-all ui-icon-edit ui-btn-icon-left"
                       role="button">
            <stripes:param name="product.id" value="${actionBean.product.id}"/>
            <fmt:message key="label.edit" />
        </sdynattr:link>        
    </stripes:layout-component>

    <stripes:layout-component name="content">
        <stripes:errors />
        <stripes:messages />
        <div data-role="collapsible" data-collapsed-icon="carat-d" data-expanded-icon="carat-u" data-collapsed="false">
            <h2><fmt:message key="label.ProductImage"/></h2>
            <div class="ui-grid-solo">
                <div class="ui-block-a">
                    <img src="${pageContext.servletContext.contextPath}/ProductImage.action?preview&product.id=${actionBean.product.id}"
                         alt="${actionBean.product.name}" />
                </div>
            </div>
        </div>

        <div data-role="collapsible" data-collapsed-icon="carat-d" data-expanded-icon="carat-u" data-collapsed="true">
            <h2><fmt:message key="label.ProductGeneralInfo"/></h2>
            <div class="ui-grid-a ui-responsive">  
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.name" />
                    </div>                    
                </div>                    
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <c:out value="${actionBean.product.name}"/>
                    </div>                    
                </div>
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.reference" />
                    </div>                    
                </div>                    
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <c:out value="${actionBean.product.reference}"/>
                    </div>                    
                </div>
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.priceBuy" />
                    </div>                    
                </div>
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <fmt:formatNumber value="${actionBean.product.priceBuy}"
                                          type="CURRENCY"
                                          pattern="#0.00 ¤"                                                  
                                          maxFractionDigits="2" 
                                          minFractionDigits="2"/>
                    </div>                    
                </div>                
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.priceSell" />
                    </div>                    
                </div>
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <fmt:formatNumber value="${actionBean.product.priceSell}"
                                          type="CURRENCY"
                                          pattern="#0.00 ¤"                                                  
                                          maxFractionDigits="2" 
                                          minFractionDigits="2"/>
                    </div>                    
                </div>
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.taxCategory" />
                    </div>                    
                </div>                    
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <c:out value="${actionBean.product.taxCategory.name}"/>
                    </div>                    
                </div>                 
                <div class="ui-block-a">
                    <div class="ui-body ui-body-d">
                        <fmt:message key="label.Product.taxPriceSell" />
                    </div>                    
                </div>                    
                <div class="ui-block-b">
                    <div class="ui-body ui-body-d">
                        <fmt:formatNumber value="${actionBean.product.taxPriceSell}"
                                          type="CURRENCY"
                                          pattern="#0.00 ¤"                                                  
                                          maxFractionDigits="2" 
                                          minFractionDigits="2"/>
                    </div>                    
                </div>                    
            </div>
            <div>                
                <canvas id="ean" width="256" height="128"></canvas>  
                <script type="text/javascript">
                    $("#ean").EAN13("${actionBean.product.code}");</script>
            </div>
        </div>
        <div data-role="collapsible" data-collapsed-icon="carat-d" data-expanded-icon="carat-u">
            <script type="text/javascript">
                $(document).ready(function () {
                    var data = [
                        {label: "<fmt:message key='label.ProductPriceCost'/>", data: ${actionBean.product.priceBuy}},
                        {label: "<fmt:message key='label.ProductPriceMargin'/>", data: ${actionBean.product.priceSell.subtract(actionBean.product.priceBuy)}},
                        {label: "<fmt:message key='label.ProductPriceTax'/>", data: ${actionBean.product.taxPriceSell.subtract(actionBean.product.priceSell)}}];
                    var placeholder = $("#price_pie_chart");
                    $.plot(placeholder, data, {
                        series: {
                            pie: {
                                show: true
                            }
                        },
                        legend: {
                            show: false
                        },
                        grid: {
                            hoverable: true,
                            clickable: true
                        }
                    });
                    placeholder.bind("plotclick", function (event, pos, obj) {
                        if (!obj) {
                            return;
                        }
                        var value = parseFloat(obj.series.data[0][1]).toFixed(2);
                        alert("" + obj.series.label + " = " + value);
                    });
                });
            </script>
            <h2><fmt:message key="label.ProductPricePieChart" /></h2>
            <div style="padding: 10px;">
                <div id="price_pie_chart" style="width:256px;height:256px"></div>
            </div>
        </div>            
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
