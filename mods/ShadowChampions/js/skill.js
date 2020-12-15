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
        ret += '<span class="" style="color: yellow">Only works with: ' + skill.attributes["requiredWeapons"] + '</span>';
        ret += "<br>\n";
    }
    
    if(currLevel > 0){
        ret += '<br>\n';
        if(currLevel <= skill.attributes["skillMaxLevel"]){
            if(isSkill)
                document.getElementById(skill.name).getElementsByClassName("buttonText")[0].style.color = "white";
            ret += '<span class="nextLevel">Level: ' + currLevel + '</span>\n';
        } else if(currLevel <= skill.attributes["skillUltimateLevel"]){
            if(isSkill)
                document.getElementById(skill.name).getElementsByClassName("buttonText")[0].style.color = "yellow";
            ret += '<span class="nextLevel"><div style="color: yellow">Level: ' + currLevel + '</div></span>\n';
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
                ret += '<span class="skillAttribute" style="color: gray">' + key + ':<br>\n' + value[index] + '</span>\n';
                ret += "<br>\n";
            } else {
                ret += '<span class="skillAttribute" style="color: gray">' + key + ':<br>\n' + value + '</span>\n';
                ret += "<br>\n";
            }
        });
    } else if(isSkill)
        document.getElementById(skill.name).getElementsByClassName("buttonText")[0].style.color = "white";
    
    ret += '<br>\n';
    
    if(currLevel+1 <= skill.attributes["skillMaxLevel"]){
        ret += '<span class="nextLevel">Next Level: ' + (currLevel+1) + '</span>\n';
    } else if(currLevel+1 <= skill.attributes["skillUltimateLevel"]){
        ret += '<span class="nextLevel"><div style="color: yellow">Next Level: ' + (currLevel+1) + '</div></span>\n';
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
            ret += '<span class="skillAttribute">' + key + ':<br>\n' + value[currLevel] + '</span>\n';
            ret += "<br>\n";
        } else if(currLevel == 0) {
            ret += '<span class="skillAttribute">' + key + ':<br>\n' + value + '</span>\n';
            ret += "<br>\n";
        }
    });
    ret += '<br>';
    return ret;
}