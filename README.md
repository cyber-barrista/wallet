# Wallet

## Architecture

Gradle based app consists of two sub-projects:

+ `db`: Auxiliary subproject. Maintains DB flyway and autogenerates DB ORM
+ `rest`: Main subproject. Maintains DB itself and adds REST API layers on top of that

## Tech stack

+ Platform: JVM
+ Language: Kotlin
+ DB: H2
+ ORM: Exposed
+ DB Connection: HikariCP
+ REST : Ktor

## Usage/Deployment

`Docker` based set up is located under `deploy/`.

### Synopsys (for Linux)

+ Install `docker` and `docker-compose` CLI tools somehow
+ `cd deploy`
+ `sudo chmod +x build-image.sh run-wallet.sh`
+ `./build-image.sh`
+ `docker-compose up -d`

NOTE: H2 mv storage file is mapped to `deploy/db/` so DB is fully persistent.