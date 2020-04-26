## Differences between SQLite and Postgres
We use SQLite locally because it can create a db in memory without the need for an sql server.

Connecting to the Heroku Postgres server is not recommended because it could mess with with live db as well possible
connection issues due to Heroku periodically changing the username/password/location of the db. 
##
There are small differences between SQLite and Postgres.
You can work out which one you by putting this at the top of the class:
```
    @Value("${spring.profiles.active}")
    private String activeProfile;
```
This will either store "prod" for Postgresql, "dev" for SQLite, or "test" for SQLite with config specific to testing

Main differences found so far:
- Primary keys in Postgresql need to be of type "serial" during creation. This just an auto incrementing Integer. 
They can be used in other tables as Integers
- Primary keys in SQLite work fine as Integers
- Timestamps use the datatype "TIMESTAMP" in Postgres. They use the datatype "DATETIME" in SQLite