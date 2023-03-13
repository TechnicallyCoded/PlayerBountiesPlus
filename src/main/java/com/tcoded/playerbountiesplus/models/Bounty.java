package com.tcoded.playerbountiesplus.models;

public class Bounty {

    private String targetUUID;
    private String bountyCreator;
    private int bountyValue;

    public Bounty(String targetUUID, String bountyCreator, int bountyValue){
        this.targetUUID = targetUUID;
        this.bountyCreator = bountyCreator;
        this.bountyValue = bountyValue;

    }

    public String getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    public String getBountyCreator() {
        return bountyCreator;
    }

    public void setBountyCreator(String bountyCreator) {
        this.bountyCreator = bountyCreator;
    }

    public int getBountyValue() {
        return bountyValue;
    }

    public void setBountyValue(int bountyValue) {
        this.bountyValue = bountyValue;
    }
}
