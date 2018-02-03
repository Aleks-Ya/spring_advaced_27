<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Booked tickets</h1>
<#assign tickets=model.tickets>
<#include "includes/ticket_list.ftl">
