<html>
<head>
    <link rel="stylesheet" href="../../stylesheets/styles.css">
    <link rel="stylesheet" href="../../stylesheets/pygment_trac.css">
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1>${name}</h1>
    <h2>You can click any link shown below to get info about the skill.</h2>

    <table border=1.7>
    <#list skills as skill>
        <tr><td><a href="${skill.url}">${skill.name}</a>
    </#list>
    </table>
</body>
</html>