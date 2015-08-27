package ixcode.openam.server;

public class OpenAmSession {

    private final OpenAmClient client;
    private final OpenAmTokenId tokenId;

    public OpenAmSession(OpenAmClient client, OpenAmTokenId tokenId) {
        this.client = client;
        this.tokenId = tokenId;
    }

    public void logout()  {
        client.logout(tokenId);
    }

    public String tokenId() {
        return tokenId.toString();
    }

}
