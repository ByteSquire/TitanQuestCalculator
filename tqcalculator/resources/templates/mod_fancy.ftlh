<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Titan Quest Calculator</title>

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
  </body>
</html>
