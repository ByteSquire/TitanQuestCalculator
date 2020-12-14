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
var m1CurrTier = 0;
var m2CurrTier = 0;
var str = 50;
var int = 50;
var dex = 50;
var mp = 300;
var hp = 300;

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
  panel.getElementsByClassName("plusButton")[0].innerHTML += '<span class="buttonText">\n0/' + mastery.masteryAttributes.skillMaxLevel + '</span>';
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
    skill.name.replaceAll(":", "") +
    '.png"\n' +
    '/>\n' +
    '<span class="buttonText">0/' + skill.attributes.skillUltimateLevel + '</span>';
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
    var decrease = false;
    var matteringMastery = (button.parentElement.id.split("panel")[1] == "1")? m1 : m2;
    if(left){
        if (curr < max){
            updated = curr + 1;
            pointsSpent++;
            mod.masteryLevels.forEach((element) => {
                if(element == updated){
                    button.parentElement.getElementsByClassName("bar")[0].style.height = (mod.masteryLevels.indexOf(element)+1) * (80/mod.masteryLevels.length) + "%";
                    if(matteringMastery == m1)
                        m1CurrTier++;
                    else
                        m2CurrTier++;
                }
            });
        } else
            return;
    } else {
        if (canDecreaseMastery(button.parentElement, mod.masteries[Number(matteringMastery)-1], (matteringMastery == m1)? m1CurrTier-1 : m2CurrTier-1, updated)){
            if (curr > 0){
                updated = curr - 1;
                pointsSpent--;
                if(m1CurrTier != 0 || m2CurrTier != 0){
                    var matters = mod.masteryLevels[Number((matteringMastery == m1)? m1CurrTier-1 : m2CurrTier-1)]-1;
                    if(updated == matters){
                        var currHeight = Number(button.parentElement.getElementsByClassName("bar")[0].style.height.split("%")[0]);
                        button.parentElement.getElementsByClassName("bar")[0].style.height = (currHeight - (80/mod.masteryLevels.length)) + "%";
                        if(button.parentElement.id.split("panel")[1] == 1)
                            m1CurrTier--;
                        else
                            m2CurrTier--;
                    }
                }
                if(updated == 0)
                    button.parentElement.getElementsByClassName("bar")[0].style.height = "0%";
            } else
                return;
        } else
            return;
    }
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
    calcBoni(mod.masteries[Number(matteringMastery)-1].masteryAttributes, updated, left);
    updateSkills();
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

function plusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    if( event ) {
        var sx = event.pageX;
        var sy = event.pageY;

        pop.style.top = "" + sy + "px";
        pop.style.left = "" + (sx+15) + "px";
    }
    var masteryIndex = (button.parentElement.id == "panel1")? (m1-1) : (m2-1);
    var skill = { attributes: mod.masteries[masteryIndex].masteryAttributes, name: mod.masteries[masteryIndex].name, description: "" };
    pop.innerHTML = getSkillString(skill, button.innerText.split("/")[0].replaceAll("\n", ""));
    
    pop.style.display = "block";
}

function skillClicked(button, left){
    var splits = button.innerText.split("/");
    var curr = Number(splits[0]);
    var max = Number(splits[1]);
    var updated = curr;
    if(canSkill(button)){
        if(left){
            if (curr < max){
                updated = curr + 1;
                pointsSpent++;
            } else
                return;
        } else {
            if (curr > 0){
                updated = curr - 1;
                pointsSpent--;
            } else
                return;
        }
    } else
        return;
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
    
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
    calcBoni(skill.attributes, updated, left);
}

function hidePopup(){
    document.getElementById("pop").style.display = "none";
}

function calcLevelReq(){
    document.getElementById("lvl").innerText = 1 + Math.ceil(pointsSpent/3);
}

function calcBoni(attributes, lvl, increase){
    var attrs = Object.keys(attributes);
    
    var changed = false;
    if(lvl == 0){
        changed = true;
    }
    
    attrs.forEach((x) => {
        if(x.startsWith("character")){
            var value;
            if(increase){
                if(lvl > 1)
                    value = Number(attributes[x][lvl-1]) - Number(attributes[x][lvl-2]);
                else 
                    value = Number(attributes[x][lvl-1]);
                if(lvl-1 > attributes[x].length-1)
                    return;
            } else {
                if(lvl >= 1)
                    value = Number(attributes[x][lvl]) - Number(attributes[x][lvl-1]);
                else {
                    if(changed)
                        value = Number(attributes[x][lvl]);
                    else
                        value = Number(attributes[x][lvl-1]);
                }
                if(lvl-1 >= attributes[x].length-1)
                    return;
                value *= -1;
            }
            value = Number(value);
            var attr = x.split("character")[1];
            switch(attr){
            case "Strength": str += value;
                    break;
            case "Intelligence": int += value;
                    break;       
            case "Dexterity": dex += value;
                    break; 
            case "Life": hp += value;
                    break; 
            case "Mana": mp += value;
                    break; 
            }
        }
    });
    document.getElementById("hp").innerText = hp;
    document.getElementById("mp").innerText = mp;
    document.getElementById("dex").innerText = dex;
    document.getElementById("str").innerText = str;
    document.getElementById("int").innerText = int;
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

function canDecreaseMastery(panel, mastery,  masteryTier, masteryLevel){
    if(masteryLevel > mod.masteryLevels[masteryTier]){
        return true;
    }
    var skillButtons = panel.getElementsByClassName("skillButton");
    var activeSkillButtons = [];
    for(var i = 0; i < skillButtons.length; i++){
        var button = skillButtons[i];
        var buttonLvl = button.innerText.split("/")[0].replaceAll("\n", "").replaceAll(" ", "");
        if(buttonLvl != "0"){
            activeSkillButtons.push(button);
        }
    }
    
    var ret = true;
    mastery.skillTiers[masteryTier].forEach((skill) => {
        activeSkillButtons.forEach((button) => {
            if(button.id == skill.name){
                ret = false;
            }
        });
    });
    //if(!ret){
    //    masteryLevel
    //}
    return ret;
}

function updateSkills(){
    var mastery1 = mod.masteries[m1-1];
    var mastery2 = mod.masteries[m2-1];
    
    for (var i = 0; i < mastery1.skillTiers.length; i++){
        if(mastery1)
            if(i+1 <= m1CurrTier){
                mastery1.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "none";
                });
            } else {
                mastery1.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "grayscale()";
                });
            }
        
        if(mastery2)
            if(i+1 <= m2CurrTier){
                mastery2.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "none";
                });
            } else {
                mastery2.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "grayscale()";
                });
            }
    }
}