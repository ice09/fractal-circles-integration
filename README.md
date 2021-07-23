# Fractal Circles Integration Demo Setup
These instructions help to create a demo setup for the Fractal-Circles-Integration with the Authentication part mocked by Keycloak.  
The prototypical demo should be used to clarify open questions between Fractal and Circles.  

## Authorization Grant Flow Call Sequence
![flow](https://drive.google.com/uc?export=view&id=19DZx6dloY_LGnqKnw5k2yVyrFOyC8i9e)

## Start Authorization Grant Flow with Authentication URL

### Prerequisites

#### Installations

* Java 11+
* Maven 3+
* Keycloak 14+

#### Configurations

##### Keycloak

* Create Realm "circles"
* Create Client "verification"
    * Set "Valid Redirect URL": http://localhost:8989/auth
    * Set "Access-Type" to "Confidential"
        * Copy "client_secret" from "Credentials" tab for later use
    * Set "Create Protocol Mapper": "wallet-address"
      * Set "Mapper Type": "User Attribute"
      * Set "User Attribute", "Claim Name", "Token Claim Name": "wallet_address"
      * Set "Claim JSON Type": "String"
* Create User "circles_user"
    * Add attribute "wallet-address": "0x01"
    * Set non-temporary password for "circles_user"

##### Spring Boot Demo Application

* Set "client_secret" from Keycloak Client as `keycloak.client-secret` in `application.properties`

#### Setup Demo Environment

* Repo is cloned locally
* Keycloak is running at *localhost:8080*
* Keycloak configuration according to steps mentioned (Realm, Client, Users, Mappers)
* *client_secret* (copied from Keycloak Client/Credentials/Secret) was added to `src/main/resources/application.properties`
* Start Application with `mvn spring-boot:run`

#### Run Demo

* Call Authentication URL Demo Setup:
  http://localhost:8080/auth/realms/circles/protocol/openid-connect/auth?client_id=verification&response_type=code&redirect_uri=http://localhost:8989/auth


##### Important URLs

* Real Fractal URL Integration Test: https://next.fractal.id/authorize?client_id=mclp5nB-iLXT2DJhP_Km1pL_JaZveFVw8qg44JQPc0c&redirect_uri=https%3A%2F%2Fgist.githubusercontent.com%2Fice09%2F87509a1ecafd9ddd73e02c6ebc5b005d%2Fraw%2F8b23e325b11d35358fa1ced2de3aff2ddaa9fb2a%2Fanyblock_analytics_sql.sql&response_type=code&scope=contact%3Aread%20verification.light%3Aread%20verification.selfie%3Aread

* Keycloak Mock
    * well-known-URL: http://localhost:8080/auth/realms/circles/.well-known/openid-configuration