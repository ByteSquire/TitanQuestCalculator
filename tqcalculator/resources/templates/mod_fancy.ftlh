<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Titan Quest Calculator - ${name}</title>

    <link rel="stylesheet" type="text/css" href="../../css/styles.css" />

    <script type="text/javascript" src="js/mod.js"></script>
  </head>

  <body oncontextmenu="return false">
    <h1 id="name" class="centered">${name}</h1>
    <h2 class="centered">Choose one or two masteries</h2>

    <div class="masteryOverview">
    <#assign x = 1>
    <#list masteries as mastery>
      <button id="${x}" class="masteryButton" onclick="changeImage(this);">
        <img
          class="masteryImage"
          src="images/masteries/${mastery.name}.png"
        />
        <br />
        ${mastery.name}
        <#assign x++>
      </button>
    </#list>
    </div>

    <button class="submitButton" onclick="submit(this);">
      <img
        class="centered"
        src="../../MasteryPage/images/button.jpg"
      />
    </button>
    
    <#if name=="Vanilla">
    <div class="centered">
        <br>
        Huge thanks to the Titan Quest(TQ), TQ Ragnarok and TQ Atlantis developers for their awesome work
        <br>
        All images (apart from the submit button but including images on pages linked on this one) by said developers <a href="https://titanquestgame.com/">visit the official TQ site</a>
    </#if>
  </body>
</html>
