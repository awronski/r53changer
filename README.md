# r53changer
r53changer is easy to use library for Amazon Route 53 zones batch updates.

## Install
```
git clone https://github.com/awronski/r53changer.git
cd r53changer
gradle install
```

# Usage
The library can takes up to three condition:
- zone: which zones you want to change
- record: which records inside the zone you want to change
- value: which values inside the records you want to change

# Example
Lets say we want to change TTL and IP address only for the zones that meets below conditions:

1. only the ".com" zones AND
2. only the A records thats start with "www"
3. only with value "111.222.333.444"

To: new TTL 300, new IP 123.123.123.123:

```java
        Changer.create(key, secret)
                .zone(zone -> zone.getName().endsWith(".com"))      //1
                .record(record -> record.getType().equals(RRType.A.name()) && record.getName().startsWith("www."))      //2
                .value(value -> value.getValue().contains("111.222.333.444"))       //3
                .change(new ChangeRecordTtl(300l).andThen(new ChangeRecordValue("123.123.123.123")))    //change
                .apply();
```

# Testing
You can run the updater in the test mode so that the changes will be print on the console but not made on AWS.
```java
        Changer.create(key, secret)
                [..]
                .dryRun();
```


License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
