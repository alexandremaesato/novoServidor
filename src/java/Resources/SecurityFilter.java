/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;


/**
 *
 * @author Alexandre
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter{

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic";
    private static final String SECURED_URL_PREFIX = "secured";
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
        if(authHeader != null && authHeader.size() > 0){
            try{
            String authToken = authHeader.get(0);
            authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
            
             byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;
    
            //String decodeString = Base64.getDecoder().decode(authToken).toString();
            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            
            if ("user".equals(username) && "password".equals(password)){
                return;
            }
            }catch (Exception e){
                Response unauthorizedStatus = Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .header("Content-Type", "text/xml; charset=utf-8")
                                        .entity("Erro de conexão.")
                                        .build();
                requestContext.abortWith(unauthorizedStatus);
            }
            
        }
        Response unauthorizedStatus = Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .header("Content-Type", "text/xml; charset=utf-8")
                                        .entity("Erro de Usuário ou Senha.")
                                        .build();
        requestContext.abortWith(unauthorizedStatus);
    }
    
    
   
    
    
}
