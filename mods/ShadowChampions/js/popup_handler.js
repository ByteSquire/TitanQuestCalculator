function getPopupString(skill, currLevel, isSkill){
    currLevel = Number(currLevel);
    var ret = "";
    ret += '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    if(isSkill){
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
        if(currLevel <= skill.attributes["skillMaxLevel"]){
            ret += '<span class="nextLevel">Level: ' + currLevel + '</span>\n';
        } else if(currLevel <= skill.attributes["skillUltimateLevel"]){
            ret += '<span class="nextLevel" style="color: yellow">Level: ' + currLevel + '</span>\n';
        }
        ret += '<br>\n';
        var attr = Object.keys(skill.attributes);
        attr.forEach((key) => {
            if(key == "requiredWeapons")
                return;
            if(key == "skillMaxLevel")
                return;
            if(key == "skillUltimateLevel")
                return;
            var value = skill.attributes[key];
            if(value.constructor === Array){
                if(value[currLevel-1] == 0)
                    return;
                var index = currLevel-1;
                if(value[index] === undefined){
                    index = value.length-1;
                    if(value[index] === undefined)
                        return;
                }
                ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value[index] + '</span>\n';
                ret += "<br>\n";
            } else if(value.constructor === Object) {
                if(value.min.constructor === Array){
                    if(value.max.constructor === Array){
                        if(value.min[currLevel-1] == 0)
                            return;
                        var index = currLevel-1;
                        if(value.min[index] === undefined){
                            index = value.min.length-1;
                            if(value.min[index] === undefined)
                                return;
                        }
                        ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min[index] + ' ~ ' + value.max[index] + '</span>\n';
                        ret += "<br>\n";
                    } else {
                        if(value.min[currLevel-1] == 0)
                            return;
                        var index = currLevel-1;
                        if(value.min[index] === undefined){
                            index = value.min.length-1;
                            if(value.min[index] === undefined)
                                return;
                        }
                        if(value.max === undefined){
                            ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min[index] + '</span>\n';
                        } else {
                            ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min[index] + ' ~ ' + value.max + '</span>\n';
                        }
                        ret += "<br>\n";
                    }
                } else if(value.max.constructor === Array){
                    if(value.min == 0)
                        return;
                    var index = currLevel-1;
                    if(value.max[index] === undefined){
                        index = value.max.length-1;
                        if(value.max[index] === undefined)
                            return;
                    }
                    ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min + ' ~ ' + value.max[index] + '</span>\n';
                    ret += "<br>\n";
                } else {
                    if(value.max === undefined){
                        ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min + '</span>\n';
                    } else {
                        ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value.min + ' ~ ' + value.max + '</span>\n';
                    }
                    ret += "<br>\n";
                }
            } else {
                ret += '<span class="skillAttribute" style="color: gray">' + key + ': \n' + value + '</span>\n';
                ret += "<br>\n";
            }
        });
    } else if(isSkill)
        document.getElementById(skill.name).getElementsByClassName("buttonText")[0].style.color = "white";
    
    ret += '<br>\n';
    
    if(currLevel+1 <= skill.attributes["skillMaxLevel"]){
        ret += '<span class="nextLevel">Next Level: ' + (currLevel+1) + '</span>\n';
    } else if(currLevel+1 <= skill.attributes["skillUltimateLevel"]){
        ret += '<span class="nextLevel" style="color: yellow">Next Level: ' + (currLevel+1) + '</span>\n';
    } else {
        return ret;
    }
    ret += '<br>\n';
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        if(key == "requiredWeapons")
            return;
        if(key == "skillMaxLevel")
            return;
        if(key == "skillUltimateLevel")
            return;
        var value = skill.attributes[key];
        if(value.constructor === Array){
            if(value[currLevel] == 0 || value[currLevel] === undefined)
                return;
            ret += '<span class="skillAttribute">' + key + ': \n' + value[currLevel] + '</span>\n';
            ret += "<br>\n";
        } else if(value.constructor === Object) {
            if(value.min.constructor === Array){
                if(value.min[currLevel] == 0 || value.min[currLevel] === undefined)
                    return;
                if(value.min[currLevel] === undefined){
                    index = value.min.length-1;
                    if(value.min[index] === undefined)
                        return;
                }
                if(value.max === undefined){
                    ret += '<span class="skillAttribute">' + key + ': \n' + value.min[currLevel] + '</span>\n';
                } else {
                    ret += '<span class="skillAttribute">' + key + ': \n' + value.min[currLevel] + ' ~ ' + value.max[currLevel] + '</span>\n';
                }
                ret += "<br>\n";
            } else {
                if(value.max === undefined){
                    ret += '<span class="skillAttribute">' + key + ': \n' + value.min + '</span>\n';
                } else {
                    ret += '<span class="skillAttribute">' + key + ': \n' + value.min + ' ~ ' + value.max + '</span>\n';
                }
                ret += "<br>\n";
            }
        } else if(currLevel == 0) {
            ret += '<span class="skillAttribute">' + key + ': \n' + value + '</span>\n';
            ret += "<br>\n";
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
}