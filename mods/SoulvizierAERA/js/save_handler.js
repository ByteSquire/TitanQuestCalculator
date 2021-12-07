function getSaveLink(){
    var ret = "";
    ret += location.href.split("&config")[0];
    ret += '&config=';
    
    var params = '{"qp": ' + document.getElementById("questPoints").value;
    params += ",";
    params += '"m1":[';
    params += "[";
    params += document.getElementById("plusButton1").getElementsByClassName("buttonText")[0].innerText.split("/")[0];
    params += ",";
    params += document.getElementById("allPlusButton1").innerText.replace(" ", "");
    params += "]";
    
    params += ",";
    params += masteryTiersToArrays(m1);
    params += "]";
    
    if(m2){
        params += ',"m2":[';
        params += "[";
        params += document.getElementById("plusButton2").getElementsByClassName("buttonText")[0].innerText.split("/")[0];
        params += ",";
        params += document.getElementById("allPlusButton2").innerText.replace(" ", "");
        params += "]";        
        
        params += ",";
        params += masteryTiersToArrays(m2);
        params += "]";
    }
    
    params += '}';
    params = encodeURIComponent(params);
    
    ret += params;
    return ret;
}

function saveClicked(){
    var linkText = document.getElementById("saveLink");
    linkText.value = getSaveLink();
    linkText.parentElement.style.display = "unset";
}

function masteryTiersToArrays(mastery){
    var ret = "";
    mastery.skillTiers.forEach((tier) => {
        ret += "[";
        ret += getCommaSeparatedSkillLevelsInTier(tier);
        ret += "],";
    });
    ret = ret.substring(0, ret.length-1);
    return ret;
}

function getCommaSeparatedSkillLevelsInTier(tier){
    var ret = "";
    tier.forEach((skill) => {
        var skillButton = document.getElementById(skill.name);
        var skillPlusButton = document.getElementById(skill.name + "+");
        ret += "[";
        ret += skillButton.getElementsByClassName("disabled")[0].innerText;
        ret += ",";
        ret += skillPlusButton.innerText;
        ret += "]";
        ret += ",";
    });
    ret = ret.substring(0, ret.length-1);
    return ret;
}

function loadConfig(config){
    if(config.qp){
        var dropDown = document.getElementById("questPoints");
        dropDown.value = config.qp;
        pointsSelected(dropDown);
    }
    if(config.m1){
        for(var i = 0; i < config.m1[0][0]; i++){
            plusClicked(document.getElementById("plusButton1"), { "button" : 0 });
        }
        for(var i = 0; i < config.m1[0][1]; i++){
            allPlusClicked(document.getElementById("allPlusButton1"), { "button" : 0 });
        }
        for(var i = 1; i < config.m1.length; i++){
            for(var j = 0; j < config.m1[i].length; j++){
                var skillName = m1.skillTiers[i-1][j].name;
                for(var k = 0; k < config.m1[i][j][0]; k++){
                    skillClicked(document.getElementById(skillName), { "button" : 0 });
                }
                for(var k = 0; k < config.m1[i][j][1]; k++){
                    skillPlusClicked(document.getElementById(skillName + "+"), { "button" : 0 });
                }
            }
        }
    } else {
        document.getElementById("className").innerHTML += ' <span style="color: red">Error reading config!</span>';
        return;    
    }

    if(config.m2){
        for(var i = 0; i < config.m2[0][0]; i++){
            plusClicked(document.getElementById("plusButton2"), { "button" : 0 });
        }
        for(var i = 0; i < config.m2[0][1]; i++){
            allPlusClicked(document.getElementById("allPlusButton2"), { "button" : 0 });
        }
        for(var i = 1; i < config.m2.length; i++){
            for(var j = 0; j < config.m2[i].length; j++){
                var skillName = m2.skillTiers[i-1][j].name;
                for(var k = 0; k < config.m2[i][j][0]; k++){
                    skillClicked(document.getElementById(skillName), { "button" : 0 });
                }
                for(var k = 0; k < config.m2[i][j][1]; k++){
                    skillPlusClicked(document.getElementById(skillName + "+"), { "button" : 0 });
                }
            }
        }
    }
}