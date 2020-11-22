<html>
<head>
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1>You can click any link shown below to get info about the skill.</h1>

    <table border=1>
    <#list skills as skill>
        <tr><td><a href="${skill.url}">${skill.name}</a>
    </#list>
    </table>
</body>
</html>