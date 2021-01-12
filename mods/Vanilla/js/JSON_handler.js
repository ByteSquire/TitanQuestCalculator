var m1_id = new URLSearchParams(location.search).get("m1")
  ? new URLSearchParams(location.search).get("m1") <= 10
    ? new URLSearchParams(location.search).get("m1") >= 1
      ? new URLSearchParams(location.search).get("m1")
      : null
    : null
  : null;
var m2_id = new URLSearchParams(location.search).get("m2")
  ? new URLSearchParams(location.search).get("m2") <= 10
    ? new URLSearchParams(location.search).get("m2") >= 1
      ? new URLSearchParams(location.search).get("m2")
      : null
    : null
  : null;
var mod;
var m1;
var m2;

var xmlhttp = new XMLHttpRequest();
var url =
  "https://bytesquire.github.io/TitanQuestCalculator/mods/Vanilla/Vanilla.json";

xmlhttp.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
    mod = JSON.parse(this.responseText);
    init();
  }
};
xmlhttp.open("GET", url, true);
xmlhttp.send();

function init(){
  m1 = mod.masteries[m1_id - 1];
  m2 = mod.masteries[m2_id - 1];
  
  if (m1) addSkills(document.getElementById("panel1"), m1);
  if (m2) addSkills(document.getElementById("panel2"), m2);
  setClassName();
  setMasteries();
  setMasteryTiers();
}

function setClassName(){
    var titleElement = document.getElementsByTagName("title")[0];
    var header = document.getElementById("className");
    titleElement.innerText = titleElement.innerText.replace("ClassName", "test");
    header.innerText = header.innerText.replace("ClassName", "test");
}

function setMasteries() {
  if (m1) {
    document.getElementById("panel1").style.display = "unset";
    var mastery1 = mod.mappedMasteries[m1_id];
    document.getElementById("mastery1").src = document
      .getElementById("mastery1")
      .src.replace("1", mastery1);
    document.getElementById("mastery1").src = document
      .getElementById("mastery1")
      .src.replace("MasteryPage", "mods/Vanilla");
  }

  if (m2) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2_id];
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("2", mastery2);
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("MasteryPage", "mods/Vanilla");
  }
}

function setMasteryTiers() {
  var x = document.getElementsByClassName("masteryTier");
  for (var b = 0; b < mod.masteryLevels.length; b++) {
    x[b].innerHTML = mod.masteryLevels[b];
    x[b+mod.masteryLevels.length].innerHTML = mod.masteryLevels[b];
  } 
}