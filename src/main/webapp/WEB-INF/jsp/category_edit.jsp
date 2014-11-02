<%--
    Document   : category_edit
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network
    License    : Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="Product Category Edit"
                       pageid="ProductCategoryEdit">

    <stripes:layout-component name="button.return">
        <sdynattr:link href="/Welcome.action"
                       class="ui-btn ui-corner-all ui-icon-home ui-btn-icon-notext">
            <fmt:message key="label.home" />
        </sdynattr:link>          
        <sdynattr:link href="/CategoryList.action"
                       class="ui-btn ui-corner-all ui-icon-bars ui-btn-icon-left">
            <fmt:message key="label.Categories" />
        </sdynattr:link>           
    </stripes:layout-component>

    <stripes:layout-component name="header.title">
        <fmt:message key="label.ProductCategoryEdit" />
    </stripes:layout-component>

    <stripes:layout-component name="button.action">

        <a href="#delete_category" 
           data-rel="popup" 
           data-position-to="window" 
           data-transition="pop" 
           class="ui-btn ui-corner-all ui-icon-delete ui-btn-icon-left ui-btn-b ui-shadow">
            <fmt:message key="label.delete" />
        </a>
        <div data-role="popup" 
             id="delete_category" 
             data-overlay-theme="b" data-theme="b" 
             data-dismissible="false" style="max-width:400px;">
            <div data-role="header" data-theme="a">
                <h1><fmt:message key="label.dialog.delete" /></h1>
            </div>
            <div role="main" class="ui-content">
                <h3 class="ui-title">
                    <c:out value="${actionBean.category.name}"/>
                </h3>
                <p><fmt:message key="label.ask.delete" /></p>
                <fieldset class="ui-grid-a">
                    <div class="ui-block-a">
                        <a href="#" 
                           class="ui-btn ui-corner-all ui-icon-forbidden ui-btn-icon-left ui-btn-b ui-shadow" 
                           data-rel="back" 
                           data-transition="flow">
                            <fmt:message key="no" />
                        </a>  
                    </div>
                    <div class="ui-block-b">
                        <stripes:form action="/CategoryChange.action?delete">
                            <div>
                                <stripes:hidden name="category.id" value="${actionBean.category.id}"/>                    
                            </div>
                            <sdynattr:submit name="yes" data-theme="a" data-icon="check"/>                    
                        </stripes:form>
                    </div>
                </fieldset>
            </div>
        </div>        

    </stripes:layout-component>


    <stripes:layout-component name="content">        
        <stripes:errors />
        <stripes:messages />           
        <sdynattr:form action="/CategoryChange.action?update" data-ajax="false">
            <div>
                <stripes:hidden name="category.id" value="${actionBean.category.id}"/>
                <stripes:hidden name="currentCategory.name" value="${actionBean.category.name}"/>
                <stripes:hidden name="currentCategory.code" value="${actionBean.category.code}"/>
            </div>
            <ul data-role="listview" data-inset="true">  
                <li class="ui-field-contain">
                    <stripes:label name="label.ProductCategory.name" for="categoryName" />
                    <input name="category.name" id="categoryName" type="text"
                           placeholder="<fmt:message key='label.ProductCategoryName.enter' />" 
                           value="${actionBean.category.name}"
                           data-clear-btn="true">
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.ProductCategory.code" for="categoryCode" />
                    <input name="category.code" id="categoryCode" type="text"
                           placeholder="<fmt:message key='label.ProductCategoryCode.enter' />" 
                           value="${actionBean.category.code}"
                           data-clear-btn="true">
                </li>
                <li class="ui-field-contain">
                    <stripes:label name="label.ProductCategoryImage.file" for="categoryImageFile" />                    
                    <stripes:file name="imageFile" id="categoryImageFile" /> 
                </li>
                <li class="ui-body ui-body-b">
                    <fieldset class="ui-grid-a">
                        <div class="ui-block-a">
                            <sdynattr:reset name="clear" data-theme="b"/>
                        </div>
                        <div class="ui-block-b">
                            <sdynattr:submit name="update" data-theme="a"/>
                        </div>
                    </fieldset>
                </li>
            </ul>
        </sdynattr:form>
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
