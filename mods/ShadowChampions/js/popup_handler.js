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

function skillButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    if( event ) {
        var sx = event.pageX;
        var sy = event.pageY;

        pop.style.top = "" + sy + "px";
        pop.style.left = "" + (sx+10) + "px";
    }
    var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
    var tiers = matteringMastery.skillTiers;
    var skill;
    tiers.forEach((tier) => {
        tier.forEach((aSkill) => {
            if(aSkill.name == button.id){
                skill = aSkill;
            }
        });
    });
    pop.innerHTML = getPopupString(skill, button.innerText.split("/")[0].replaceAll("\n", ""), true);

    pop.style.width = "10%";

    pop.style.display = "block";
}

function plusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    if( event ) {
        var sx = event.pageX;
        var sy = event.pageY;

        pop.style.top = "" + sy + "px";
        pop.style.left = "" + (sx+15) + "px";
    }
    var matteringMastery = (button.parentElement.id == "panel1")? m1 : m2;
    var skill = { attributes: matteringMastery.masteryAttributes, name: matteringMastery.name, description: "" };
    pop.innerHTML = getPopupString(skill, button.innerText.split("/")[0].replaceAll("\n", ""), false);
    
    pop.style.display = "block";
}

function allPlusButtonPopup(button, event){
    var pop = document.getElementById( 'pop' );

    if( event ) {
        var sx = event.pageX;
        var sy = event.pageY;

        pop.style.top = "" + sy + "px";
        pop.style.left = "" + (sx+15) + "px";
    }
    pop.innerHTML = '<span class="title">Adds ' + Number((button.parentElement.id == "panel1")? m1GlobalPlus : m2GlobalPlus) + " to all skills of this mastery</span>";
    
    pop.style.display = "block";
}

function hidePopup(){
    document.getElementById("pop").style.display = "none";
}