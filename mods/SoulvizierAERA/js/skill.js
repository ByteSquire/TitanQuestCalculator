var skill = {
    var name;
    var parent;
    var attributes;
    var description;
    var currentLevel;
    var maxLevel;
    var ultimateLevel;
}

var skills;

function Skill(skill){
    name = skill.name;
    parent = skill.parent;
    description = skill.description;
    currentLevel = 0;
    
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        if(key == "skillMaxLevel"){
            maxLevel = skill.attributes[key].value;
            return;
        }
        if(key == "skillUltimateLevel"){
            ultimateLevel = skill.attributes[key].value;
            return;
        }
        if(key != "skillMasteryLevelRequired"){
            attributes.put(key, skill.attributes[key].value);
        }
    });
}

function getSkillString(){
    var ret = "";
    ret += '<span class="title">' + name + '</span>\n';
    ret += "<br>\n";
    ret += '<span class="desc">' + description.replaceAll("{^n}", "<br>\n") + '</span>\n';
    ret += "<br>\n";
    ret += "<br>\n";
    ret += '<span class="nextLevel">Next Level: ' + (currentLevel+1) + '</span>\n';
    ret += '<br>\n';
    
    attributes.forEach((key) => {
        ret += '<span class="skillAttribute">' + key + ': ' + attributes[key] + '</span>\n';
        ret += "<br>\n";
    });
    ret += '<br>';
    return ret;
}