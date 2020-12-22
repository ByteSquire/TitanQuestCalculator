function getAttributeStringWithColour(key, value, index, colour){
    return '<span style="color: ' + colour + '">' + getAttributeString(key, value, index) + '</span>';
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
                return formatAttribteMinMax(ret, value.min, value.max, index);
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