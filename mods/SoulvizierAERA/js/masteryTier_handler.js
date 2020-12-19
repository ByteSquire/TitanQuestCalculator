function masteryTierClicked(panel, index){
    if(panel.id == "panel1"){
        matteringMastery = m1;
        if(!canDecreaseMastery(panel, matteringMastery, m1CurrTier-1, mod.masteryLevels[index]))
            return;
        m1CurrTier = index+1;
    } else {
        matteringMastery = m2;
        if(!canDecreaseMastery(panel, matteringMastery, m2CurrTier-1, mod.masteryLevels[index]))
            return;
        m2CurrTier = index+1;
    }
   
    var splits = panel.getElementsByClassName("plusButton")[0].innerText.split("/");
    var curr = Number(splits[0]);
    var updated = mod.masteryLevels[index];

    panel.getElementsByClassName("plusButton")[0].innerHTML = panel.getElementsByClassName("plusButton")[0].innerHTML.replace(curr, updated);
    panel.getElementsByClassName("bar")[0].style.height = (index+1) * (80/mod.masteryLevels.length) + "%";
    calcLevelReq();
    calcBoni(matteringMastery.masteryAttributes, curr, updated, event);
    updateSkills();
}