package de.bytesquire.titanquest.tqcalculator.main;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TriggerTypeSerializer.class)
public enum TriggerType {
    ON_ITEM_USE, ON_TARGET_KILLED, ON_TARGET_HIT
}

class TriggerTypeSerializer extends JsonSerializer<TriggerType> {

    @Override
    public void serialize(TriggerType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null)
            return;
        
        String result = null;
        switch (value) {
        case ON_ITEM_USE:
            result = "when an item is used";
            break;
        case ON_TARGET_HIT:
            result = "if the target is hit";
            break;
        case ON_TARGET_KILLED:
            result = "if the target is killed";
            break;
        }
        gen.writeString(result);
    }

}
