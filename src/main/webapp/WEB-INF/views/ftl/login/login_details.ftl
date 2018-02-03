<#if currentUser()??>
<#assign user=currentUser()>
<p>${(user.name)!} (${(user.email)!}, ${(user.roles)!}, <a href='/logout'>logout</a>)</p>
<#else>
User: Anonymous (<a href='/login'>login</a>)
</#if>
