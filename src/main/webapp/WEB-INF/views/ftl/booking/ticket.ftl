<h1>Ticket</h1>
<#if model.ticket??>
    <#assign ticket=model.ticket>
    <#include "includes/ticket_details.ftl">
<#else>No ticket info</#if>