package com.apwglobal.com.r53changer.changerecord;

import com.amazonaws.services.route53.model.ResourceRecord;
import com.amazonaws.services.route53.model.ResourceRecordSet;

import java.util.function.Predicate;

public class ChangeRecordValue implements ChangeRecord {

    private String value;
    private Predicate<ResourceRecord> filter = p -> true;

    public ChangeRecordValue(String value) {
        this.value = value;
    }

    public ChangeRecordValue(String value, Predicate<ResourceRecord> filter) {
        this.value = value;
        this.filter = filter;
    }

    @Override
    public ResourceRecordSet apply(ResourceRecordSet record) {
        record
                .getResourceRecords()
                .stream()
                .filter(filter)
                .forEach(value -> value.setValue(this.value));

        return record;
    }

}
