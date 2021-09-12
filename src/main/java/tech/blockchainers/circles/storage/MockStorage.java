package tech.blockchainers.circles.storage;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MockStorage implements IStorage {

    private Map<String, String> memStorageStateWalletAddress = new HashMap<>();
    private Map<String, String> memStorageStateUserId = new HashMap<>();

    @Override
    public String readWalletAddressForUID(String uID) {
        return "0x0" + memStorageStateUserId.get(uID);
    }

    @Override
    public void storeWalletAddressForStateId(String stateId, String walletAddress) {
        memStorageStateWalletAddress.put(stateId, walletAddress);
    }

    @Override
    public void storeUserIdForStateId(String state, String uid) {
        String walletAddress = memStorageStateWalletAddress.get(state);
        memStorageStateUserId.put(uid, walletAddress);
    }
}
