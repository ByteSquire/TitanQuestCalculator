function skillClicked(button, event){
    var splits = button.innerText.split("/");
    var curr = Number(splits[0]);
    var max = Number(splits[1]);
    var localPointsSpent = Number(button.innerHTML.split('<div class="disabled">')[1].replace("</div>", ""));
    var left = event.button == 0;
    
    if(canSkill(button)){
        if(left){
            if (localPointsSpent < max){
                if(event.shiftKey){
                    pointsSpent += max - localPointsSpent;
                    localPointsSpent = max;
                } else {
                    localPointsSpent = localPointsSpent + 1;
                    pointsSpent++;
                }
            } else
                return;
        } else {
            if (localPointsSpent > 0){
                if(event.shiftKey){
                    pointsSpent -= localPointsSpent;
                    localPointsSpent = 0;
                } else {
                    localPointsSpent = localPointsSpent - 1;
                    pointsSpent--;
                }
            } else
                return;
        }
    } else
        return;
    
    button.getElementsByClassName("disabled")[0].innerText = localPointsSpent;
    var updated = localPointsSpent;
    
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
    
    if(localPointsSpent > 0){
        if((button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus > 0){
            if(updated + Number((button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus) > skill.attributes.skillUltimateLevel)
                updated = skill.attributes.skillUltimateLevel;
            else
                updated += (button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus;
        }
    } else {
        updated = 0;
    }
    button.innerHTML = button.innerHTML.replace(curr, updated);
    calcLevelReq();
    
    calcBoni(skill.attributes, curr, updated, event);
}