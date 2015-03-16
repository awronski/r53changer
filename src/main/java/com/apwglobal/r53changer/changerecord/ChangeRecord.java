package com.apwglobal.r53changer.changerecord;

import com.amazonaws.services.route53.model.ResourceRecordSet;

import java.util.function.Function;

public interface ChangeRecord extends Function<ResourceRecordSet, ResourceRecordSet> {

}
