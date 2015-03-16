package com.apwglobal.com.r53changer;

import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ResourceRecordSet;

public class ZoneRecordPair {
    private HostedZone zone;
    private ResourceRecordSet record;

    public ZoneRecordPair(HostedZone zone, ResourceRecordSet record) {
        this.zone = zone;
        this.record = record;
    }

    public HostedZone getZone() {
        return zone;
    }

    public ResourceRecordSet getRecord() {
        return record;
    }
}
