<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">
<h1>Users</h1>
<#list model.users as user>
    <#include "user_details.ftl">
<hr/>
</#list>