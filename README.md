Demo Signal server i bot implementacija

Za pokretanje potrebno je instalirati Java 8 JDK, Maven, PostgreSQL, Wildfly 10+/EAP7.1.

U fajl `WILDFLY_DIR/standalone/configuration/standalone.xml` u `urn:jboss:domain:datasources:5.0` potrebno dodati:
```
<subsystem xmlns="urn:jboss:domain:datasources:5.0">
...        
    <datasource jta="true" jndi-name="java:jboss/datasources/signalDS" pool-name="diplomskiclient" enabled="true" use-ccm="true">                 <connection-url>jdbc:postgresql://localhost:5432/IME_BAZE</connection-url>
        <driver>postgresql</driver>
        <security>
          <user-name>postgres</user-name>
          <password>SIFRA</password>
        </security>
    </datasource>
...
   <drivers>
     ...
      <driver name="postgresql" module="org.postgresql">
        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
      </driver>
     ...
   </drivers>
</subsystem>
```
Zamijeniti `SIFRA` sa odgovarajucom lozinkom postgres korisnika u koliko postoji!

Potrebno preuzeti odgovarajucu verziju JDBC drajvera sa https://jdbc.postgresql.org/download.html, i instalirati na sledeći nacin:

Kreirati putanju `WILDFLY_DIR/modules/system/layers/base/org/postgresql/main/`. U koliko neki folderi ne postoje kreirati ih.

U folderu main prebaciti preuzeti .jar driver i kreirati fajl modules.xml, u koji treba upisati:
```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
   <resources>
     <resource-root path="NAZIV_JAR_FAJLA.jar"/>
   </resources>
   <dependencies>
     <module name="javax.api"/>
     <module name="javax.transaction.api"/>
   </dependencies>
</module>
```
Izmijeniti NAZIV_JAR_FAJLA iz `modules.xml` u ime preuzeto .jar fajla.

Projekat se kompajlira na sledeci nacin:
U direktorijumu ovog projekta, gdje se nalazi pom.xml potrebno pokrenuti `maven clean package`;
Kopirati .war fajl iz `target` foldera u `WILDFLY_DIR/standalone/deployments`
Server pokrenuti pomocu: `WILDFLY_DIR/bin/standalone.sh -b 0.0.0.0`
