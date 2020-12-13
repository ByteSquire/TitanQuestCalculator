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
var pointsSpent = 0;
var m1LevelReq;
var m2LevelReq;

var xmlhttp = new XMLHttpRequest();
var url =
  "https://bytesquire.github.io/TitanQuestCalculator/mods/SoulvizierAERA/SoulvizierAERA.json";

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
      .src.replace("MasteryPage", "mods/SoulvizierAERA");
  }

  if (m2) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2];
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("2", mastery2);
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("MasteryPage", "mods/SoulvizierAERA");
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
  panel.getElementsByClassName("plusButton")[0].innerHTML += "\n0/" + mastery.masteryAttributes.skillMaxLevel;
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
    mastery.name +
    "/" +
    skill.name +
    '.png"\n' +
    '/>\n' +
    '<br>\n' +
    '0/' + skill.attributes.skillMaxLevel;
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
        if (curr < max){
            updated = curr + 1;
            pointsSpent++;
        }
    } else {
        if (curr > 0){
            updated = curr - 1;
            pointsSpent--;
        }
    }
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
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
    pop.innerHTML = getSkillString(skill, button.innerText.split("/")[0].replaceAll("\n", ""));

    pop.style.width = "10%";

    pop.style.display = "block";
}

function skillClicked(button, left){
    var splits = button.innerText.split("/");
    var curr = Number(splits[0]);
    var max = Number(splits[1]);
    var updated = curr;
    if(left){
        if(canSkill(button)){
            if (curr < max){
                updated = curr + 1;
                pointsSpent++;
            }
        }
    } else {
        if (curr > 0){
            updated = curr - 1;
            pointsSpent--;
        }
    }
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
   
}

function hidePopup(){
    document.getElementById("pop").style.display = "none";
}

function calcLevelReq(){
    document.getElementById("lvl").innerText = 1 + Math.ceil(pointsSpent/3);
}

function canSkill(button){
    var masteryLevel = button.parentElement.getElementsByClassName("plusButton")[0].innerText.split("/")[0].replaceAll("\n", "");
    masteryLevel = Number(masteryLevel);
    var masteryIndex = (Number(button.parentElement.id.split("panel")[1]) == 1)? m1 : m2;
    var tiers = mod.masteries[Number(masteryIndex-1)].skillTiers;
    var tier;
    tiers.forEach((aTier) => {
        aTier.forEach((aSkill) => {
            if(aSkill.name == button.id){
                tier = tiers.indexOf(aTier);
            }
        });
    });
    return (masteryLevel >= mod.masteryLevels[Number(tier)]);
}