function drawSkillConnection(panel, fromSkillButton, toSkillButton) {
    if(fromSkillButton.style.left != toSkillButton.style.left)
        return;

    var hv = document.createElement("img");
    hv.src = "../../MasteryPage/images/masteries/panel/horizontal-vertical.png";
    hv.style.top = Number(fromSkillButton.style.top.replace("%", "")) + 2.5 + "%";
    hv.style.left = Number(fromSkillButton.style.left.replace("%", "")) + 4.9 + "%";
    hv.classList.add("skillConnectionCorner");
    
    var vh = document.createElement("img");
    vh.src = "../../MasteryPage/images/masteries/panel/vertical-horizontal.png";
    vh.style.top = Number(toSkillButton.style.top.replace("%", "")) + 0.1 + "%";
    vh.style.left = hv.style.left;
    vh.classList.add("skillConnectionCorner");

    panel.appendChild(hv);
    panel.appendChild(vh);

    var origin = Number(hv.style.top.replace("%", ""))+2;
    var destination = Number(vh.style.top.replace("%", ""));
    var leftOffset = Number(hv.style.left.replace("%", "")) + 0.1 + "%";

    var v = document.createElement("img");
    v.src = "../../MasteryPage/images/masteries/panel/vertical.png";
    v.style.top = origin + "%";
    v.style.left = leftOffset;
    v.style.height = destination-origin + "%";
    v.classList.add("skillConnectionStraight");
    panel.appendChild(v);
}