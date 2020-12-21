function addSkills(panel, mastery) {
  mastery.skillTiers.forEach((element) =>
    addSkillTier(panel, mastery, element)
  );
  panel.getElementsByClassName("plusButton")[0].innerHTML += '<span class="buttonText">\n0/' + mastery.masteryAttributes.MaxLevel + '</span>';
}

function addSkillTier(panel, mastery, skillTier) {
  skillTier.forEach((element) => addSkill(panel, mastery, element));
}

function addSkill(panel, mastery, skill) {
  panel.innerHTML +=
    '\n<button class="skillButton" id="' +
    skill.name +
    '" onmousedown="skillClicked(this, event);" onmouseover="skillButtonPopup(this, event);" onmouseout="hidePopup();">\n' +
    "\t<img\n" +
    '\t\tclass="skillButtonImage"\n' +
    '\t\tsrc="' +
    "images/skills/" +
        mastery.name.toLowerCase() +
    "/" +
    skill.name.replaceAll(" ", "_").replaceAll("'", "").toLowerCase().replaceAll(":", "") + 
    '.png"\n' +
    '/>\n' +
    '<span class="buttonText">0/' + skill.attributes.MaxLevel + '</span>' +
    '<div class="disabled">0</div>';
  scaleButtonPosition(document.getElementById(skill.name), skill.skillIcon);
  
  panel.innerHTML += '<button class="skillPlusButton" id="' + skill.name + '+' + '" onmousedown="skillPlusClicked(this, event);" onmouseover="skillPlusButtonPopup(this, event);" onmouseout="hidePopup();">\n' +
              ' <img\n' +
              '   class="skillPlusButtonImage"\n' + 
              '   src="../../MasteryPage/images/masteries/panel/plusbutton.png"\n' + 
              ' />\n' +
              ' 0\n' +
              '</button>';
  scaleButtonPosition(document.getElementById(skill.name + '+'), { posY: skill.skillIcon.posY + 4, posX: skill.skillIcon.posX - 20 });
}

function scaleButtonPosition(button, iconPosition) {
  button.style.top = iconPosition.posY/5.4 + "%";
  button.style.left = (iconPosition.posX-19)/8.65 + "%";
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

function skillButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    var pageHeight = getPageHeight();
    if( event ) {
        var pos = getPopupPos(event);
        
        pop.style.top = "" + pos.y + "px";
        pop.style.left = "" + pos.x + "px";
        pop.style.bottom = "unset";
    }
    var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
    var tiers = matteringMastery.skillTiers;
    var skill;
    tiers.forEach((tier) => {
        tier.forEach((aSkill) => {
            if(aSkill.name == button.id){
                skill = aSkill;
            }
        });
    });
    pop.innerHTML = getPopupString(skill, button.innerText.split("/")[0].replaceAll("\n", ""), true);

    pop.style.display = "block";
    movePopupintoView(pop, pageHeight);
}