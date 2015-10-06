package ixcode.openam.client;

public class OpenAmSession {

    private final OpenAmClient client;
    private final OpenAmTokenId tokenId;

    public OpenAmSession(OpenAmClient client, OpenAmTokenId tokenId) {
        this.client = client;
        this.tokenId = tokenId;
    }

    public void logout()  {
        if (!client.logout(this)) {
            throw new RuntimeException("Could not log out successfully (see logs)");
        }
    }

    public String tokenId() {
        return tokenId.toString();
    }

}
