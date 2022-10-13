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

function getSaveLinkNew(){
	var ret = "";
    ret += location.href.split("&config")[0];
    ret += '&config=';
    
    ret += document.getElementById("questPoints").value;
    ret += "-"
    
    ret += document.getElementById("plusButton1").getElementsByClassName("buttonText")[0].innerText.split("/")[0];
    ret += ",";
    ret += document.getElementById("allPlusButton1").innerText.replace(" ", "");
    
    ret += ";"
    
    ret += masteryTiersToArraysNew(m1);
    
    if(m2){
    	ret += "-";
    	
	    ret += document.getElementById("plusButton2").getElementsByClassName("buttonText")[0].innerText.split("/")[0];
	    ret += ",";
	    ret += document.getElementById("allPlusButton2").innerText.replace(" ", "");
	    
	    ret += ";"
	    
	    ret += masteryTiersToArraysNew(m2);
    }
    
    return ret;
}

function saveClicked(){
    var linkText = document.getElementById("saveLink");
    linkText.value = getSaveLinkNew();
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

function masteryTiersToArraysNew(mastery){
	var ret = "";
	mastery.skillTiers.forEach((tier) => {
		ret += getSemicolonSeparatedSkillLevelsInTier(tier);
		ret += ";"
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

function getSemicolonSeparatedSkillLevelsInTier(tier){
	var ret = "";
	tier.forEach((skill) => {
        var skillButton = document.getElementById(skill.name);
        var skillPlusButton = document.getElementById(skill.name + "+");
        ret += skillButton.getElementsByClassName("disabled")[0].innerText;
        ret += ",";
        ret += skillPlusButton.innerText;
        ret += ";";
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

function loadConfigNew(config){
	var majors = config.split("-");
	if (majors.length < 2 || majors.length > 3)
		return false;
	
	var qp = majors[0];
    var dropDown = document.getElementById("questPoints");
    dropDown.value = qp; // should check here if valid
    pointsSelected(dropDown);
    
    var m1Config = majors[1];
    var m1Skills = m1Config.split(";");
    
	var skill = parseInt(m1Skills[0].split(",")[0]);
	var skillPlus = parseInt(m1Skills[0].split(",")[1]);
    for(var i = 0; i < skill; i++){
        plusClicked(document.getElementById("plusButton1"), { "button" : 0 });
    }
    for(var i = 0; i < skillPlus; i++){
        allPlusClicked(document.getElementById("allPlusButton1"), { "button" : 0 });
    }
    
    var j = 0;
    var b = 0;
    for(var i = 1; i < m1Skills.length; i++){
    	var skillTuple = m1Skills[i];
    	skill = skillTuple.split(",")[0];
    	skillPlus = skillTuple.split(",")[1];
    	if (j >= m1.skillTiers.length)
    		return false;
		var skillName = "";
    	if (b < m1.skillTiers[j].length)
        	skillName = m1.skillTiers[j][b++].name;
    	else{
    		b = 0;	
        	skillName = m1.skillTiers[j++][b++].name;
		}
	    for(var k = 0; k < parseInt(skill); k++){
            skillClicked(document.getElementById(skillName), { "button" : 0 });
	    }
	    for(var l = 0; l < parseInt(skillPlus); l++){
            skillPlusClicked(document.getElementById(skillName + "+"), { "button" : 0 });
	    }
    }
    
    if (majors.length == 3){
	    var m2Config = majors[2];
	    var m2Skills = m2Config.split(";");
	    
		skill = parseInt(m2Skills[0].split(",")[0]);
		skillPlus = parseInt(m2Skills[0].split(",")[1]);
	    for(var i = 0; i < skill; i++){
	        plusClicked(document.getElementById("plusButton2"), { "button" : 0 });
	    }
	    for(var i = 0; i < skillPlus; i++){
	        allPlusClicked(document.getElementById("allPlusButton2"), { "button" : 0 });
	    }
	    
	    j = 0;
	    b = 0;
	    for(var i = 1; i < m2Skills.length; i++){
	    	var skillTuple = m2Skills[i];
	    	var skill = skillTuple.split(",")[0];
	    	var skillPlus = skillTuple.split(",")[1];
	    	if (j >= m2.skillTiers.length)
	    		return false;
			var skillName = "";
	    	if (b < m2.skillTiers[j].length)
	        	skillName = m2.skillTiers[j][b++].name;
	    	else{
	    		b = 0;	
	        	skillName = m2.skillTiers[j++][b++].name;
			}
		    for(var k = 0; k < parseInt(skill); k++){
	            skillClicked(document.getElementById(skillName), { "button" : 0 });
		    }
		    for(var l = 0; l < parseInt(skillPlus); l++){
	            skillPlusClicked(document.getElementById(skillName + "+"), { "button" : 0 });
		    }
	    }
    }
    
    return true;
}