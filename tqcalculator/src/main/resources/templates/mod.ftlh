<html>
<head>
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1>You can click any link shown below to get to the mastery overview.</h1>

    <table border=1>
    <#list masteries as mastery>
        <tr><td><a href="${mastery.url}"><#if mastery.name??>${mastery.name}<#else>name</#if></a>
    </#list>
    </table>
</body>
</html>