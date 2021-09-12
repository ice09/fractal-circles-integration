# Fractal Circles Integration Demo Setup
These instructions help to create a demo setup for the Fractal-Circles-Integration.  
The prototypical demo should be used to clarify open questions between Fractal and Circles.  

## Authorization Grant Flow Call Sequence

The sequence below documents a Keycloak Mock for the Fractal OAuth2 Flow, however this demo uses the actual Fractal staging test environment. The flow itself is the same for both demos.

![flow](https://drive.google.com/uc?export=view&id=19DZx6dloY_LGnqKnw5k2yVyrFOyC8i9e)

## Start Authorization Grant Flow with Authentication URL

### Prerequisites

#### Installations

* Java 11+
* Maven 3+

#### Configurations

#### Setup Demo Environment

* Repo branch _fractal_ is cloned locally
* Properties `oauth2.client-id` and `oauth2.client-secret` are set in `application.properties`
* Start Application with `mvn spring-boot:run`

#### Run Demo

* Call Redirect URL Setup Endpoint
* Open the Redirect URL in a Browser and execute the Fractal Flow
* Open the URL in the Email from Fractal
* The result will be your UserID (UID) displayed on the result page
* Use this UID in the POST Request in `fractal-script.http` (see below)
* The Result of this POST Request will be the Transaction Hash of the Transaction `addMember` on the `GroupCurrencyToken` contract

### Testing the Group Currency Token with VS Code

All the steps above are included in the `fractal-script.http` file in root directory.  
To execute the tests, [VS Code](https://code.visualstudio.com/) must be installed with the [REST plugin](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

### Additional Information

#### Flatten & Compile GroupCurrencyToken
* Clone [circles-fork](https://github.com/ice09/circles-contracts) to folder `${circles.git}/contracts`
* Install [remixd-server](https://github.com/ethereum/remix-project/tree/master/libs/remixd)
* Start remixd-server with folder `${circles.git}/contracts` and URL http://remix.ethereum.org/
  * Open  [remix-ide](http://remix.ethereum.org/)
    * Install Flattener in Remix-IDE, Flatten `GroupCurrencyToken.sol`
    * Connect to localhost
    * Load `GroupCurrencyToken_flat.sol`
      * Change `pragma solidity ^0.7.0;` in all `*.sol`
      * Remove "unchecked" from flattened contract
      * Store flattened Solidity file under `/src/main/resources/solidity`
      * Build with `mvn clean package`