package main;

public class SkillAttribute<T> {

    private T value;

    public SkillAttribute(T aValue) {
        this.value = aValue;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
