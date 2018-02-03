<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Booked tickets</h1>
<#assign bookings=model.bookings>
<#include "includes/booking_list.ftl">
