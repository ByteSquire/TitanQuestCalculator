var m1CurrTier = 0;
var m2CurrTier = 0;
var m1GlobalPlus = 0;
var m2GlobalPlus = 0;

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
        if (canDecreaseMastery(button.parentElement, matteringMastery, (matteringMastery == m1)? m1CurrTier-1 : m2CurrTier-1, updated)){
            if (curr > 0){
                if(event.shiftKey){
                    updated = 0;
                    pointsSpent -= curr;
                    button.parentElement.getElementsByClassName("bar")[0].style.height = "0%";
                    if(matteringMastery == m1)
                        m1CurrTier = 0;
                    else
                        m2CurrTier = 0;
                } else {
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
    calcBoni(matteringMastery.masteryAttributes, curr, updated, event);
    updateSkills();
}

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
        if(curr1 > 0){
            if(curr1+globalPlus > Number(activeButtons[i].innerText.split("/")[1]))
                activeButtons[i].innerHTML = activeButtons[i].innerHTML.replace(Number(activeButtons[i].innerText.split("/")[0]), Number(activeButtons[i].innerText.split("/")[1]))
            else
                activeButtons[i].innerHTML = activeButtons[i].innerHTML.replace(Number(activeButtons[i].innerText.split("/")[0]), curr1+globalPlus);
        }
    }
       
    button.innerHTML = button.innerHTML.replace(curr, globalPlus);
}