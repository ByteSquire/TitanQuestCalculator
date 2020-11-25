<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="../../../stylesheets/styles.css">
    <link rel="stylesheet" href="../../../stylesheets/pygment_trac.css">
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1><#if name??>${name}<#else>Error reading name</#if></h1>

    <table border=1.7>
    <#if buffName??>
        <tr><td>${buffName}
        <td>
        <table border=1.7>
            <#list buffAttributes as buffAttributeName, buffAttributeValue>
                <tr><td>${buffAttributeName}<td>${buffAttributeValue}
            </#list>
        </table>
    <#else>
    <#list attributes as attributeName, attributeValue>
        <tr><td>${attributeName}<td>${attributeValue}
    </#list>
    </#if>
    </table>
    
    <script src="/TitanQuestCalculator/assets/js/scale.fix.js"></script>
</body>
</html>