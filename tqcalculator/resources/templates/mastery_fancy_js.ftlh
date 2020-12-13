var m1 = new URLSearchParams(location.search).get("m1")
  ? new URLSearchParams(location.search).get("m1") <= 10
    ? new URLSearchParams(location.search).get("m1") >= 1
      ? new URLSearchParams(location.search).get("m1")
      : null
    : null
  : null;
var m2 = new URLSearchParams(location.search).get("m2")
  ? new URLSearchParams(location.search).get("m2") <= 10
    ? new URLSearchParams(location.search).get("m2") >= 1
      ? new URLSearchParams(location.search).get("m2")
      : null
    : null
  : null;
var mod;

var xmlhttp = new XMLHttpRequest();
var url =
  "https://bytesquire.github.io/TitanQuestCalculator/mods/${name}/${name}.json";

xmlhttp.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
    mod = JSON.parse(this.responseText);
    main();
  }
};
xmlhttp.open("GET", url, true);
xmlhttp.send();

function main() {
  setMasteries();
  setMasteryTiers();
  if (m1) addSkills(document.getElementById("panel1"), mod.masteries[m1 - 1]);
  if (m2) addSkills(document.getElementById("panel2"), mod.masteries[m2 - 1]);
}

function setMasteries() {
  if (m1) {
    document.getElementById("panel1").style.display = "unset";
    var mastery1 = mod.mappedMasteries[m1];
    document.getElementById("mastery1").src = document
      .getElementById("mastery1")
      .src.replace("1", mastery1);
    document.getElementById("mastery1").src = document
      .getElementById("mastery1")
      .src.replace("MasteryPage", "mods/${name}");
  }

  if (m2) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2];
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("2", mastery2);
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("MasteryPage", "mods/${name}");
  }
}

function setMasteryTiers() {
  var x = document.getElementsByClassName("masteryTier");
  for (var b = 0; b < mod.masteryLevels.length; b++) {
    x[b].innerHTML = mod.masteryLevels[b];
    x[b+mod.masteryLevels.length].innerHTML = mod.masteryLevels[b];
  } 
}

function addSkills(panel, mastery) {
  mastery.skillTiers.forEach((element) =>
    addSkillTier(panel, mastery, element)
  );
  panel.getElementsByClassName("plusButton")[0].innerHTML += "\n0/" + mastery.masteryAttributes.skillMaxLevel.value;
}

function addSkillTier(panel, mastery, skillTier) {
  skillTier.forEach((element) => addSkill(panel, mastery, element));
}

function addSkill(panel, mastery, skill) {
  panel.innerHTML +=
    '\n<button class="skillButton" id="' +
    skill.name +
    '" onclick="skillClicked(this, true);" oncontextmenu="skillClicked(this, false);"' + 
    ' onmouseover="skillButtonPopup(this, event);" onmouseout="hidePopup();">\n' +
    "\t<img\n" +
    '\t\tclass="skillButtonImage"\n' +
    '\t\tsrc="' +
    "images/skills/" +
    <#if name=="ShadowChampions">
    mastery.name.toLowerCase() +
    <#else>
    mastery.name +
    </#if>
    "/" +
    <#if name=="ShadowChampions">
    skill.name.replaceAll(" ", "_").replaceAll("'", "").toLowerCase() + 
    <#else>
    skill.name +
    </#if>
    '.png"\n' +
    '/>\n' +
    '<br>\n' +
    '0/' + skill.attributes.skillMaxLevel.value;
  scaleButtonPositon(document.getElementById(skill.name), skill.skillIcon);
}

function scaleButtonPositon(button, iconPosition) {
  button.style.top = iconPosition.posY/5.4 + "%";
  button.style.left = iconPosition.posX/9.5 + "%";
}

function plusClicked(button, left){
   var splits = button.innerText.split("/");
   var curr = Number(splits[0]);
   var max = Number(splits[1]);
   var updated = curr;
   if(left){
    if (curr < max)
        updated = curr + 1;
   } else {
    if (curr > 0)
        updated = curr - 1;
   }
   button.innerHTML = button.innerHTML.replace(curr, updated);
}

function skillClicked(button, left){
   var splits = button.innerText.split("/");
   var curr = Number(splits[0]);
   var max = Number(splits[1]);
   var updated = curr;
   if(left){
    if (curr < max)
        updated = curr + 1;
   } else {
    if (curr > 0)
        updated = curr - 1;
   }
   button.innerHTML = button.innerHTML.replace(curr, updated);
}

function skillButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    if( event ) {
        var sx = event.pageX;
        var sy = event.pageY;

        pop.style.top = "" + sy + "px";
        pop.style.left = "" + (sx+10) + "px";
    }
    var masteryIndex = (button.parentElement.id == "panel1")? (m1-1) : (m2-1);
    var tiers = mod.masteries[masteryIndex].skillTiers;
    var skill;
    tiers.forEach((tier) => {
        tier.forEach((aSkill) => {
            if(aSkill.name == button.id){
                skill = aSkill;
            }
        });
    });
    pop.innerHTML = getSkillString(skill, button);

    pop.style.width = "10%";

    pop.style.display = "block";
}

function getSkillString(skill, button){
    var ret = "";
    ret += '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    ret += '<span class="desc">' + skill.description.replaceAll("{^n}", "<br>\n") + '</span>\n';
    ret += "<br>\n";
    ret += "<br>\n";
    ret += '<span class="nextLevel">Next Level: ' + (Number(button.innerText.split("/")[0])+1) + '</span>\n';
    ret += '<br>\n';
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        ret += '<span class="skillAttribute">' + key + ': ' + skill.attributes[key].value + '</span>\n';
        ret += "<br>\n";
    });
    ret += '<br>';
    return ret;
}

function hidePopup(){
    document.getElementById("pop").style.display = "none";
}