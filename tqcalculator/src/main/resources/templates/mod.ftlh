<html>
<head>
    <link rel="stylesheet" href="../stylesheets/styles.css">
    <link rel="stylesheet" href="../stylesheets/pygment_trac.css">
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1>${name}</h1>
    <h2>You can click any link shown below to get to the mastery overview.</h2>

    <table border=1.7>
    <#list masteries as mastery>
        <tr><td><a href="${mastery.url}"><#if mastery.name??>${mastery.name}<#else>name</#if></a>
    </#list>
    </table>
</body>
</html>