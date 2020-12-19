var str = 50;
var int = 50;
var dex = 50;
var mp = 300;
var hp = 300;

var pointsSpent = 0;

function calcBoni(attributes, fromLevel, toLevel, event){
    var attrs = Object.keys(attributes);
    var increase = event.button == 0;
    
    var changed = false;
    var lvl = toLevel;
    if(lvl == 0){
        changed = true;
    }
    
    if(event.shiftKey){
        attrs.forEach((x) => {
            if(toLevel > attributes[x].length)
                toLevel = attributes[x].length;
            if(fromLevel > attributes[x].length)
                fromLevel = attributes[x].length;
                
            if(increase){
                var value;
                if(fromLevel > 0)
                    value = Number(attributes[x][toLevel-1]) - Number(attributes[x][fromLevel-1]);
                else
                    value = Number(attributes[x][toLevel-1]);
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
            } else{
                var value = "-" + attributes[x][fromLevel-1];
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
    } else {
        attrs.forEach((x) => {
            if(x.startsWith("character")){
                if(toLevel > attributes[x].length)
                    toLevel = attributes[x].length;
                if(fromLevel > attributes[x].length)
                    fromLevel = attributes[x].length;
                var value;
                if(increase){
                    if(lvl-1 > attributes[x].length-1)
                        return;
                    if(fromLevel > 0)
                        value = Number(attributes[x][toLevel-1]) - Number(attributes[x][fromLevel-1]);
                    else
                        value = Number(attributes[x][toLevel-1]);
                } else {
                    if(lvl-1 >= attributes[x].length-1)
                        return;
                    if(fromLevel > 0)
                        value = Number(attributes[x][toLevel-1]) - Number(attributes[x][fromLevel-1]);
                    else
                        value = Number(attributes[x][toLevel-1]);
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
    }
    document.getElementById("hp").innerText = hp;
    document.getElementById("mp").innerText = mp;
    document.getElementById("dex").innerText = dex;
    document.getElementById("str").innerText = str;
    document.getElementById("int").innerText = int;
}

function calcLevelReq(){
    document.getElementById("lvl").innerText = 1 + Math.ceil(pointsSpent/3);
}