function getPopupString(skill, currLevel, skipNext){
    var ret = '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    if(skill.description){
        ret += '<span class="desc">' + skill.description.replaceAll("{^n}", "<br>\n").replaceAll("{^y}", '<span style="color: yellow">').replaceAll("{^g}", '<span style="color: green">').replaceAll("{^a}", '<span style="color: cyan">').replaceAll("{^w}", '</span>').replaceAll("{^Y}", '<span style="color: yellow">').replaceAll("^Y", '<span style="color: yellow">').replaceAll("^n", "<br>\n").replaceAll("^y", '<span style="color: yellow">') + '</span>\n';
        if (!ret.endsWith("<br>\n"))
            ret += "<br>\n";
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
    
    if(skill.attributes["requiredWeapons"]){
        ret += "<br>\n";
        ret += '<span class="" style="color: yellow">Works with: ' + skill.attributes["requiredWeapons"] + '</span>\n';
    }
    
    if(skill.notDispellable){
        ret += "<br>\n";
        ret += '<span class="" style="color: cyan">Cannot be dispelled</span>\n';
    }
    
    // not sure how it works
    //if(skill.doesNotIncludeRacialDamage){
    //    ret += "<br>\n";
    //    ret += '<span class="" style="color: orange">Does not include Racial Damage</span>\n';
    //}
    
    if(skill.exclusiveSkill){
        ret += "<br>\n";
        ret += '<span class="" style="color: orange">Exclusive Skill - Only one Exclusive Skill can be active at a time</span>\n';
    }
    
    if(skill.projectileUsesAllDamage){
        ret += "<br>\n";
        ret += '<span class="" style="color: orange">Projectile(s) trigger weapon hit effects</span>\n';
    }
    
    var skillCastStringDesc = "";
    
    if(skill.castOnTarget){
        skillCastStringDesc += 'Skill(s) will be cast at the target’s location';
    }
    
    if(skill.triggerType){
        var s;
        if (skillCastStringDesc.length == 0){
            s = 'Skill(s) will be cast ' + skill.triggerType;
        } else{
            s = ' ' + skill.triggerType
        }
        skillCastStringDesc += s;
    }
    
    if(skill.castOnDamage){
        var s;
        var damageType = skill.castOnDamage == 'ALL' ? 'any' : skill.castOnDamage.toLowerCase();
        if (!skill.triggerType){
            s = 'Skill(s) will be cast when dealing ' + damageType  + ' damage';
        } else{
            s = ' by ' + damageType + ' damage'; 
        }
        skillCastStringDesc += s;
    }
    
    if(skill.triggeringSkill){
        var s;
        if (skillCastStringDesc.length == 0){
            s = 'Skill(s) are triggered by ' + skill.triggeringSkill;
        } else{
            s = (skill.castOnDamage ? ' from ' : ' by ') + skill.triggeringSkill; 
        }
        skillCastStringDesc += s;
    }
    
    if (skillCastStringDesc.length > 0){
        ret += "<br>\n";
        ret += '<span class="" style="color: orange">';
        ret += skillCastStringDesc;
        ret += '</span>\n';
    }
    
    if(skill.protectsAgainst){
        ret += "<br>\n";
        ret += '<span class="" style="color: green">Protects Against:</span>\n';
        skill.protectsAgainst.forEach((dmgType) => {
            ret += "<br>\n";
            ret += '<span class="desc" style="color: green">' + dmgType + '</span>\n';
        });
    }
    
    ret += '<br>\n';
    ret += '<table style="width: 100%">\n';
    
    var colour = "white";
    if(currLevel > 0){        
        ret += '<tr style="vertical-align: top"><td>';
        
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
            if(key == "Bonus to all Pets:"){
                ret += '<span style="color: orange">Bonus to all Pets:</span>\n';
                var petAttr = Object.keys(value);
                petAttr.forEach((petKey) => {
                    var petValue = value[petKey];
                    if(petValue.key === null)
                        return;
                    var str = getAttributeStringWithColour(petKey, petValue, currLevel-1, "lightskyblue");
                    if(str.startsWith("<br>\n"))
                        str.substring(6, str.length);
                    ret += '<div class="indentedAttribute">';
                    ret += str;
                    ret += '</div>';
                });
            } else
                ret += getAttributeStringWithColour(key, value, currLevel-1, colour);
            ret += '<br>\n';
        });
    }
    
    if(ret.endsWith("<br>\n"))
        ret = ret.substring(0, ret.length - "<br>\n".length);
    
    if(skipNext){
		return popupEarlyOut(skill, ret);
    }
    
    if(currLevel+1 <= skill.attributes["MaxLevel"]){
        ret += '<td>';
        ret += '<span class="nextLevel">Next Level: ' + (currLevel+1) + '</span>\n';
    } else if(currLevel+1 <= skill.attributes["UltimateLevel"]){
        ret += '<td>';
        ret += '<span class="nextLevel" style="color: yellow">Next Level: ' + (currLevel+1) + '</span>\n';
    } else {
		return popupEarlyOut(skill, ret);
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
        if(key == "Bonus to all Pets:"){
            ret += '<span style="color: orange">Bonus to all Pets:</span>\n';
            var petAttr = Object.keys(value);
            petAttr.forEach((petKey) => {
                var petValue = value[petKey];
                if(petValue.key === null)
                    return;
                if(currLevel+1 == 1){
                    var str = getAttributeStringWithColour(petKey, petValue, currLevel, colour);
                    if(str.startsWith("<br>\n"))
                        str.substring(6, str.length);
                    ret += '<div class="indentedAttribute">';
                    ret += str;
                    ret += '</div>';
                    return;
                }   
                if (petValue.constructor === Array){
                    var str = getAttributeStringWithColour(petKey, petValue, currLevel, colour);
                    if(str.startsWith("<br>\n"))
                        str.substring(6, str.length);
                    ret += '<div class="indentedAttribute">';
                    ret += str;
                    ret += '</div>';
                } else if(petValue.constructor === Object) {
                    if(checkForArray(petValue)){
                        var str = getAttributeStringWithColour(petKey, petValue, currLevel, colour);
                        if(str.startsWith("<br>\n"))
                            str.substring(6, str.length);
                        ret += '<div class="indentedAttribute">';
                        ret += str;
                        ret += '</div>';
                    }
                }
            });
            ret += '<br>\n';
        } else {
            if(currLevel+1 == 1){
                ret += getAttributeStringWithColour(key, value, currLevel, colour);
                ret += '<br>\n';
                return;
            }   
            if (value.constructor === Array){
                ret += getAttributeStringWithColour(key, value, currLevel, colour);
                ret += '<br>\n';
            } else if(value.constructor === Object) {
                if(checkForArray(value)){
                    ret += getAttributeStringWithColour(key, value, currLevel, colour);
                ret += '<br>\n';
                }
            }
        }
    });
    
    if(ret.endsWith("<br>\n"))
        ret = ret.substring(0, ret.length - "<br>\n".length);
    
	return popupEarlyOut(skill, ret);
}

