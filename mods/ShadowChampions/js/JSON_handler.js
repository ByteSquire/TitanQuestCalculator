var m1_id = new URLSearchParams(location.search).get("m1");
var m2_id = new URLSearchParams(location.search).get("m2");
var config_string = new URLSearchParams(location.search).get("config");
var mod;
var m1;
var m2;

var xmlhttp = new XMLHttpRequest();
var url =
  "https://bytesquire.github.io/TitanQuestCalculator/mods/ShadowChampions/ShadowChampions.json";

xmlhttp.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
    mod = JSON.parse(this.responseText);
    init();
  }
};
xmlhttp.open("GET", url, true);
xmlhttp.send();

function init(){
  if(m1_id > 10 || m1_id < 1 || Number.isNaN(m1_id))
    m1_id = null;
  if(m2_id > 10 || m2_id < 1 || Number.isNaN(m2_id))
    m2_id = null;
    
  m1 = mod.masteries[m1_id - 1];
  m2 = mod.masteries[m2_id - 1];
  
  if (m1) addSkills(document.getElementById("panel1"), m1);
  if (m2) addSkills(document.getElementById("panel2"), m2);
  setClassName();
  setMasteries();
  setMasteryTiers();
  
  if(m1 && config_string){
      var config;
      try{
        config = JSON.parse(config_string);
        loadConfig(config);
      } catch(err){
        document.getElementById("className").innerHTML += ' <span style="color: red">Error parsing config!</span>';
      }
  }
}

function setClassName(){
    var titleElement = document.getElementsByTagName("title")[0];
    var header = document.getElementById("className");
    var index1 = 0;
    var index2 = 0;
    
    if(m1)
        index1 = m1_id;
    if(m2)
        index2 = m2_id;
    if(index2 != 0 && Number(index1) > Number(index2) || Number(index1) == 0){
        var tmp = index1;
        index1 = index2;
        index2 = tmp;
    }
    
    titleElement.innerText = titleElement.innerText.replace("ClassName", mod.classNames[index1][index2]);
    header.innerText = header.innerText.replace("ClassName", mod.classNames[index1][index2]);
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
      .src.replace("MasteryPage", "mods/ShadowChampions");
  }

  if (m2) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2_id];
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("2", mastery2);
    document.getElementById("mastery2").src = document
      .getElementById("mastery2")
      .src.replace("MasteryPage", "mods/ShadowChampions");
  }
}

function setMasteryTiers() {
  var x = document.getElementsByClassName("masteryTier");
  for (var b = 0; b < mod.masteryLevels.length; b++) {
    x[b].innerHTML = mod.masteryLevels[b];
    x[b+mod.masteryLevels.length].innerHTML = mod.masteryLevels[b];
  } 
}