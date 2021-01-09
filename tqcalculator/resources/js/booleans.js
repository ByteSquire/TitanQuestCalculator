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
        var ret = true;
        for (var i = 0; i < skill.parent.length; i++) {
            if(Number(document.getElementById(skill.parent[i]).innerText.split("/")[0]) == 0)
                ret = false;
        }
        return (masteryLevel >= mod.masteryLevels[Number(tier)]) && ret;
    }
    return (masteryLevel >= mod.masteryLevels[Number(tier)]);
}

function canDecreaseSkill(button, shift){
    if(Number(button.getElementsByClassName("disabled")[0].innerText) > 1 && !shift)
        return true;
    var matteringMastery = (Number(button.parentElement.id.split("panel")[1]) == 1)? m1 : m2;
    var tiers = matteringMastery.skillTiers;
    var tier;
    var skill;
    var ret = true;
    tiers.forEach((aTier) => {
        aTier.forEach((aSkill) => {
            if(aSkill.parent){
                if(aSkill.parent.constructor === Array){
                    for(var i = 0; i < aSkill.parent.length; i++){
                        if(aSkill.parent[i] == button.id && Number(document.getElementById(aSkill.name).getElementsByClassName("disabled")[0].innerText) > 0){
                            ret = false;
                        }
                    }
                } else {
                    if(aSkill.parent == button.id && Number(document.getElementById(aSkill.name).getElementsByClassName("disabled")[0].innerText) > 0){
                        ret = false;
                    }
                }
            }
        });
    });
    return ret;
}