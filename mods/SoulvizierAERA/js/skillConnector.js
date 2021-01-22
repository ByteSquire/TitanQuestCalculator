function drawSkillConnection(panel, fromSkillButton, toSkillButton) {
    var hv = document.createElement("img");
    hv.src = "../../MasteryPage/images/masteries/panel/horizontal-vertical.png";
    hv.style.top = Number(fromSkillButton.style.top.replace("%", "")) + 2.5 + "%";
    hv.style.left = Number(fromSkillButton.style.left.replace("%", "")) + 4.9 + "%";
    hv.classList.add("skillConnectionCorner");
    
    var vh = document.createElement("img");
    vh.src = "../../MasteryPage/images/masteries/panel/vertical-horizontal.png";
    vh.style.top = Number(toSkillButton.style.top.replace("%", "")) + 0.1 + "%";
    vh.style.left = hv.style.left;//Number(toSkillButton.style.left.replace("%", "")) + 4.9 + "%";
    vh.classList.add("skillConnectionCorner");

    panel.appendChild(hv);
    panel.appendChild(vh);

    var origin = Number(hv.style.top.replace("%", ""))+2;
    var destination = Number(vh.style.top.replace("%", ""));
    var leftOffset = Number(hv.style.left.replace("%", "")) + 0.1 + "%";

    for(var i = origin; i < destination; i++) {
        var tmp = document.createElement("img");
        tmp.src = "../../MasteryPage/images/masteries/panel/vertical.png";
        tmp.style.top = i + "%";
        tmp.style.left = leftOffset;
        tmp.classList.add("skillConnectionStraight");
        panel.appendChild(tmp);
    }
}