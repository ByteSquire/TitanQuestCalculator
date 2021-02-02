function getAttributeStringWithColour(key, value, index, colour){
    return '<span class="skillAttribute" style="color: ' + colour + '">' + getAttributeString(key, value, index, colour) + '</span>';
}

function getAttributeString(key, value, index, colour){
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
        else if(value.values)
            return formatChanceBasedAttributes(key, value, index, colour);
        else
            return formatSkillAttribute(key, value, index);
    }
    
    return formatAttribute(ret, value, index);
}

function formatSkillAttribute(key, value, index){
    var ret = key;
    if(value.chance)
        ret = ret.replace("${chance}", formatCurrValue(value.chance, index));
    if(value.duration){
        if(value.value)
            ret = ret.replace("${value}", formatCurrValueScaled(value.value, value.duration, index));
        ret = ret.replace("${duration}", formatCurrValue(value.duration, index));
    } else if(value.value)
        ret = ret.replace("${value}", formatCurrValue(value.value, index));
    return ret;
}

function formatCurrValue(value, index){
    var ret = getCurrValue(value, index);
    if(ret.constructor === Object){
        return ret.min + " ~ " + ret.max;
    } else
        return ret;
}

function formatCurrValueScaled(value, duration, index){
    var ret = getCurrValueScaled(value, duration, index);
    if(ret.constructor === Object){
        return ret.min + " ~ " + ret.max;
    } else
        return ret;
}

function formatChanceBasedAttributes(key, value, index, colour){
    var ret = "";
    ret += '<div style="indent-text: inherit">';
    if(colour == "gray")
        ret += key.replace("${chance}", getCurrValue(value.chance, index));
    else
        ret += '<span style="color: #DE5825">' + key.replace("${chance}", getCurrValue(value.chance, index)) + '</span>\n';

    var attrKeys = Object.keys(value.values);
    attrKeys.forEach((attrKey) => {
        ret += '<div class="indentedAttribute">';
        if(colour == "gray")
            ret += getAttributeString(attrKey, value.values[attrKey], index);
        else
            ret += '<span style="color: lightskyblue">' + getAttributeString(attrKey, value.values[attrKey], index) + '</span>\n';
        ret += "</div>";
        //ret += '<br>\n';
    });
    ret += '</div>';
    //ret = ret.substring(0, ret.length-"<br>\n".length);
    return ret;
}

function formatAttribute(key, value, index){
    var ret = key;
    if(key.includes("${value}"))
        ret = ret.replace("${value}", getCurrValue(value, index));
    else
        ret = getCurrValue(value, index) + ret;
    if(ret.includes("+-"))
        ret = ret.replace("+-", "-");
    return ret;
}

function formatAttributeMinMax(key, valueMin, valueMax, index){
    var ret = key;
    
    if(key.includes("${value}"))
        ret = key.replace("${value}", getCurrValue(valueMin, index) + " ~ " + getCurrValue(valueMax, index));
    else
        ret = getCurrValue(valueMin, index) + " ~ " + getCurrValue(valueMax, index) + key;
            
    if(ret.includes("+-"))
        ret = ret.replaceAll("+-", "-");
    return ret;
}

function getCurrValueMinMax(valueMinMax, index){
    return { "min": getCurrValue(valueMinMax.min, index), "max": getCurrValue(valueMinMax.max, index)};
}

function getCurrValue(value, index){
    var ret = value;
    if(ret.min){
        if(ret.max)
            return getCurrValueMinMax(value, index);
        else
            ret = ret.min;
    } else if(ret.max)
        ret = ret.max;
    if(ret.constructor === Array){
        var _index = index;
        if(_index >= ret.length)
            _index = ret.length-1;
        ret = ret[_index];
    }
    return ret;
}

function getCurrValueScaled(value, duration, index){
    var scaledValue = value;
    var ret = getCurrValue(value, index);
    var scaleFactor = getCurrValue(duration, index);
    if(scaleFactor.constructor === Object){
        ret = { "min": getCurrValueScaled(ret, scaleFactor.min, index), "max": getCurrValueScaled(ret, scaleFactor.max, index) };
    } else if(scaleFactor != 1.0){
        if(ret.constructor === Object){
            ret = { "min": getCurrValueScaled(ret.min, scaleFactor, index), "max": getCurrValueScaled(ret.max, scaleFactor, index) };
        } if(ret.constructor === Array){
            var _index = index;
            if(_index >= ret.length)
                _index = ret.length-1;
            scaledValue = ret[_index] * scaleFactor;
        } else
            scaledValue *= scaleFactor;
    }
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
    var ret = '<span style="color: brown">Pet Skills:</span>\n';
    ret += '<table>\n<tr style="vertical-align: top">';
    var i = 0;
    skills.forEach((key) => {
        var skill = value.petSkills[key];
        if(value.petSkillLevels[key] === undefined){
            return;
        }
        if(i !=0 && i%4 == 0){
            ret += '<tr style="vertical-align: top">';
        }
        i++;
        ret += '<td style="min-width: fit-content">';
        var tmpIndex = index;
        if(tmpIndex >= value.petSkillLevels[key].length)
            tmpIndex = value.petSkillLevels[key].length-1;
        if(value.petSkillLevels[key][tmpIndex] == 0){
            ret += '<span class="title">' + skill.name + '</span>\n';
            ret += '<br>\n';
            ret += '<span class="desc">Pet has not learned this skill yet</span>\n';
            ret += '</td>';
            return;
        }
        ret += getPopupString(skill, value.petSkillLevels[key][tmpIndex], true);
        ret += '</td>';
    });
    ret += '</table>';
    return ret;
}