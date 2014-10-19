<%--
    Document   : user_view
    Author     : Andrey Svininykh (svininykh@gmail.com)
    Copyright  : Nord Trading Network
    License    : Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)
--%>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<stripes:layout-render name="/WEB-INF/jsp/common/layout_main.jsp"
                       title="User"
                       pageid="User">

    <stripes:layout-component name="button.return">
        <sdynattr:link href="/ApplicationPresent.action"
                       class="ui-btn ui-corner-all ui-icon-home ui-btn-icon-notext">
            <stripes:label name="label.home" />
        </sdynattr:link>        
    </stripes:layout-component>

    <stripes:layout-component name="header.title">
        <stripes:label name="label.UserLogIn" />
    </stripes:layout-component>

    <stripes:layout-component name="content">
        <stripes:errors />
        <stripes:messages />
        <a href="#popupDialog" 
           data-rel="popup" 
           data-position-to="window" 
           data-transition="pop" 
           class="ui-btn-center ui-btn ui-btn-b ui-shadow ui-corner-all">
            <stripes:label name="label.UserLogOut" />
        </a>
        <div data-role="popup" id="popupDialog" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="max-width:400px;">
            <div data-role="header" data-theme="a">
                <h1><stripes:label name="label.dialog.UserLogOut" /></h1>
            </div>
            <div role="main" class="ui-content">
                <h3 class="ui-title"><stripes:label name="label.ask.UserLogOut" /></h3>
                <sdynattr:link href="/UserAuthorization.action"
                               event="logout"
                               class="ui-btn ui-corner-all ui-icon-check ui-btn-icon-left ui-btn-a ui-shadow">
                    <stripes:label name="Yes" />
                </sdynattr:link>
                <a href="#" 
                   class="ui-btn ui-corner-all ui-icon-forbidden ui-btn-icon-left ui-btn-b ui-shadow" 
                   data-rel="back" 
                   data-transition="flow">
                    <stripes:label name="No" />
                </a>
            </div>
        </div>                
    </stripes:layout-component>

    <stripes:layout-component name="footer">

    </stripes:layout-component>
</stripes:layout-render>
