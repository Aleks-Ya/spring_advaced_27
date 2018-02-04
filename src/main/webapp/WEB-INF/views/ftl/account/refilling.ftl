<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Refill account</h1>

<#-- TODO use constant-->
<form action="/account" method="post">
    <p>User: ${currentUser().name}</p>
    <p>
        <label>
            Amount (e.g. "1000.5"): <input name="amount" type="text"/>
        </label>
    </p>
    <p>
        <input type="submit" value="Refill"/>
    </p>
    <input name="userId" value="${currentUser().id}" type="hidden"/>
</form>
