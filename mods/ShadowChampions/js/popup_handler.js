function getPopupString(skill, currLevel){
    var ret = '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    if(skill.description){
        ret += '<span class="desc">' + skill.description.replaceAll("{^n}", "<br>\n").replaceAll("{^y}", '<p style="color: yellow">').replaceAll("{^g}", '<p style="color: green">').replaceAll("{^a}", '<p style="color: cyan">').replaceAll("{^w}", '</p>').replaceAll("^n", "").replaceAll("^y", '<p style="color: yellow">') + '</span>\n';
        ret += "<br>\n";
    }
    
    if(skill.attributes["requiredWeapons"]){
        ret += '<span class="" style="color: yellow">Works with: ' + skill.attributes["requiredWeapons"] + '</span>';
        ret += "<br>\n";
    }
    
    if(skill.parent){
        if(skill.parent.constructor === Array)
            for (var i = 0; i < skill.parent.length; i++) {
                if(Number(document.getElementById(skill.parent[i]).innerText.split("/")[0]) == 0){
                    ret += '<span class="" style="color: red">Requires at least one point in: ' + skill.parent[i] + '</span>';
                    ret += "<br>\n";
                }
            }
        else
            if(Number(document.getElementById(skill.parent).innerText.split("/")[0]) == 0){
                ret += '<span class="" style="color: red">Requires at least one point in: ' + skill.parent + '</span>';
                ret += "<br>\n";
            }
    }
    
    if(currLevel > 0){
        ret += '<br>\n';
        var colour = "gray";
        if(currLevel <= skill.attributes["MaxLevel"]){
            ret += '<span class="nextLevel">Level: ' + currLevel + '</span>\n';
        } else if(currLevel <= skill.attributes["UltimateLevel"]){
            ret += '<span class="nextLevel" style="color: yellow">Level: ' + currLevel + '</span>\n';
            if(currLevel == skill.attributes["UltimateLevel"])
                colour = "white";
        }
        ret += '<br>\n';
        var attr = Object.keys(skill.attributes);
        attr.forEach((key) => {
            if(key == "requiredWeapons")
                return;
            if(key == "MaxLevel")
                return;
            if(key == "UltimateLevel")
                return;
            var value = skill.attributes[key];
            ret += getAttributeStringWithColour(key, value, currLevel-1, colour);
            ret += '<br>\n';
        });
    }
    
    ret += '<br>\n';
    
    if(currLevel+1 <= skill.attributes["MaxLevel"]){
        ret += '<span class="nextLevel">Next Level: ' + (currLevel+1) + '</span>\n';
    } else if(currLevel+1 <= skill.attributes["UltimateLevel"]){
        ret += '<span class="nextLevel" style="color: yellow">Next Level: ' + (currLevel+1) + '</span>\n';
    } else {
        return ret;
    }
    ret += '<br>\n';
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        if(key == "requiredWeapons")
            return;
        if(key == "MaxLevel")
            return;
        if(key == "UltimateLevel")
            return;
        var value = skill.attributes[key];
        if(currLevel+1 == 1){
            ret += getAttributeStringWithColour(key, value, currLevel, "white");
            ret += '<br>\n';
            return;
        }   
        if (value.constructor === Array){
            ret += getAttributeStringWithColour(key, value, currLevel, "white");
            ret += '<br>\n'
        } else if(value.constructor === Object) {
            if(value.min){
                if(value.max){
                    if(value.max.constructor === Array || value.min.constructor === Array){
                        ret += getAttributeStringWithColour(key, value, currLevel, "white");
                        ret += '<br>\n'
                    }
                } else {
                    if(value.min.constructor === Array){
                        ret += getAttributeStringWithColour(key, value, currLevel, "white");
                        ret += '<br>\n'
                    }   
                }
            } else if(value.max && value.max.constructor === Array){
                ret += getAttributeStringWithColour(key, value, currLevel, "white");
                ret += '<br>\n';
            }
        }
    });
    return ret;
}

function getPopupPos(event) {
    return { x: Number(event.pageX)+15, y: event.pageY }
}

function movePopupintoView(pop, pageHeight){
    var bottomPos = Number(pop.style.top.split("px")[0]) + pop.offsetHeight;
    
    if(bottomPos > pageHeight){
        pop.style.top = "unset";
        pop.style.bottom = "-" + window.pageYOffset + "px";
    }
}

function getPageHeight() {
    return window.innerHeight + window.pageYOffset;              
}

function hidePopup(){
    document.getElementById("pop").style.display = "none";
    document.getElementById("pop").style.bottom = "unset";
    petDisplayed = false;
}

function getPopupStringPet(skill, currLevel){
    var hasPet = false;
    var ret = '<span class="title">' + skill.name + '</span>\n<br>\n';
    if(skill.pet){
        ret += '<table><tr style="vertical-align: top">';
        ret += '<td>';
        ret += formatPetAttributes(skill.pet.attributes, currLevel, "white");
        if(skill.petSkills){
            ret += "<td>";
            ret += formatPetSkills(skill.pet, currLevel, "white");
        }
    } else
        return getPopupString(skill, currLevel);
    return ret;
}