<#include "login/login_details.ftl">
<h1>Choose page:</h1>
<p>
<ul>
    <li><a href="${statics['booking.web.controller.AuditoriumController'].ENDPOINT}">Auditoriums</a></li>
    <li><a href="${statics['booking.web.controller.EventController'].ENDPOINT}">Events</a></li>
    <li><a href="${statics['booking.web.controller.BookingController'].NEW_BOOKING_ENDPOINT}">New booking</a></li>
</ul>
<p>
    Restricted area (only for booking managers):
<ul>
    <li><a href="${statics['booking.web.controller.BookingController'].SHOW_ALL_TICKETS_ENDPOINT}">Booked tickets (HTML)</a></li>
    <li><a href="${statics['booking.web.controller.BookingController'].SHOW_ALL_TICKETS_PDF_ENDPOINT}">Booked tickets (PDF)</a></li>
    <li><a href="${statics['booking.web.controller.UserController'].SHOW_ALL_USERS_ENDPOINT}">Users</a></li>
    <li><a href="${statics['booking.web.controller.AccountController'].REFILLING_ENDPOINT}">Refill account</a></li>
</ul>
