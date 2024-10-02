package lambdaapi;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import lambdaapi.dto.ClientRequestDto;
import lambdaapi.service.AwsService;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
        	
        	String output = "";
            ClientRequestDto clientRequestDto = new Gson().fromJson(input.getBody(), ClientRequestDto.class);

            AwsService awsService = new AwsService();
            AWSCognitoIdentityProvider cognitoClient = awsService.createCognitoClient();

            if(clientRequestDto.isCriado()) {
            	System.out.println("Iniciando o login");
                //Map<String, String> resultadoLogin = awsService.login("marcelo30128@gmail.com", "0p9o8i7U", cognitoClient);
            	Map<String, String> resultadoLogin = awsService.logarUsuario(clientRequestDto.getCpf(), cognitoClient);
            	output =  String.format("{ \"Token \": \"%s\" }", resultadoLogin.toString()); 
            	System.out.println("Login finalizado");
            }
            else {
            	System.out.println("Iniciando o registro...");
            	SignUpResult usuarioRegistrado = awsService.registrarUsuario(clientRequestDto.getNome(), clientRequestDto.getEmail(), clientRequestDto.getCpf(), cognitoClient);
            	System.out.println("UserSub de registro de usuario: " + usuarioRegistrado.getUserSub());
            	System.out.println("Finalizando o registro...");
            	output = String.format("{ \"UserSub \": \"%s\" }", usuarioRegistrado.getUserSub());
            }
            
//            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
//            String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

//    @SuppressWarnings("unused")
//	private String getPageContents(String address) throws IOException{
//        @SuppressWarnings("deprecation")
//		URL url = new URL(address);
//        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
//            return br.lines().collect(Collectors.joining(System.lineSeparator()));
//        }
//    }
}
