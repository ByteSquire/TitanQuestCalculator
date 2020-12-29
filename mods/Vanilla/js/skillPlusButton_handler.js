var m1GlobalPlus = 0;
var m2GlobalPlus = 0;

function allPlusClicked(button, event) {
    var curr = Number(button.innerText);
    var globalPlus = (button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus;
    var left = event.button == 0;
    
    if(!left && globalPlus == 0)
        return;
    
    if(left)
        globalPlus++;
    else
        globalPlus--;
    
    if(button.parentElement.id == "panel1")
        m1GlobalPlus = globalPlus;
    else
        m2GlobalPlus = globalPlus;
    
    var activeButtons = button.parentElement.getElementsByClassName("skillButton"); 
    
    for(var i = 0; i < activeButtons.length; i++) {
        var curr1 = Number(activeButtons[i].innerHTML.split('<div class="disabled">')[1].replace("</div>", ""));
        var curr2 = curr1 + Number(document.getElementById(activeButtons[i].id + "+").innerText);
        if(curr1 > 0){
            var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
            var tiers = matteringMastery.skillTiers;
            var skill;
            tiers.forEach((tier) => {
                tier.forEach((aSkill) => {
                    if(aSkill.name == activeButtons[i].id){
                        skill = aSkill;
                    }
                });
            });
        
            if(curr2+globalPlus > skill.attributes.skillUltimateLevel)
                updateSkillLevel(skill, skill.attributes.skillUltimateLevel, event);
            else
                updateSkillLevel(skill, curr2 + globalPlus, event);
        }
    }
       
    button.innerHTML = button.innerHTML.replace(curr, globalPlus);
}

function allPlusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    var pageHeight = getPageHeight();
    if( event ) {
        var pos = getPopupPos(event);
        
        pop.style.top = "" + pos.y + "px";
        pop.style.left = "" + pos.x + "px";
        pop.style.bottom = "unset";
    }
    pop.innerHTML = '<span class="title">Adds ' + Number((button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus) + " to all skills of this mastery</span>";
    
    pop.style.display = "block";
    movePopupintoView(pop, pageHeight);
}

function skillPlusClicked(button, event) {
    var curr = Number(button.innerText);
    var localPlus = button.innerText;
    var left = event.button == 0;
    
    if(!left && localPlus == 0)
        return;
    
    if(left)
        localPlus++;
    else
        localPlus--;
    
    var skill = document.getElementById(button.id.split("+")[0]);
    var curr1 = Number(skill.innerHTML.split('<div class="disabled">')[1].replace("</div>", ""));
    var curr2;
    if(button.parentElement.id == "panel1")
        curr2 = curr1 + m1GlobalPlus;
    else
        curr2 = curr1 + m2GlobalPlus;
    if(curr1 > 0){
        var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
        var tiers = matteringMastery.skillTiers;
        var skillObj;
        tiers.forEach((tier) => {
            tier.forEach((aSkill) => {
                if(aSkill.name == skill.id){
                    skillObj = aSkill;
                }
            });
        });
    
        if(curr2+localPlus > skillObj.attributes.skillUltimateLevel)
            updateSkillLevel(skillObj, skillObj.attributes.skillUltimateLevel, event);
        else
            updateSkillLevel(skillObj, curr2 + localPlus, event);
    }
       
    button.innerHTML = button.innerHTML.replace(curr, localPlus);
}

function skillPlusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    var pageHeight = getPageHeight();
    if( event ) {
        var pos = getPopupPos(event);
        
        pop.style.top = "" + pos.y + "px";
        pop.style.left = "" + pos.x + "px";
        pop.style.bottom = "unset";
    }
    pop.innerHTML = '<span class="title">Adds ' + button.innerText + " to this skill</span>";
    
    pop.style.display = "block";
    movePopupintoView(pop, pageHeight);
}