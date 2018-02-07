<#include "../login/login_details.ftl">
<#include "../includes/navigator.ftl">

<h1>Account is refilled</h1>
<p>User: ${model.user.name}</p>
<p>Amount before: ${model.amountBefore}</p>
<p>Amount after: ${model.amountAfter}</p>
<p>Amount: ${model.amount}</p>