function popupEarlyOut(skill, value){
	value += '</tr></table>\n';
    
    if (skill.pet || skill.skillCast)
    	value += '<span class="" style="color: gray">Press shift for more info</span>\n';
	
	return value;
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
    if(value.chance && value.chance.constructor === Array)
        return true;
    if(value.chance && value.chance.constructor === Object)
        return checkForArray(value.chance);
    if(value.value && value.value.constructor === Array)
        return true;
    if(value.value && value.value.constructor === Object)
        return checkForArray(value.value);
    if(value.duration && value.duration.constructor === Array)
        return true;
    if(value.duration && value.duration.constructor === Object)
        return checkForArray(value.duration);
    if(value.values){
        var ret = false;
        var keys = Object.keys(value.values);
        keys.forEach((key) => {
            ret = checkForArray(value.values[key]);
        });
        return ret;
    }
        
    return false;
}

function getPopupPos(event) {
    return { x: Number(event.pageX)+15, y: event.pageY }
}

function movePopupintoView(pop){
    var pageHeight = getPageHeight();
    var bottomPos = Number(pop.style.top.split("px")[0]) + pop.offsetHeight;
    
    if(bottomPos > pageHeight){
        pop.style.top = "unset";
        pop.style.bottom = "-" + window.pageYOffset + "px";
    }
    
    var pageWidth = getPageWidth();
    var rightPos = Number(pop.style.left.split("px")[0]) + pop.offsetWidth;
    
    if (rightPos > pageWidth){
    	pop.style.left = "unset";
    	pop.style.right = "-" + window.pageXOffset + "px";
    }
}

function getPageHeight() {
    return window.innerHeight + window.pageYOffset;              
}

function getPageWidth() {
	return window.innerWidth + window.pageXOffset;
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
            ret += '<span style="color: red">Invest at least one point into this skill to get info about it\'s pet</span>';
            return ret;
        }
        currLevel -= 1;
        ret += '<table><tr style="vertical-align: top">';
        ret += '<td>';
        if (skill.pet.initialSkill){
            ret += '<table><tr style="vertical-align: top">';
            ret += '<td>';
        }
        ret += formatPetAttributes(skill.pet.attributes, currLevel, "white");
        if (skill.pet.initialSkill){
            ret += '<tr style="vertical-align: top">';
            ret += '<td>';
            ret += formatPetInitialSkill(skill.pet.initialSkill, currLevel+1, "white");
            ret += '</table>';
        }
        if(skill.pet.petSkills){
            ret += "<td>";
            ret += formatPetSkills(skill.pet, currLevel, "white");
        }
        ret += '</table>';
    } else if (skill.skillCast) {
        var ret = '<span class="title">' + skill.name + '</span>\n<br>\n';
        if(currLevel == 0){
            ret += '<span style="color: red">Invest at least one point into this skill to get info about the skill(s) it casts</span>';
            return ret;
        }
        currLevel -= 1;
        ret += '<table><tr style="vertical-align: top">';
        if(skill.skillCast.castSkills){
            ret += "<td>";
            ret += formatCastSkills(skill.skillCast, currLevel, "white");
        }
        ret += '</table>';
    } else
        return getPopupString(skill, currLevel);
    return ret;
}