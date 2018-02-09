<html>
<head>
    <title>Login</title>
</head>
<body>
<div>
<#if model.error>
    <p style="color:red">Failed to login.</p>
</#if>
<#if model.logout>
    <p style="color:green">You have been logged out.</p>
</#if>
    <form action="${statics['booking.web.controller.LoginController'].LOGIN_ENDPOINT}"
          method="post">
        <fieldset>
            <legend>Login</legend>
            <p>Default BOOKING_MANAGER: john@gmail.com/jpass</p>
            <p>Default REGISTERED_USER: mary@gmail.com/mpass</p>
            <br/>
            <label for="username">E-mail</label>
            <input type="text" id="username" name="username"/>
            <br/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
            <br/>
            <label for="remember-me">Remember me</label>
            <input type="checkbox" name="remember-me" id="remember-me"/>
            <br/>
            <div>
                <button type="submit">Log in</button>
            </div>
        </fieldset>
    </form>
</div>
<div>
    <form action="${statics['booking.web.controller.UserController'].REGISTER_ENDPOINT}"
          enctype="${statics['org.springframework.http.MediaType'].APPLICATION_FORM_URLENCODED_VALUE}"
          method="post">
        <fieldset>
            <legend>Create new user</legend>
            <br/>
            <label for="name">Name</label>
            <input type="text" id="name" name="name"/>
            <br/>
            <label for="email">Email</label>
            <input type="email" id="email" name="email"/>
            <br/>
            <label for="birthday">Birthday</label>
            <input type="date" id="birthday" name="birthday"/>
            <br/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
            <br/>
            <div>
                <button type="submit">Create</button>
            </div>
        </fieldset>
    </form>
</div>
</body>
</html>