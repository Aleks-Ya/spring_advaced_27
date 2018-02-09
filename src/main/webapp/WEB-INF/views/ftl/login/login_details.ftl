<#assign loginEndpoint=statics['booking.web.controller.LoginController'].LOGIN_ENDPOINT>
<#assign logoutEndpoint=statics['booking.web.controller.LoginController'].LOGOUT_ENDPOINT>
<#if currentUser()??>
    <#if currentAccount()??>
        <#assign amountStr=', $' + currentAccount().amount>
    <#else>
        <#assign amountStr=''>
    </#if>
    <#assign user=currentUser()>
<p>${(user.name)!} (${(user.email)!}${amountStr}, ${(user.roles)!}, <a href='${logoutEndpoint}'>logout</a>)</p>
<#else>
User: Anonymous (<a href='${loginEndpoint}'>login</a>)
</#if>
