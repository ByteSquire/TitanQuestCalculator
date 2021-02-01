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
    var skillButton = document.createElement("button");
    skillButton.classList.add("skillButton");
    skillButton.id = skill.name;
    skillButton.addEventListener("mousedown", () => {skillClicked(event.currentTarget, event);});
    skillButton.addEventListener("mouseover", () => {skillButtonPopup(event.currentTarget, event);});
    skillButton.addEventListener("mouseout",  () => {hidePopup();});
    
    var skillImage = document.createElement("img");
    skillImage.classList.add("skillButtonImage");
    skillImage.src = "images/skills/" +
    <#if name=="ShadowChampions">
    mastery.name.toLowerCase() + "/" + skill.name.replaceAll(" ", "_").replaceAll("'", "").toLowerCase().replaceAll(":", "")
    <#else>
    mastery.name + "/" + skill.name.replaceAll(":", "")
    </#if>
    + ".png";
    skillButton.appendChild(skillImage);
    
    var skillLevelSpan = document.createElement("span");
    skillLevelSpan.classList.add("buttonText");
    skillLevelSpan.innerText = "0/" + skill.attributes.MaxLevel;
    skillButton.appendChild(skillLevelSpan);
    
    var actualPointsSpent = document.createElement("div");
    actualPointsSpent.classList.add("disabled");
    actualPointsSpent.innerText = "0";
    skillButton.appendChild(actualPointsSpent);
    
    scaleButtonPosition(skillButton, skill.skillIcon);
  
    var skillPlusButton = document.createElement("button");
    skillPlusButton.classList.add("skillPlusButton");
    skillPlusButton.id = skill.name + "+";
    skillPlusButton.addEventListener("mousedown", () => {skillPlusClicked(event.currentTarget, event);});
    skillPlusButton.addEventListener("mouseover", () => {skillPlusButtonPopup(event.currentTarget, event);});
    skillPlusButton.addEventListener("mouseout",  () => {hidePopup();});
    
    var skillPlusButtonImage = document.createElement("img");
    skillPlusButtonImage.classList.add("skillPlusButtonImage");
    skillPlusButtonImage.src = "../../MasteryPage/images/masteries/panel/plusbutton.png";
    skillPlusButton.appendChild(skillPlusButtonImage);
    
    skillPlusButton.innerHTML += "0";
    scaleButtonPosition(skillPlusButton, { posY: skill.skillIcon.posY + 4, posX: skill.skillIcon.posX - 20 });
    
    panel.appendChild(skillButton);
    panel.appendChild(skillPlusButton);
    
    if(skill.parent){
        drawSkillConnection(panel, skillButton, document.getElementById(skill.parent[0]));
    }
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
    var skill = getSkillByName(matteringMastery, button.id);
    
    if(!petDisplayed){
        pop.innerHTML = getPopupString(skill, Number(button.innerText.split("/")[0].replaceAll("\n", "")));
    } else {
        pop.innerHTML = getPopupStringPet(skill, Number(button.innerText.split("/")[0].replaceAll("\n", "")));
    }
    pop.style.display = "block";
    movePopupintoView(pop, pageHeight);
}

var petDisplayed = false;
var lastTop = "unset";

function shiftPressed(event){
    if(event.shiftKey){
        var pop = document.getElementById("pop");
        if(pop.style.display == "none")
            return;
            
        var button = document.getElementById(pop.getElementsByClassName("title")[0].innerText);
        
        if(!button)
            return;
        
        var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
        var skill = getSkillByName(matteringMastery, button.id);
        
        if(!petDisplayed){
            if(pop.style.top != "unset")
                lastTop = pop.style.top;
            pop.innerHTML = getPopupStringPet(skill, Number(button.innerText.split("/")[0].replaceAll("\n", "")));
            petDisplayed = true;
        } else {
            pop.style.top = lastTop;
            pop.style.bottom = "unset";
            pop.innerHTML = getPopupString(skill, Number(button.innerText.split("/")[0].replaceAll("\n", "")));
            petDisplayed = false;
        }
        pop.style.display = "block";
        movePopupintoView(pop, getPageHeight());
    }
}

function getSkillByName(mastery, skillName){
    var tiers = mastery.skillTiers;
    var skill;
    tiers.forEach((tier) => {
        tier.forEach((aSkill) => {
            if(aSkill.name == skillName){
                skill = aSkill;
            }
        });
    });
    return skill;
}