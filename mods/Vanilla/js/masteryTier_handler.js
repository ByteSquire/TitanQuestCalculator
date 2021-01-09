function masteryTierClicked(panel, index){
    var matteringMastery;
    if(panel.id == "panel1"){
        matteringMastery = m1;
        var minRequiredMasteryTier = getMinMasteryTier(panel, matteringMastery);
        if(index < minRequiredMasteryTier)
            index = minRequiredMasteryTier;
        var old = Number(panel.getElementsByClassName("plusButton")[0].innerText.split("/")[0]);
        m1CurrTier = index+1;
        pointsSpent += (mod.masteryLevels[m1CurrTier-1] - old);
    } else {
        matteringMastery = m2;
        var minRequiredMasteryTier = getMinMasteryTier(panel, matteringMastery);
        if(index < minRequiredMasteryTier)
            index = minRequiredMasteryTier;
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
    var event;
    if(curr > updated)
        event = {"button" : 2};
    else
        event = {"button" : 0};
    calcBoni(matteringMastery.masteryAttributes, curr, updated);
    updateSkills();
}