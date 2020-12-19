var str = 50;
var int = 50;
var dex = 50;
var mp = 300;
var hp = 300;

var pointsSpent = 0;

function calcBoni(attributes, lvl, increase){
    var attrs = Object.keys(attributes);
    
    var changed = false;
    if(lvl == 0){
        changed = true;
    }
    
    attrs.forEach((x) => {
        if(x.startsWith("character")){
            var value;
            if(increase){
                if(lvl > 1)
                    value = Number(attributes[x][lvl-1]) - Number(attributes[x][lvl-2]);
                else 
                    value = Number(attributes[x][lvl-1]);
                if(lvl-1 > attributes[x].length-1)
                    return;
            } else {
                if(lvl >= 1)
                    value = Number(attributes[x][lvl]) - Number(attributes[x][lvl-1]);
                else {
                    if(changed)
                        value = Number(attributes[x][lvl]);
                    else
                        value = Number(attributes[x][lvl-1]);
                }
                if(lvl-1 >= attributes[x].length-1)
                    return;
                value *= -1;
            }
            value = Number(value);
            var attr = x.split("character")[1];
            switch(attr){
            case "Strength": str += value;
                    break;
            case "Intelligence": int += value;
                    break;       
            case "Dexterity": dex += value;
                    break; 
            case "Life": hp += value;
                    break; 
            case "Mana": mp += value;
                    break; 
            }
        }
    });
    document.getElementById("hp").innerText = hp;
    document.getElementById("mp").innerText = mp;
    document.getElementById("dex").innerText = dex;
    document.getElementById("str").innerText = str;
    document.getElementById("int").innerText = int;
}

function calcLevelReq(){
    document.getElementById("lvl").innerText = 1 + Math.ceil(pointsSpent/3);
}