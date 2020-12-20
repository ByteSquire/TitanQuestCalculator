function canSkill(button){
    var masteryLevel = button.parentElement.getElementsByClassName("plusButton")[0].innerText.split("/")[0].replaceAll("\n", "");
    masteryLevel = Number(masteryLevel);
    var matteringMastery = (Number(button.parentElement.id.split("panel")[1]) == 1)? m1 : m2;
    var tiers = matteringMastery.skillTiers;
    var tier;
    var skill;
    tiers.forEach((aTier) => {
        aTier.forEach((aSkill) => {
            if(aSkill.name == button.id){
                tier = tiers.indexOf(aTier);
                skill = aSkill;
            }
        });
    });
    if(skill.parent){
        return (masteryLevel >= mod.masteryLevels[Number(tier)]) && (Number(document.getElementById(skill.parent).innerText.split("/")[0]) > 0);
    }
    return (masteryLevel >= mod.masteryLevels[Number(tier)]);
}

function canDecreaseMastery(panel, mastery,  masteryTier, masteryLevel){
    if(masteryTier < 0){
        return true;
    }
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
    return ret;
}