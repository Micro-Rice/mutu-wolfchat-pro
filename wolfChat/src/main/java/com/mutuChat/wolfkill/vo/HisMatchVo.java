package com.mutuChat.wolfkill.vo;

public class HisMatchVo {
    private String roleName;
    private int winLose;
    private String time;
    private int leveladd;
    private String achiveName;
    private int matchId;
    
    public int getMatchId() {
        return matchId;
    }
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getWinLose() {
        return winLose;
    }
    public void setWinLose(int winLose) {
        this.winLose = winLose;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getLeveladd() {
        return leveladd;
    }
    public void setLeveladd(int leveladd) {
        this.leveladd = leveladd;
    }
    public String getAchiveName() {
        return achiveName;
    }
    public void setAchiveName(String achiveName) {
        this.achiveName = achiveName;
    }
    
}
