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
    if(window.location.href.endsWith("index.html")){
        window.location.href = window.location.href.replace("index.html", getMasteriesPage());
    } else {
        window.location.href += getMasteriesPage();
    }
  }
}

function getMasteriesPage() {
  var ret = "Masteries.html";
  if (masteryIndex0 > 0){
    ret += "?m1=" + masteryIndex0;
    if (masteryIndex1 > 0)
  	  ret += "&m2=" + masteryIndex1;
  }
  else if (masteryIndex1 > 0)
  	ret += "?m1=" + masteryIndex1;
  
  return ret;
}
