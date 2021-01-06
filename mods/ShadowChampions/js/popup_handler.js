function getPopupString(skill, currLevel, skipNext){
    var ret = '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    if(skill.description){
        ret += '<span class="desc">' + skill.description.replaceAll("{^n}", "<br>\n").replaceAll("{^y}", '<p style="color: yellow">').replaceAll("{^g}", '<p style="color: green">').replaceAll("{^a}", '<p style="color: cyan">').replaceAll("{^w}", '</p>').replaceAll("{^Y}", '<p style="color: yellow">').replaceAll("^Y", '<p style="color: yellow">').replaceAll("^n", "").replaceAll("^y", '<p style="color: yellow">') + '</span>\n';
        ret += "<br>\n";
    }
    
    if(skill.attributes["requiredWeapons"]){
        ret += '<span class="" style="color: yellow">Works with: ' + skill.attributes["requiredWeapons"] + '</span>';
        ret += "<br>\n";
    }
    
    if(skill.notDispellable || skill.doesNotIncludeRacialDamage || skill.exclusiveSkill || skill.protectsAgainst)
        ret += "<br>\n";
    
    if(skill.notDispellable){
        ret += '<span class="" style="color: cyan">Cannot be dispelled</span>';
        ret += "<br>\n";
    }
    
    if(skill.doesNotIncludeRacialDamage){
        ret += '<span class="" style="color: orange">Does not include Racial Damage</span>';
        ret += "<br>\n";
    }
    
    if(skill.exclusiveSkill){
        ret += '<span class="" style="color: orange">Excluse Skill - Only one Exclusive Skill can be active at a time</span>';
        ret += "<br>\n";
    }
    
    if(skill.protectsAgainst){
        ret += '<spanc class="" style="color: green">Protects Against:</span>';
        ret += "<br>\n";
        skill.protectsAgainst.forEach((dmgType) => {
            ret += dmgType;
            ret += "<br>\n";
        });
    }
    
    if(skill.parent){
        if(skill.parent.constructor === Array)
            for (var i = 0; i < skill.parent.length; i++) {
                if(document.getElementById(skill.parent[i]))
                    if(Number(document.getElementById(skill.parent[i]).innerText.split("/")[0]) == 0){
                        ret += '<span class="" style="color: red">Requires at least one point in: ' + skill.parent[i] + '</span>';
                        ret += "<br>\n";
                    }
            }
        else if(document.getElementById(skill.parent))
            if(Number(document.getElementById(skill.parent).innerText.split("/")[0]) == 0){
                ret += '<span class="" style="color: red">Requires at least one point in: ' + skill.parent + '</span>';
                ret += "<br>\n";
            }
    }
    
    var colour = "white";
    if(currLevel > 0){
        ret += '<br>\n';
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
            if(value.key === null)
                return;
            ret += getAttributeStringWithColour(key, value, currLevel-1, colour);
            ret += '<br>\n';
        });
    }
    
    if(skipNext)
        return ret;
    
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
    colour = "gray";
    attr.forEach((key) => {
        if(key == "requiredWeapons")
            return;
        if(key == "MaxLevel")
            return;
        if(key == "UltimateLevel")
            return;
        var value = skill.attributes[key];
        if(value.key === null)
            return;
        if(currLevel+1 == 1){
            ret += getAttributeStringWithColour(key, value, currLevel, colour);
            ret += '<br>\n';
            return;
        }   
        if (value.constructor === Array){
            ret += getAttributeStringWithColour(key, value, currLevel, colour);
            ret += '<br>\n'
        } else if(value.constructor === Object) {
            if(checkForArray(value)){
                ret += getAttributeStringWithColour(key, value, currLevel, colour);
                ret += '<br>\n';
            }
        }
    });
    return ret;
}

function checkForArray(value){
    if(value.min){
        if(value.max){
            if(value.max.constructor === Array || value.min.constructor === Array)
                return true;
        } else {
            if(value.min.constructor === Array)
                return true;
        }
    } else if(value.max && value.max.constructor === Array)
        return true;
    else if(value.chance && value.chance.constructor === Array)
        return true;
    else if(value.value0 && value.value0.constructor === Array || value.value1 && value.value1.constructor === Array)
        return true;
    else if(value.value0 && value.value0.constructor === Object)
        return checkForArray(value.value0);
    else if(value.value0 && value.value1.constructor === Object)
        return checkForArray(value.value1);
        
    return false;
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
    lastTop = pop.style.top;
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
    if(skill.pet){
        var ret = '<span class="title">' + skill.name + '</span>\n<br>\n';
        if(currLevel == 0){
            ret += '<span style="color: red">Invest at last one point into this skill to get info about it\'s pet</span>';
            return ret;
        }
        currLevel -= 1;
        ret += '<table><tr style="vertical-align: top">';
        ret += '<td>';
        ret += formatPetAttributes(skill.pet.attributes, currLevel, "white");
        if(skill.pet.petSkills){
            ret += "<td>";
            ret += formatPetSkills(skill.pet, currLevel, "white");
        }
    } else
        return getPopupString(skill, currLevel);
    return ret;
}