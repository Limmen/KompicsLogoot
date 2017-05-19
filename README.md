# Conflict free p2p replicated datatypes

**Course project, id2210-vt17**: 

Java-Implementation of Logoot-Undo: *Distributed Collaborative Editing System on P2P networks*

## Installation

> The pom file contains the shade plugin which will create a new jar when you run the install phase. The jar is located in the target forlder and has "-shaded.jar" sufix

```sh
mvn clean install
```
> Build jar without running tests:

```sh
mvn install -DskipTests
```

## How to run Simulations

> Run simulation from main-class (simple boot scenario that takes ~23s):

```sh
java -jar target/app-1.0-SNAPSHOT-shaded.jar
```

> Run simulation-test from commandline:

```sh
mvn -Dtest={TestFileName}#{TestMethodName} test
```

### Logs

Output of simulations are written to log-files in root-dir. 
Such as simulation.log, simulation-2017-05-19.0.log, simulation-2017-05-19.1.log etc. 

For configuration see `src/main/resources/logback.xml`
### Running Test Examples

> bestEffortDeliveryTest
```sh
mvn -Dtest=BEBTest#bestEffortDeliveryTest test
```

> causalOrderDeliveryTest
```sh
mvn -Dtest=CBTest#causalOrderDeliveryTest test
```

> reliableDeliveryTest with basic churn
```sh
mvn -Dtest=se.kth.tests.rb_test.basic_churn_test.RBTest#reliableDeliveryTest test
```

> reliableDeliveryTest with more churn
```sh
mvn -Dtest=se.kth.tests.rb_test.rb_delivery_test.RBTest#reliableDeliveryTest test
```

> insertionsCommuteTest
```sh
mvn -Dtest=LogootInsertTest#insertionsCommuteTest test
```

> deletionsCommuteTest
```sh
mvn -Dtest=LogootDeleteTest#deletionsCommuteTest() test
```

> redoTest
```sh
mvn -Dtest=LogootRedoTest#redoTest() test
```

> undoTest
```sh
mvn -Dtest=LogootUndoTest#undoTest test
```

> cemeteryTest
```sh
mvn -Dtest=LogootCemeteryTest#cemeteryTest test
```

> Logoot unit tests
```sh
mvn '-Dtest=se.kth.tests.logoot.unit_tests.*Test' test
```

## Authors

Template provided by 

Alex Ormenisan, aaor@kth.se

Implementation and tests written by

Kim Hammar, kimham@kth.se

Maxime Dufour, maximed@kth.se
