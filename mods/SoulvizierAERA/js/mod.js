var active = 0;
var masteryIndex0 = 0;
var masteryIndex1 = 0;
var mod;

var xmlhttp = new XMLHttpRequest();
var url = "SoulvizierAERA.json";

xmlhttp.onreadystatechange = function () {
  if (this.readyState == 4 && this.status == 200) {
    mod = JSON.parse(this.responseText);
    main();
  }
};
xmlhttp.open("GET", url, true);
xmlhttp.send();

function main() {
  document.getElementById("name").innerText = "SoulvizierAERA";
  for (var i = 1; i < 11; i++) {
    document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML.replaceAll(i.toString(), mod.mappedMasteries[i]);
    document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML.replace("MasteryPage", "mods/SoulvizierAERA");
  }
}

function changeImage(button) {
  switch (button.id) {
    case masteryIndex0:
      button.children[0].style.filter = "grayscale()";
      active--;
      masteryIndex0 = 0;
      break;
    case masteryIndex1:
      button.children[0].style.filter = "grayscale()";
      active--;
      masteryIndex1 = 0;
      break;
    default:
      if (active < 2) {
        button.children[0].style.filter = "none";
        if (masteryIndex0 == 0) masteryIndex0 = button.id;
        else masteryIndex1 = button.id;
        active++;
      }
      break;
  }
}

function submit(button) {
  if (active > 0) {
    window.location.href += getMasteriesPage();
  }
}

function getMasteriesPage() {
  if (masteryIndex1 != 0)
    return "Masteries.html?m1=" + masteryIndex0 + "&m2=" + masteryIndex1;
  else return "Masteries.html?m1=" + masteryIndex0;
}
