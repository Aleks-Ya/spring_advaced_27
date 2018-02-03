<p>Id: ${(booking.id)!}</p>
<p>User: ${(booking.user.name)!}</p>
<#assign ticket=booking.ticket>
<p>Ticket</p>
<#include "ticket_details.ftl">