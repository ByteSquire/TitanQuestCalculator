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
    }
    
    return formatAttribute(ret, value, index);
}

function formatAttribute(key, value, index){
    var ret = key;
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
        else if(value == 1)
            ret = key;
        else
            ret = value + key;
    }
    if(ret.includes("+-"))
        ret = ret.replace("+-", "-");
    return ret;
}

function formatAttributeMinMax(key, valueMin, valueMax, index){
    var ret = key;
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
    var skills = Object.keys(value);
    var ret = '<table><tr style="vertical-align: top"><td><span style="color: brown">Pet Skills:</span><br><tr style="vertical-align: top">';
    var i = 1;
    skills.forEach((key) => {
        var skill = value[key];
        if(i%4 == 0){
            ret += '<tr style="vertical-align: top">';
        }
        ret += "<td>";
        ret += getPopupString(skill, index);
        ret += "<br>";
        i++;
    });
    return ret;
}