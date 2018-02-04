<#if currentUser()??>
    <#if currentAccount()??>
        <#assign amountStr=', $' + currentAccount().amount>
    <#else>
        <#assign amountStr=''>
    </#if>
    <#assign user=currentUser()>
<p>${(user.name)!} (${(user.email)!}${amountStr}, ${(user.roles)!}, <a href='/logout'>logout</a>)</p>
<#else>
User: Anonymous (<a href='/login'>login</a>)
</#if>
