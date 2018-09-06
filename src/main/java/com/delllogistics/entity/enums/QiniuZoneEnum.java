package com.delllogistics.entity.enums;

public enum QiniuZoneEnum {
    zone0("华东"),
    zone1("华北"),
    zone2("华南"),
    zoneNa0("北美")
    ;

    private String zoneName;

    QiniuZoneEnum(String zoneName){
        this.zoneName=zoneName;
    }

    public String getZoneName() {
        return zoneName;
    }
}
