<#assign max=statics['java.lang.Integer'].max(1,2)>
<#if (model.user.name)??>
<p>User: ${(model.user.name)!} (<a href="/logout">logout</a>)</p>
<#else>
User: Anonymous
</#if>
