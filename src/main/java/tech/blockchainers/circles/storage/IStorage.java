package tech.blockchainers.circles.storage;

public interface IStorage {

    String readWalletAddressForUID(String uID);
    void storeWalletAddressForStateId(String stateId, String walletAddress);
    void storeUserIdForStateId(String state, String uid);
}
