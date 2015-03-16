package com.apwglobal.com.r53changer.changerecord;

import com.amazonaws.services.route53.model.*;

public class ChangeRecordTtl implements ChangeRecord {

    private Long ttl;

    public ChangeRecordTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public ResourceRecordSet apply(ResourceRecordSet record) {
        return record.withTTL(ttl);
    }

}
