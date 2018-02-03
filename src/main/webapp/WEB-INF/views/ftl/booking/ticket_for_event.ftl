<#include "../login/login_details.ftl">
<h1>Tickets for event</h1>
<p>Event: ${(model.event.name)!}</p>
<#assign tickets=model.tickets>
<#include "includes/ticket_list.ftl">
