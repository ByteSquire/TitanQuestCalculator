package de.bytesquire.titanquest.tqcalculator.main;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({})
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "castLevels", "castSkills" })
public class SkillCast {

    private Object mCastLevels;
    private List<Skill> mCastSkills;

    public SkillCast(Object castLevels, List<Skill> castSkills) {
        mCastLevels = castLevels;
        mCastSkills = castSkills;
    }

    public Object getCastLevels() {
        return mCastLevels;
    }

    public List<Skill> getCastSkills() {
        if (mCastSkills.size() == 0)
            return null;
        List<Skill> ret = new ArrayList<>();
        for (Skill skill : mCastSkills)
            if (!ret.contains(skill))
                ret.add(skill);
        return ret;
    }
}
