var str = 50;
var strPercent = 0;
var int = 50;
var intPercent = 0;
var dex = 50;
var dexPercent = 0;
var mp = 300;
var mpPercent = 0;
var hp = 300;
var hpPercent = 0;

var pointsSpent = 0;
var bonusPoints = 0;
var maxPoints = 252;

function setSkillPoints(mod){
    maxPoints = mod.skillPointIncrement * (mod.maxLevel-1);
    updateUI();
}

function calcBoni(attributes, fromLevel, toLevel){
    var attrs = Object.keys(attributes);
    
    attrs.forEach((x) => {
        var _toLevel = toLevel;
        var _fromLevel = fromLevel;
        if(_toLevel > attributes[x].length)
            _toLevel = attributes[x].length;
        if(_fromLevel > attributes[x].length)
            _fromLevel = attributes[x].length;
        var value;
        
        if(attributes[x].constructor === Array){
            if(toLevel == 0){
                value = (-1) * Number(attributes[x][_fromLevel-1]);
            } else if(fromLevel == 0){
                value = Number(attributes[x][_toLevel-1]);
            } else {
                value = Number(attributes[x][_toLevel-1]) - Number(attributes[x][_fromLevel-1]);
            }
        } else {
            if(fromLevel == 1 && toLevel == 0){
                value = (-1) * Number(attributes[x]);
            }
            if(fromLevel == 0 && toLevel == 1){
                value = Number(attributes[x]);
            }
        }
        value = Number(value);
        updateAttributes(x, value);
    });
    updateUI();
}

function calcLevelReq() {
    if(pointsSpent > 0)
        document.getElementById("lvl").innerText = 1 + Math.ceil(pointsSpent/mod.skillPointIncrement);
    else
        document.getElementById("lvl").innerText = 1 + Math.floor(pointsSpent/mod.skillPointIncrement);
}

function pointsSelected(select){
    var diff = select.value - bonusPoints;
    bonusPoints = select.value;
    pointsSpent -= diff;
    calcLevelReq();
    updateUI();
}

function updateAttributes(x, value){
    if(!x.startsWith("+") && !x.startsWith("-"))
        return;
    //if(!(x.includes("Requirement") || !(x.includes("Regeneration") || x.includes("Leech"))){
        var attr = x.split(" ", 2)[1];
        if(!x.includes("%")){
            switch(attr){
            case "Strength": str += value;
                    break;
            case "Intelligence": int += value;
                    break;       
            case "Dexterity": dex += value;
                    break; 
            case "Health": hp += value;
                    break; 
            case "Energy": mp += value;
                    break; 
            }
        } else {
            switch(attr){
            case "Strength": strPercent += value;
                    break;
            case "Intelligence": intPercent += value;
                    break;       
            case "Dexterity": dexPercent += value;
                    break; 
            case "Health": hpPercent += value;
                    break; 
            case "Energy": mpPercent += value;
                    break; 
            }
        }
    //}
}

function updateUI(){
    document.getElementById("hp").innerText = Math.floor(hp)    + "+" + Math.floor(hpPercent) + "%";
    document.getElementById("mp").innerText = Math.floor(mp)    + "+" + Math.floor(mpPercent) + "%";
    document.getElementById("dex").innerText = Math.floor(dex)  + "+" + Math.floor(dexPercent) + "%";
    document.getElementById("str").innerText = Math.floor(str)  + "+" + Math.floor(strPercent) + "%";
    document.getElementById("int").innerText = Math.floor(int)  + "+" + Math.floor(intPercent) + "%";
    var pointsLeft = maxPoints-pointsSpent
    if(pointsLeft < 0)
        document.getElementById("pointsLeft").innerHTML = '<span style="color: red">' + pointsLeft + '</span>';
    else
        document.getElementById("pointsLeft").innerText = pointsLeft;
}