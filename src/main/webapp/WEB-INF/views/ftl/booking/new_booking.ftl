<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>New booking</h1>

<form action="${statics['booking.web.controller.BookingController'].ROOT_ENDPOINT}" method="post">
    <p>
        <label>
            Event:
            <select name="eventId">
            <#list model.events as event>
                <option value="${event.id}">${event.name} (${event.auditorium.name}, ${event.dateTime}, $${event.basePrice})</option>
            </#list>
            </select>
        </label>
    </p>
    <p>
        <label>
            Seats (e.g. "10,11,15"): <input name="seats" type="text"/>
        </label>
    </p>
    <p>
        <input type="submit" value="Book"/>
    </p>
    <input name="userId" value="${currentUser().id}" type="hidden"/>
</form>
