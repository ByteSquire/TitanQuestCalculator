package de.bytesquire.titanquest.tqcalculator.main;

public class SkillIcon {

    private String mName;
    private Integer mPosX, mPosY;
    private boolean mCircular;

    public SkillIcon() {

    }

    public SkillIcon(String aName, Integer aPosX, Integer aPosY, boolean aCircular) {
        setName(aName);
        setPosX(aPosX);
        setPosY(aPosY);
        setCircular(aCircular);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Integer getPosX() {
        return mPosX;
    }

    public void setPosX(Integer mPosX) {
        this.mPosX = mPosX;
    }

    public Integer getPosY() {
        return mPosY;
    }

    public void setPosY(Integer mPosY) {
        this.mPosY = mPosY;
    }

    public boolean isCircular() {
        return mCircular;
    }

    public void setCircular(boolean mIsCircular) {
        this.mCircular = mIsCircular;
    }
}
