package com.apwglobal.com.r53changer;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.model.*;
import com.apwglobal.com.r53changer.utils.ZoneRecordPairs;
import com.apwglobal.com.r53changer.utils.Zones;

import java.io.PrintStream;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Changer {

    private AmazonRoute53Client client;
    private Predicate<HostedZone> zonePredicate = p -> true;
    private Predicate<ResourceRecordSet> recordPredicate = p -> true;
    private Predicate<ResourceRecord> valuePredicate = p -> true;
    private Function<ResourceRecordSet,ResourceRecordSet> change;
    private PrintStream printStream;
    private boolean dryRun;

    private Changer(String key, String secret) {
        this.client = new AmazonRoute53Client(new BasicAWSCredentials(key, secret));
        this.printStream = System.out;
    }

    public static Changer create(String key, String secret) {
        return new Changer(key, secret);
    }

    public Changer zone(Predicate<HostedZone> predicate) {
        this.zonePredicate = predicate;
        return this;
    }

    public Changer record(Predicate<ResourceRecordSet> predicate) {
        this.recordPredicate = predicate;
        return this;
    }

    public Changer value(Predicate<ResourceRecord> predicate) {
        this.valuePredicate = predicate;
        return this;
    }

    public Changer change(Function<ResourceRecordSet, ResourceRecordSet> change) {
        this.change = change;
        return this;
    }

    public void dryRun() {
        this.dryRun = true;
        apply();
    }

    public void dryRun(PrintStream printStream) {
        this.printStream = printStream;
        this.dryRun = true;
        apply();
    }

    public void apply() {
        Set<HostedZone> zones = Zones.hostedZones(client, zonePredicate);
        ZoneRecordPairs.pair(zones, client)
                .stream()
                .filter(filter())
                .forEach(pair -> doChange(pair.getZone().getId(), pair.getRecord()));

        client.shutdown();
    }



    private void doChange(String zoneId, ResourceRecordSet record) {
        ChangeBatch changeBatch = new ChangeBatch().withChanges(new Change(ChangeAction.DELETE, record));
        executeChange(zoneId, changeBatch);

        changeBatch = new ChangeBatch().withChanges(new Change(ChangeAction.CREATE, change.apply(record)));
        executeChange(zoneId, changeBatch);
    }

    private void executeChange(String zoneId, ChangeBatch changeBatch) {
        if (dryRun) {
            printStream.println(changeBatch);
        } else {
            client.changeResourceRecordSets(new ChangeResourceRecordSetsRequest(zoneId, changeBatch));
        }
    }

    private Predicate<ZoneRecordPair> filter() {
        return ((Predicate<ZoneRecordPair>) pair -> recordPredicate.test(pair.getRecord()))
                .and(pair -> pair.getRecord().getResourceRecords().stream().anyMatch(valuePredicate));
    }

}
