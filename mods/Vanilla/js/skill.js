var skills;

function Skill(skill){
    name = skill.name;
    parent = skill.parent;
    description = skill.description;
    currentLevel = 0;
    
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        if(key == "skillMaxLevel"){
            maxLevel = skill.attributes[key];
            return;
        }
        if(key == "skillUltimateLevel"){
            ultimateLevel = skill.attributes[key];
            return;
        }
        if(key != "skillMasteryLevelRequired"){
            attributes.put(key, skill.attributes[key]);
        }
    });
}

function getSkillString(skill, currLevel){
    var ret = "";
    ret += '<span class="title">' + skill.name + '</span>\n';
    ret += "<br>\n";
    ret += '<span class="desc">' + skill.description.replaceAll("{^n}", "<br>\n").replaceAll("{^y}", '<p style="color: yellow">').replaceAll("{^g}", '<p style="color: green">').replaceAll("{^a}", '<p style="color: cyan">').replaceAll("{^w}", '</p>') + '</span>\n';
    ret += "<br>\n";
    ret += "<br>\n";
    if(Number(currLevel)+1 <= skill.attributes["skillMaxLevel"]){
        ret += '<span class="nextLevel">Next Level: ' + (Number(currLevel)+1) + '</span>\n';
        ret += '<br>\n';
    } else if(Number(currLevel)+1 <= skill.attributes["skillUltimateLevel"]){
        ret += '<span class="nextLevel"><div style="color: yellow">Next Level: ' + (Number(currLevel)+1) + '</div></span>\n';
    }
    var attr = Object.keys(skill.attributes);
    attr.forEach((key) => {
        if(skill.attributes[key].constructor === Array){
            ret += '<span class="skillAttribute">' + key + ': ' + skill.attributes[key][currLevel] + '</span>\n';
            ret += "<br>\n";
        } else{
            ret += '<span class="skillAttribute">' + key + ': ' + skill.attributes[key] + '</span>\n';
            ret += "<br>\n";
        }
    });
    ret += '<br>';
    return ret;
}