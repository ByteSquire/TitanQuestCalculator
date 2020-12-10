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
  "https://bytesquire.github.io/TitanQuestCalculator/mods/ShadowChampions/ShadowChampions.json";

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
      .src.replace("MasteryPage", "mods/ShadowChampions");
  }

  if (m2) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2];
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("2", mastery2);
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("MasteryPage", "mods/ShadowChampions");
  }
}

function setMasteryTiers() {
  for (var i = 1; i <= mod.masteryLevels.length; i++) {
    var x = document.getElementsByClassName("masteryTier");
    var b;
    for (b = 0; b < x.length; b++) {
        if(x[b].innerHTML == i)
            x[b].innerHTML = mod.masteryLevels[i - 1];
    } 
  }
}

function addSkills(panel, mastery) {
  mastery.skillTiers.forEach((element) =>
    addSkillTier(panel, mastery, element)
  );
}

function addSkillTier(panel, mastery, skillTier) {
  skillTier.forEach((element) => addSkill(panel, mastery, element));
}

function addSkill(panel, mastery, skill) {
  panel.innerHTML +=
    '\n<button class="skillButton" id="' +
    skill.name +
    '">\n' +
    "\t<img\n" +
    '\t\tclass="skillButtonImage"\n' +
    '\t\tsrc="' +
    mod.url +
    "images/skills/" +
    mastery.name +
    "/" +
    skill.name +
    '.png"\n' +
    "/>";
  scaleButtonPositon(document.getElementById(skill.name), skill.skillIcon);
}

function scaleButtonPositon(button, iconPosition) {
  button.style.top = iconPosition.posY/5.4 + "%";
  button.style.left = iconPosition.posX/9.5 + "%";
}
