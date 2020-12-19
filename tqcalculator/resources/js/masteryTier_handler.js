function masteryTierClicked(panel, index){
    if(panel.id == "panel1"){
        m1CurrTier = index+1;
        matteringMastery = m1;
    } else {
        m2CurrTier = index+1;
        matteringMastery = m2;
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