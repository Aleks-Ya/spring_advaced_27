<h1>Ticket for event</h1>
<p>Events:
<#list model.events as event>
    ${event.name};
</#list>
</p>
<#assign tickets=model.tickets>
<#include "includes/ticket_list.ftl">
