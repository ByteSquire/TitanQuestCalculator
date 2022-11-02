var m1CurrTier = 0;
var m2CurrTier = 0;

function plusClicked(button, event){
    var splits = button.innerText.split("/");
    var curr = Number(splits[0]);
    var max = Number(splits[1]);
    var updated = curr;
    var decrease = false;
    var matteringMastery = (button.parentElement.id.split("panel")[1] == "1")? m1 : m2;
    var left = event.button == 0;
    
    if(left){
        if (curr < max){
            if(event.shiftKey){
                updated = max;
                pointsSpent += max - curr;
                button.parentElement.getElementsByClassName("bar")[0].style.height = mod.masteryLevels.length * (80/mod.masteryLevels.length) + "%";
                if(matteringMastery == m1)
                    m1CurrTier = mod.masteryLevels.length;
                else
                    m2CurrTier = mod.masteryLevels.length;
            } else {
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
            }
        } else
            return;
    } else {
        if (curr > 0){
            var minRequiredMasteryTier = getMinMasteryTier(button.parentElement, matteringMastery);
            if(event.shiftKey){
                if(minRequiredMasteryTier == -1){
                    updated = 0;
                    pointsSpent -= curr;
                    button.parentElement.getElementsByClassName("bar")[0].style.height = "0%";
                    if(matteringMastery == m1)
                        m1CurrTier = 0;
                    else
                        m2CurrTier = 0;
                } else{
                    updated = mod.masteryLevels[minRequiredMasteryTier];
                    pointsSpent -= (curr-updated);
                    button.parentElement.getElementsByClassName("bar")[0].style.height = (minRequiredMasteryTier+1) * (80/mod.masteryLevels.length) + "%";
                    if(matteringMastery == m1)
                        m1CurrTier = minRequiredMasteryTier+1;
                    else
                        m2CurrTier = minRequiredMasteryTier+1;
                }
            } else {
                updated = curr - 1;
                if(updated < mod.masteryLevels[minRequiredMasteryTier])
                    return;
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
            }
        if(updated == 0)
            button.parentElement.getElementsByClassName("bar")[0].style.height = "0%";
        } else
            return;
    }
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
    calcBoni(matteringMastery.masteryAttributes, curr, updated);
    updateSkills();
}

function plusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );
    
    if( event ) {
        var pos = getPopupPos(event);
        
        pop.style.top = "" + pos.y + "px";
        pop.style.left = "" + pos.x + "px";
        pop.style.bottom = "unset";
    }
    var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
    var skill = { attributes: matteringMastery.masteryAttributes, name: matteringMastery.name, description: "" };
    pop.innerHTML = getPopupString(skill, Number(button.innerText.split("/")[0].replaceAll("\n", "")));
    
    pop.style.display = "block";
    movePopupintoView(pop);
}

function getMinMasteryTier(panel, mastery){
    var skillButtons = panel.getElementsByClassName("skillButton");
    var activeSkillButtons = [];
    for(var i = 0; i < skillButtons.length; i++){
        var button = skillButtons[i];
        var buttonLvl = button.innerText.split("/")[0].replaceAll("\n", "").replaceAll(" ", "");
        if(buttonLvl != "0"){
            activeSkillButtons.push(button);
        }
    }
    
    var minReqMasteryTier = -1;
    for(var i = 0; i < mod.masteryLevels.length; i++){
        mastery.skillTiers[i].forEach((skill) => {
            activeSkillButtons.forEach((button) => {
                if(button.id == skill.name){
                    minReqMasteryTier = i;
                }
            });
        });
    }
    
    return minReqMasteryTier;
}