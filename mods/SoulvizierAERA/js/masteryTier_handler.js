function masteryTierClicked(panel, index){
    var matteringMastery;
    if(panel.id == "panel1"){
        matteringMastery = m1;
        if(!canDecreaseMastery(panel, matteringMastery, m1CurrTier-1, mod.masteryLevels[index]))
            return;
        var old = Number(panel.getElementsByClassName("plusButton")[0].innerText.split("/")[0]);
        m1CurrTier = index+1;
        pointsSpent += (mod.masteryLevels[m1CurrTier-1] - old);
    } else {
        matteringMastery = m2;
        if(!canDecreaseMastery(panel, matteringMastery, m2CurrTier-1, mod.masteryLevels[index]))
            return;
        var old = Number(panel.getElementsByClassName("plusButton")[0].innerText.split("/")[0]);
        m2CurrTier = index+1;
        pointsSpent += (mod.masteryLevels[m2CurrTier-1] - old);
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