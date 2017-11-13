<h1>User</h1>
<#if model.user??>
    <#assign user=model.user>
    <#include "user_details.ftl">
<#else>No user info</#if>