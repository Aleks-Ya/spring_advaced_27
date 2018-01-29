<#if (model.user.name)??>
<p>Name: ${(model.user.name)!}, E-mail: ${(model.user.email)!}, Roles: ${(model.user.roles)!} (<a href="/logout">logout</a>)</p>
<#else>
User: Anonymous
</#if>
