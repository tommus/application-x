Application-X REST API
----------------------

This application has been developed as a part of recruitment process.

Compatibility
-------------

An application has been tested using `Java(TM) SE Runtime Environment (build 1.8.0_91-b14)`.
Despite this I encourage to test it on other versions of JVM.

Project dependencies
--------------------

This project uses:

- [Spring Framework](https://projects.spring.io/spring-framework/)
- [Hibernate Persistence](http://hibernate.org/)
- [Jackson Serialization Library](https://github.com/FasterXML/jackson)
- [H2 Database Engine](http://www.h2database.com/html/main.html)
- [Joda Time Provider](http://www.joda.org/joda-time/)
- [Jadira Datatypes for Hibernate](http://jadira.sourceforge.net/)

Compilation
-----------

Application can be compiled using `Gradle` build system. 

1. Open project directory:

    > cd \<project-dir\>

2. Compile project:

    > gradle build

3. Execute:

    > java -jar build/libs/gs-rest-service-0.1.0.jar

4. To run unit tests, just type:

    > gradle test

Above has been assumed that, the alternatives to `gradle` and `java` executables are
available in system path.

Goals
-----

First evaluation consists of implementing the following:

- an endpoint that allows to receive available TV stations,
- an endpoint that allows to receive details information about selected TV station,
- an endpoint that allows to receive a list of ongoing programs at given time.

Second evaluation assumes the following updates:

- an implementation of other CRUD (exactly: C, U, D) methods for programs manipulation,
- an implementation of unit tests for service,
- optimization of `program` table.

Endpoints
---------

Detailed description of all endpoints is available here:

- [stations](docs/stations.md)
- [schedules](docs/schedules.md)
- [programs](docs/programs.md)
- [shows](docs/shows.md)

Pretty print
------------

For pritty-printing `curl`'s responses (depends on your needs) you can install:

* python
* python-pygments
* xmllint

And pipe commands (depends on format):

> curl "http://localhost:8080/stations.json" | python -mjson.tool | pygmentize -l json

> curl "http://localhost:8080/stations.xml" | xmllint --format - | pygmentize -l xml

Next steps
----------

The next steps that should be made:

- Providing additional validation levels (ex. do not allow to schedule programs that overlaps).
- Implementation of Docker's scripts that allows to simplify deployment.
- For production case - H2 database should be replaced with other standalone SQL DB.
