#### Application-X REST API

This application has been developed as a part of recruitment process.

#### Compatibility

An application has been tested using `Java(TM) SE Runtime Environment (build 1.8.0_91-b14)`.
Despite this I encourage to test it on other versions of JVM.

#### Project dependencies

This project uses:

- [Spring Framework](https://projects.spring.io/spring-framework/)
- [Hibernate Persistence](http://hibernate.org/)
- [Jackson Serialization Library](https://github.com/FasterXML/jackson)
- [H2 Database Engine](http://www.h2database.com/html/main.html)
- [Joda Time Provider](http://www.joda.org/joda-time/)
- [Jadira Datatypes for Hibernate](http://jadira.sourceforge.net/)

#### Compilation

Application can be compiled using `Gradle` build system. 

1. Open project directory:

    > cd \<project-dir\>

2. Compile project:

    > gradle build

3. Execute:

    > java -jar build/libs/gs-rest-service-0.1.0.jar

Above has been assumed that, the alternatives to `gradle` and `java` executables are
available in system path.

##### Goal

Evaluation consists of implementing the following:

- an endpoint that allows to receive available TV stations,
- an endpoint that allows to receive details information about selected TV station,
- an endpoint that allows to receive a list of ongoing programs at given time.

##### Implemented endpoints

A `/stations` endpoint:

> /stations[.json/.xml]

Returns a list of available stations in both formats: JSON or XML.

An example of request:

> curl "http://localhost:8080/stations.json"

An example of response:

    [
        {
            "id": 1,
            "name": "TVP1"
        },
        {
            "id": 2,
            "name": "TVP2"
        },
        {
            "id": 3,
            "name": "Polsat"
        }
    ]


A `/stations/{id}` endpoint:

> /stations/{id}[.json/.xml]

Returns detailed information about selected TV station in both formats: JSON or XML.

An example of request:

> curl "http://localhost:8080/stations/1.json"

An example of response:

    {
        "id": 1,
        "name": "TVP1",
        "schedule": [
            {
                "brief": "Codzienny serwis informacyjny z prognoz\u0105 pogody.",
                "name": "Pogoda",
                "start_time": "2016-05-13T17:00:00+0200"
            },
            {
                "brief": "Popularny program rozrywkowy.",
                "name": "Jaka to melodia?",
                "start_time": "2016-05-13T17:25:00+0200"
            }
        ]
    }

A `/programs` endpoint:

> /programs?time={time}[.json/.xml]

Returns a list of ongoing programs. Related to the given timestamp.

An example of request:

> curl "http://localhost:8080/programs.json?time=`date -I`T21:00:00%2B0200"

An example of response:

    [
        {
            "brief": "Adam organizuje dla Wiktorii imprez\u0119 urodzinow\u0105.",
            "name": "Na dobre i na z\u0142e",
            "time_left": 45,
            "time_passed": 20
        },
        {
            "brief": "Film o dw\u00f3ch bohaterach Marsylii - komisarzu Emilienie i taks\u00f3wkarzu Danielu.",
            "name": "Taxi",
            "time_left": 40,
            "time_passed": 20
        }
    ]

It is worth to notice that, query parameter `time` should be escaped [ISO8601](https://www.w3.org/TR/NOTE-datetime) - formatted string.

##### Pretty print

For pritty-printing `curl`'s responses (depends on your needs) you can install:

* python
* python-pygments
* xmllint

And pipe commands (depends on format):

> curl "http://localhost:8080/stations.json" | python -mjson.tool | pygmentize -l json

> curl "http://localhost:8080/stations.xml" | xmllint --format - | pygmentize -l xml

##### Next steps

The next steps that should be made:

- Implementation of unit tests.
- Implementation of Docker's scripts that allows to simplify deployment.
- For production case - H2 database should be replaced with other standalone SQL DB.
