<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="./../../../../stylesheets/styles.css">
    <link rel="stylesheet" href="./../../../../stylesheets/pygment_trac.css">
    <title>Titan Quest Calculator</title>
</head>
<body>
    <h1><#if name??>${name}<#else>Error reading name</#if></h1>
    <h2><#if description??>${description}<#else>Error reading name</#if></h2>
    <#if requiredWeapons??><h2 style="color: green">${requiredWeapons}</h2></#if>

    <table border=1.7>
    <#list attributes as attributeName, attributeValue>
    <#if attributeName!="requiredWeapons">  <tr><td>${attributeName}<td><#if ArrayList.isInstance(attributeValue)><#list attributeValue as value>${value} </#list><#else>${attributeValue}</#if></#if>
    </#list>
    </table>
</body>
</html>