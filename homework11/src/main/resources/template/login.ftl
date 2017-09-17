<#-- @ftlvariable name="login" type="java.lang.String" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>
    <#if login??>
        Hello, ${login}
    <#else>
        Login
    </#if>
    </title>
</head>
<body>
<#if login??>
<p>Hello, ${login}
    <form name="test" method="post" action="/">
        <input type="submit" value="Logout">
    </form>
</p>
<p><a href="/info">Statistic</a></p>
<#else>
<form name="test" method="post" action="/login">
    <p>Login: <input type="text" size="40" name="login"><input type="submit" value="Login"></p>
</form>
</#if>
</body>
</html>