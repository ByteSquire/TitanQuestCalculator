var m1 = new URLSearchParams(location.search).get("m1");
var m2 = new URLSearchParams(location.search).get("m2");
var mod;

var xmlhttp = new XMLHttpRequest();
var url = "https://bytesquire.github.io/TitanQuestCalculator/mods/Vanilla/Vanilla.json";

xmlhttp.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
    mod = JSON.parse(this.responseText);
    main();
  }
};
xmlhttp.open("GET", url, true);
xmlhttp.send();

function main() {
  if (m1 && m1 <= 10 && m1 >= 1) {
    document.getElementById("panel1").style.display = "unset";
    var mastery1 = mod.mappedMasteries[m1];
    document.getElementById("mastery1").src = document.getElementById("mastery1").src.replace("1", mastery1);
    document.getElementById("mastery1").src = document.getElementById("mastery1").src.replace("MasteryPage", "mods/Vanilla");
  }

  if (m2 && m2 <= 10 && m2 >= 1) {
    document.getElementById("panel2").style.display = "unset";
    var mastery2 = mod.mappedMasteries[m2];
    document.getElementById("mastery2").src = document.getElementById("mastery2").src.replace("2", mastery2);
    document.getElementById("mastery2").src = document.getElementById("mastery2").src.replace("MasteryPage", "mods/Vanilla");
  }
}
