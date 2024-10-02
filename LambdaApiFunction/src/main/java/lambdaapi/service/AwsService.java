package lambdaapi.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;

public class AwsService {

    public AWSCognitoIdentityProvider createCognitoClient() {
        String access_key = System.getenv("AWS_ACCESS_KEY");
        String secret_key = System.getenv("AWS_SECRET_KEY");
        String region = "us-east-1";

        AWSCredentials cred = new BasicAWSCredentials(access_key, secret_key);
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credProvider)
                .withRegion(region)
                .build();
    }

//    public SignUpResult signUp(String name, String email, String password, AWSCognitoIdentityProvider cognitoClient) {
//        String clientId = "685uq4nqlu8bn4pf6929p2r0pr";
//		SignUpRequest request = new SignUpRequest().withClientId(clientId)
//                .withUsername(email)
//                .withPassword(password)
//                .withUserAttributes(
//                        new AttributeType()
//                                .withName("name")
//                                .withValue(name));
//		SignUpResult result = cognitoClient.signUp(request);
//        return result;
//    }

    public SignUpResult registrarUsuario(String name, String email, String document, AWSCognitoIdentityProvider cognitoClient) {
        String clientId = "685uq4nqlu8bn4pf6929p2r0pr";
        
        SignUpRequest request = new SignUpRequest()
        		.withClientId(clientId)
        		.withUsername(document)
        		.withPassword(document)
                .withUserAttributes(
                        new AttributeType()
                                .withName("name")
                                .withValue(name))
                .withUserAttributes(
                        new AttributeType()
                                .withName("email")
                                .withValue(email))
                .withUserAttributes(
                        new AttributeType()
                                .withName("custom:cpf")
                                .withValue(document));
        
		SignUpResult result = cognitoClient.signUp(request);
		
        return result;
    }
    
    @SuppressWarnings("serial")
	public Map<String, String> logarUsuario(String document, AWSCognitoIdentityProvider cognitoClient) {

        String userPool = "us-east-1_TwAD4M1MB";
		String clientId = "685uq4nqlu8bn4pf6929p2r0pr";
		
		Map<String, String> authParams = new LinkedHashMap<String, String>() {{
            put("USERNAME", document);
            put("PASSWORD", document);
        }};
		
		AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPool)
                .withClientId(clientId)
                .withAuthParameters(authParams);
		
		AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();
        
        return new LinkedHashMap<String, String>() {{
            put("idToken", resultType.getIdToken());
            put("accessToken", resultType.getAccessToken());
            put("refreshToken", resultType.getRefreshToken());
            put("message", "Successfully login");
        }};

    }
    
//    @SuppressWarnings("serial")
//	public Map<String, String> login(String email, String password, AWSCognitoIdentityProvider cognitoClient) {
//		Map<String, String> authParams = new LinkedHashMap<String, String>() {{
//            put("USERNAME", email);
//            put("PASSWORD", password);
//        }};
//
//        String userPool = "us-east-1_TwAD4M1MB";
//		String clientId = "685uq4nqlu8bn4pf6929p2r0pr";
//		
//		AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
//                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//                .withUserPoolId(userPool)
//                .withClientId(clientId)
//                .withAuthParameters(authParams);
//		
//		AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);
//        AuthenticationResultType resultType = authResult.getAuthenticationResult();
//        
//        return new LinkedHashMap<String, String>() {{
//            put("idToken", resultType.getIdToken());
//            put("accessToken", resultType.getAccessToken());
//            put("refreshToken", resultType.getRefreshToken());
//            put("message", "Successfully login");
//        }};
//
//    }


}