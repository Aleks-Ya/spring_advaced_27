<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Auditoriums</h1>
<#list model.auditoriums as auditorium>
<p>Auditorium</p>
    <#include "includes/auditorium_details.ftl">
<hr/>
</#list>