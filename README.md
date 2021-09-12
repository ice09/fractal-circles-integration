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
* [ngrok](https://ngrok.com/) for external access
* [Hardhat](https://hardhat.org/) for local Ethereum node

#### Configurations

#### Setup Demo Environment

* Repo branch _fractal_ is cloned locally
* Properties `oauth2.client-id` and `oauth2.client-secret` are set in `application.properties`
* Properties can be retrieved from Fractal developer dashboard

#### Enable external Access

* Use [ngrok](https://ngrok.com/) to connect an external URL to the service on port 8989
* Set Property `oauth2.redirect-url` with the URL + "/auth"

#### Start local Hardhat

* Start [Hardhat](https://hardhat.org/) local node with the default mnemonic `test test test test test test test test test test test junk`

#### Run Demo

* Start Application with `mvn spring-boot:run`
* Call Redirect URL Setup Endpoint (see `fractal-script.http`: GET http://localhost:8989/createRedirect)
* Open the Redirect URL in a Browser and execute the Fractal Flow
* Open the URL in the Email from Fractal
* The result of the call will be your UserID (UID) displayed on the result page
* Use this UID in the POST Request in `fractal-script.http`
* The Result of this POST Request will be the Transaction Hash of executing `GroupCurrencyToken.addMember`

### Testing the Group Currency Token with VS Code

All the steps above are included in the `fractal-script.http` file in root directory.  
To execute the tests, [VS Code](https://code.visualstudio.com/) must be installed with the [REST plugin](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

### Known Issues

* The signed Secret Token in the Webhook callback is not verified.
* For simplicity, it is assumed that the callback is always an approval (no rejection) and has to be called for whitelisting.

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