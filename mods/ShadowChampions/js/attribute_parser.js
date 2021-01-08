function getAttributeStringWithColour(key, value, index, colour){
    return '<span class="skillAttribute" style="color: ' + colour + '">' + getAttributeString(key, value, index) + '</span>';
}

function getAttributeString(key, value, index){
    var ret = key;
    if(key.includes("^a")){
        ret = key.replace("^a", '<span style="color: aqua">')
        ret += "</span>";
    }
    if(key.includes("^o")){
        ret = key.replace("^o", '<span style="color: orange">')
        ret += "</span>";
    }
    
    if(value.constructor === Object){
        if(value.min){
            if(value.max)
                return formatAttributeMinMax(ret, value.min, value.max, index);
            else
                return formatAttribute(ret, value.min, index);
        } else if(value.max)
            return formatAttribute(ret, value.max, index);
        else if(value.value0 || value.value1)
            return formatAttributeWithSecondValue(value, index);
        else if(value.chance)
            return formatChanceBasedAttributes(key, value, index);
    }
    
    return formatAttribute(ret, value, index);
}

function formatAttributeWithSecondValue(value, index){
    var ret = value.key;
    if(value.value0)
        ret = getAttributeString(ret.replace("value0", "value"), value.value0, index);
    if(value.value1)
        ret = getAttributeString(ret.replace("value1", "value"), value.value1, index); 
    return ret;
}

function formatChanceBasedAttributes(key, value, index){
    var ret;
    if(value.chance.constructor === Array){
        if(index >= value.chance.length)
            index = value.chance.length-1;
        ret = '<span>' + key.replace("${value}", value.chance[index]) + '</span>\n';
    } else
        ret = '<span>' + key.replace("${value}", value.chance) + '</span>\n';
    ret += "<br>\n";
    var attrKeys = Object.keys(value.values);
    attrKeys.forEach((attrKey) => {
        ret += '<span>' + getAttributeString(attrKey, value.values[attrKey], index) + '</span>\n';
        ret += '<br>\n';
    });
    ret = ret.substring(0, ret.length-5);
    return ret;
}

function formatAttribute(key, value, index){
    var ret = key;
    if(key.includes("over") && !key.includes("value1")){
        if(key.includes(" ~ ")){
            var dpsMin = key.split(" ~ ")[0];
            var dpsMax = key.split(" ~ ")[1].split(" ")[0];
            if(!isNaN(dpsMin) && !isNaN(dpsMax))
                key = key.replace(dpsMin, (Number(dpsMin)*value).toFixed(1)).replace(dpsMax, (Number(dpsMax)*value).toFixed(1));
        } else {
            var dps = key.split(" ")[0];
            if(!isNaN(dps))
                key = key.replace(dps, (Number(dps)*value).toFixed(1));
        }
    }
    if(value.constructor === Array){
        if(index >= value.length)
            index = value.length-1;
        if(key.includes("${value}"))
            ret = key.replace("${value}", value[index]);
        else
            ret = value[index] + key;
    } else {
        if(key.includes("${value}"))
            ret = key.replace("${value}", value);
        else
            ret = value + key;
    }
    if(ret.includes("+-"))
        ret = ret.replace("+-", "-");
    return ret;
}

function formatAttributeMinMax(key, valueMin, valueMax, index){
    var ret = key;
    if(key.includes("over") && !key.includes("value1")){
        if(key.includes(" ~ ")){
            var dpsMin = key.split(" ~ ")[0];
            var dpsMax = key.split(" ~ ")[1];
            if(!isNaN(dpsMin) && !isNaN(dpsMax))
                key = key.replace(dpsMin, (Number(dpsMin)*valueMin).toFixed(1)).replace(dpsMax, (Number(dpsMax)*valueMax).toFixed(1));
        } else {
            var dps = key.split(" ")[0];
            if(!isNaN(dps))
                key = key.replace(dps, ((Number(dps)*valueMin).toFixed(1) + " ~ " + (Number(dps)*valueMax).toFixed(1)));
        }
    }
    if(valueMin.constructor === Array){
        if(valueMax.constructor === Array){
            var indexMin = index;
            var indexMax = index;
            if(indexMax >= valueMax.length)
                indexMax = valueMax.length-1;
            if(indexMin >= valueMin.length)
                indexMin = valueMin.length-1;
            if(key.includes("${value}"))
                ret = key.replace("${value}", valueMin[indexMin] + " ~ " + valueMax[indexMax]);
            else
                ret = valueMin[indexMin] + " ~ " + valueMax[indexMax] + key;
        } else {
            var indexMin = index;
            if(indexMin >= valueMin.length)
                indexMin = valueMin.length-1;
            if(key.includes("${value}"))
                ret = key.replace("${value}", valueMin[indexMin] + " ~ " + valueMax);
            else
                ret = valueMin[indexMin] + " ~ " + valueMax + key;
        }
    } else {
        if(valueMax.constructor === Array){
            var indexMax = index;
            if(indexMax >= valueMax.length)
                indexMax = valueMax.length-1;
            if(key.includes("${value}"))
                key = key.replace("${value}", valueMin + " ~ " + valueMax[indexMax]);
            else
                ret = valueMin + " ~ " + valueMax[indexMax] + key;
        } else {
            if(key.includes("${value}"))
                ret = key.replace("${value}", valueMin + " ~ " + valueMax);
            else
                ret = valueMin + " ~ " + valueMax + key;
        }
    }
    
    if(ret.includes("+-"))
        ret = ret.replaceAll("+-", "-");
    return ret;
}

function formatPetAttributes(value, index, colour) {
    var attrs = Object.keys(value);
    var ret = '<span style="color: brown">Pet Attributes:</span><br>';
    attrs.forEach((key) => {
        ret += getAttributeStringWithColour(key, value[key], index, colour).replace("+", "");
        ret += "<br>";
    });
    return ret;
}

function formatPetSkills(value, index, colour) {
    var skills = Object.keys(value.petSkills);
    var ret = '<span style="color: brown">Pet Skills:</span>';
    ret += '<table><tr style="vertical-align: top">';
    var i = 0;
    skills.forEach((key) => {
        var skill = value.petSkills[key];
        if(i !=0 && i%4 == 0){
            ret += '<tr style="vertical-align: top">';
        }
        i++;
        ret += "<td>";
        if(value.petSkillLevels[key] === undefined){
            ret += '<span class="title">' + skill.name + '</span>\n';
            ret += '<br>\n';
            ret += '<span class="desc">Boni depend on the points you invested</span>\n';
            return;
        }
        var tmpIndex = index;
        if(tmpIndex >= value.petSkillLevels[key].length)
            tmpIndex = value.petSkillLevels[key].length-1;
        ret += getPopupString(skill, value.petSkillLevels[key][tmpIndex], true);
    });
    ret += '</table>';
    return ret;
}