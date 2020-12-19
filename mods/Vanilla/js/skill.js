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
    '<span class="buttonText">0/' + skill.attributes.skillUltimateLevel + '</span>' +
    '<div class="disabled">0</div>';
  scaleButtonPositon(document.getElementById(skill.name), skill.skillIcon);
}

function scaleButtonPositon(button, iconPosition) {
  button.style.top = iconPosition.posY/5.4 + "%";
  button.style.left = iconPosition.posX/9.5 + "%";
}

function updateSkills(){    
    for (var i = 0; i < m1.skillTiers.length; i++){
        if(m1)
            if(i+1 <= m1CurrTier){
                m1.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "none";
                });
            } else {
                m1.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "grayscale()";
                });
            }
        
        if(m2)
            if(i+1 <= m2CurrTier){
                m2.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "none";
                });
            } else {
                m2.skillTiers[i].forEach((skill) => {
                    document.getElementById(skill.name).getElementsByClassName("skillButtonImage")[0].style.filter = "grayscale()";
                });
            }
    }
}