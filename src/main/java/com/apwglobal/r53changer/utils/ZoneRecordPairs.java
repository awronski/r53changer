package com.apwglobal.r53changer.utils;

import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.apwglobal.r53changer.ZoneRecordPair;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ZoneRecordPairs {

    public static List<ZoneRecordPair> pair(Set<HostedZone> zones, AmazonRoute53Client client) {
        return zones
                .stream()
                .flatMap(zone -> getResourceRecordSets(client, zone.getId())
                                .stream()
                                .map(record -> new ZoneRecordPair(zone, record)))
                .collect(toList());
    }

    private static List<ResourceRecordSet> getResourceRecordSets(AmazonRoute53Client client, String zoneId) {
        ListResourceRecordSetsRequest request = new ListResourceRecordSetsRequest(zoneId);
        return client.listResourceRecordSets(request).getResourceRecordSets();
    }
}
