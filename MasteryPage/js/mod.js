var active = 0;
var masteryIndex0 = 0;
var masteryIndex1 = 0;

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
    window.location.href = window.location.href.replace(
      "Mod.html",
      getMasteriesPage()
    );
  }
}

function getMasteriesPage() {
  if (masteryIndex1 != 0)
    return "Masteries.html?m1=" + masteryIndex0 + "&m2=" + masteryIndex1;
  else return "Masteries.html?m1=" + masteryIndex0;
}
