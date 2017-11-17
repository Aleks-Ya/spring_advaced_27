<#if event??>
<p>Id: ${(event.id)!"-No id-"}</p>
<p>Name: ${(event.name)!"-No name-"}</p>
<p>Rate: ${(event.rate)!"-No rate-"}</p>
<p>Base price: ${(event.basePrice)!"-No base price-"}</p>
<p>Date: ${(event.dateTime)!"-No date-"}</p>
<p>Auditorium: ${(event.auditorium.name)!"-No auditorium-"}</p>
<#else>
<p>No information about event</p>
</#if>