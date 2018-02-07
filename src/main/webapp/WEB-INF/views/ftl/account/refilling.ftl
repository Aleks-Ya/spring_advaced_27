<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Refill account</h1>

<#-- TODO use constant-->
<form action="/account" method="post">
    <p>
        <label>
            User:
            <select name="userId">
            <#list model.users as user>
                <option value="${user.id}">${user.name}</option>
            </#list>
            </select>
        </label>
    </p>
    <p>
        <label>
            Amount (e.g. "1000.5"): <input name="amount" type="text"/>
        </label>
    </p>
    <p>
        <input type="submit" value="Refill"/>
    </p>
</form>
