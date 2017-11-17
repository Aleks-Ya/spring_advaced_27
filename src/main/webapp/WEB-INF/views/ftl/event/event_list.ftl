<h1>Event list</h1>
<#list model.events as event>
<p>Event</p>
    <#include "includes/event_details.ftl">
<hr/>
</#list>