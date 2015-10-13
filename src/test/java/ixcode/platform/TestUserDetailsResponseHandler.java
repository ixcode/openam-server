package ixcode.platform;

import org.junit.Test;

import java.util.Map;

public class TestUserDetailsResponseHandler {

    @Test
    public void parsesDetails() {
        String userDetails = "userdetails.token.id=***token***userdetails.role=id=workers,ou=group,dc=openam,dc=forgerock,dc=orguserdetails.role=id=adminworkers,ou=group,dc=openam,dc=forgerock,dc=orguserdetails.attribute.name=uiduserdetails.attribute.value=demouserdetails.attribute.name=mailuserdetails.attribute.value=demo@loan.example.comuserdetails.attribute.name=inetUserStatususerdetails.attribute.value=Activeuserdetails.attribute.name=createTimestampuserdetails.attribute.value=20150921165137Zuserdetails.attribute.name=dnuserdetails.attribute.value=uid=demo,ou=people,dc=openam,dc=forgerock,dc=orguserdetails.attribute.name=snuserdetails.attribute.value=demouserdetails.attribute.name=cnuserdetails.attribute.value=demouserdetails.attribute.name=modifyTimestampuserdetails.attribute.value=20150928172527Zuserdetails.attribute.name=objectClassuserdetails.attribute.value=iplanet-am-managed-personuserdetails.attribute.value=inetuseruserdetails.attribute.value=sunFederationManagerDataStoreuserdetails.attribute.value=sunFMSAML2NameIdentifieruserdetails.attribute.value=devicePrintProfilesContaineruserdetails.attribute.value=inetorgpersonuserdetails.attribute.value=sunIdentityServerLibertyPPServiceuserdetails.attribute.value=iPlanetPreferencesuserdetails.attribute.value=iplanet-am-user-serviceuserdetails.attribute.value=forgerock-am-dashboard-serviceuserdetails.attribute.value=organizationalpersonuserdetails.attribute.value=topuserdetails.attribute.value=sunAMAuthAccountLockoutuserdetails.attribute.value=personuserdetails.attribute.value=iplanet-am-auth-configuration-serviceuserdetails.attribute.name=userPassworduserdetails.attribute.value={SSHA}38z8YiyWj5WFQKAq+SDazE9S6o4ywqzH0KgQ9w==";

        Map<String, String[]> data = UserDetailsResponseHandler.parseUserDetails(userDetails);

        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            System.out.println("[" + entry.getKey() + "] : " + UserDetailsResponseHandler.debugArray(entry.getValue()));
        }
    }

}
