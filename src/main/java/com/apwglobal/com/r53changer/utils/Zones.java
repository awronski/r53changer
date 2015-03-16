package com.apwglobal.com.r53changer.utils;

import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListHostedZonesRequest;
import com.amazonaws.services.route53.model.ListHostedZonesResult;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

public class Zones {

    public static Set<HostedZone> hostedZones(AmazonRoute53Client client) {
        return hostedZones(client, p -> true);
    }

    public static Set<HostedZone> hostedZones(AmazonRoute53Client client, Predicate<HostedZone> filter) {
        return hostedZones(client, client.listHostedZones(), new HashSet<>())
                .stream()
                .filter(filter)
                .collect(toSet());
    }

    private static Set<HostedZone> hostedZones(AmazonRoute53Client client, ListHostedZonesResult result, Set<HostedZone> zones) {
        zones.addAll(result.getHostedZones());

        return result.isTruncated()
                ? hostedZones(client, nextResult(client, result), zones)
                : zones;
    }

    private static ListHostedZonesResult nextResult(AmazonRoute53Client client, ListHostedZonesResult result) {
        return client.listHostedZones(new ListHostedZonesRequest().withMarker(result.getNextMarker()));
    }

}
