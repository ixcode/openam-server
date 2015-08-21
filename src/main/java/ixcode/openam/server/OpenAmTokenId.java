package ixcode.openam.server;

/**
 * Created by jmdb on 21/08/2015.
 */
public class OpenAmTokenId {
    private String tokenId;

    public OpenAmTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        return tokenId;
    }
}
