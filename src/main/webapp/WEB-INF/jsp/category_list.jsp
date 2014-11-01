<%--
    Document   : category_list
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network
    License    : Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="Product Category"
                       pageid="ProductCategory">

    <stripes:layout-component name="button.return">
        <sdynattr:link href="/Welcome.action"
                       class="ui-btn ui-shadow ui-corner-all ui-icon-home ui-btn-icon-notext">            
            <fmt:message key="label.home" />
        </sdynattr:link>          
    </stripes:layout-component>

    <stripes:layout-component name="header.title">
        <fmt:message key="label.Categories"/>
    </stripes:layout-component>

    <stripes:layout-component name="button.action">
        <sdynattr:link href="/CategoryCreate.action"                       
                       class="ui-btn ui-shadow ui-corner-all ui-icon-action ui-btn-icon-left">
            <stripes:param name="category.code" value="${actionBean.categoryList.size() + 1}"/>
            <fmt:message key="label.create" />
        </sdynattr:link>          
    </stripes:layout-component>

    <%-- Main content of the page implementation to the template for view --%>
    <stripes:layout-component name="content">        
        <%-- Errors after run action --%>
        <stripes:errors />
        <%-- Messages about completed action --%>
        <stripes:messages />
        <%-- Listview include search by the name of elements --%>
        <ul data-role="listview" data-filter="true"
            data-filter-placeholder="<fmt:message key='label.ProductCategory.search' />"
            data-inset="true" data-split-icon="edit" data-split-theme="a">
            <%-- Create dynamic listview. --%>
            <c:forEach items="${actionBean.categoryList}" var="category">                
                <li>
                    <%-- Link to view the selected child elements of the list --%>
                    <sdynattr:link href="/CategoryProductList.action"
                                   data-transition="slide">
                        <%-- Key parameter for search child elements --%>
                        <stripes:param name="category.id" value="${category.id}"/>
                        <image src="${pageContext.servletContext.contextPath}/CategoryImage.action?preview&category.id=${category.id}&thumbnailSize=80" />
                        <%-- Name of the list item --%>
                        <h2><c:out value="${category.name}"/></h2>
                        <p><c:out value="${category.code}"/></p>
                        <%-- Addional information about number of child elements --%>
                        <span class="ui-li-count"><c:out value="${category.productList.size()}"/></span>
                    </sdynattr:link>
                    <%-- Link to change the current list item --%>
                    <sdynattr:link href="/CategoryChange.action"
                                   data-transition="slide">
                        <%-- Key parameter for changes of the current list item --%>
                        <stripes:param name="category.id" value="${category.id}"/>
                        <%-- Description of the action by the current list item --%>
                        <fmt:message key="label.edit" />
                    </sdynattr:link>
                </li>
            </c:forEach>
        </ul>
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
