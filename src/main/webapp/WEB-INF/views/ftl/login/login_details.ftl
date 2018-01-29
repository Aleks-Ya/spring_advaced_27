<#if currentUser()??>
<#assign user=currentUser()>
<p>Name: ${(user.name)!}, E-mail: ${(user.email)!}, Roles: ${(user.roles)!} (<a href='/logout'>logout</a>)</p>
<#else>
User: Anonymous (<a href='/login'>login</a>)
</#if>
