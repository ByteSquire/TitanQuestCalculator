package main;

public class SkillAttribute<T> {

    private T mValue;

    public SkillAttribute(T aValue) {
        this.mValue = aValue;
    }

    @Override
    public String toString() {
        return mValue.toString();
    }
}
