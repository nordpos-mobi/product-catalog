<%--
    Document   : login
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network.
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="Home"
                       pageid="home">

    <stripes:layout-component name="button.return">
    </stripes:layout-component>

    <stripes:layout-component name="header.title">
    </stripes:layout-component>

    <stripes:layout-component name="button.action">
        <sdynattr:link href="/UserRegistration.action"
                       class="ui-btn ui-corner-all ui-icon-lock ui-btn-icon-left"
                       role="button">
            <stripes:label name="label.register" />
        </sdynattr:link>
    </stripes:layout-component>

    <stripes:layout-component name="content">
        <div class="ui-body">            
            <a href="http://www.nordpos.mobi">
                <img src="<c:url value='/image/logo.png' />" alt="NORD POS mobi"/>
            </a>
            <stripes:errors />
            <stripes:form action="/UserAuthorization.action">
                <div class="ui-field-contain">
                    <stripes:label name="label.login.name"
                                   for="loginName" />
                    <input type="text"
                           name="user.name"
                           id="loginName"
                           data-clear-btn="true"
                           placeholder="${actionBean.getLocalizationKey("label.LoginName.enter")}"
                           value=""
                           />
                </div>
                <div class="ui-field-contain">
                    <stripes:label name="label.login.password"
                                   for="loginPassword" />
                    <input type="password"
                           name="user.password"
                           id="loginPassword"
                           data-clear-btn="true"
                           placeholder="${actionBean.getLocalizationKey("label.LoginPassword.enter")}"
                           value=""/>
                </div>            
                <stripes:hidden name="targetUrl" />
                <stripes:submit name="login"/>
            </stripes:form>
        </div>
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
