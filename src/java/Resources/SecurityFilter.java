/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import control.HttpsClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import model.AutenticacaoDao;
import model.TokenInfo;
import utilitarios.Criptografia;

/**
 *
 * @author Alexandre
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String REGISTER_HEADER_KEY = "Register";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic";
    private static final String SECURED_URL_PREFIX = "secured";
    private static final String TOKEN_URL_PREFIX = "Token";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        List<String> registerHeader = requestContext.getHeaders().get(REGISTER_HEADER_KEY);
        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
        List<String> tokenList = requestContext.getHeaders().get(TOKEN_URL_PREFIX);
        TokenInfo tokenInfo = null;
        String token = "";
        //GoogleIdToken as;
        //Verificar o token
//        try {
//            if (tokenList != null && tokenList.size() > 0) {
//                token = tokenList.get(0);
//                token = token.replaceFirst(TOKEN_URL_PREFIX, "");
//                Gson gson = new Gson();
//                String json = HttpsClient.readJsonFromUrl(token);
//                tokenInfo = gson.fromJson(json, TokenInfo.class);
//            }
//
//            if (verificaEmail(tokenInfo)) {
//                if(verificaToken(tokenInfo, token)){
//                    AutenticacaoDao.gravarToken(tokenInfo, token);
//                    return;
//                }else{
//                    Response unauthorizedStatus = Response
//                    .status(Response.Status.UNAUTHORIZED)
//                    .header("Content-Type", "text/xml; charset=utf-8")
//                    .entity("Token ja foi utilizado.")
//                    .build();
//            requestContext.abortWith(unauthorizedStatus);
//            return;
//                    
//                }
//            } else {
//                AutenticacaoDao.criarAutenticacao(tokenInfo.getEmail(), "");
//                return;
//            }
//        } catch (Exception e) {
//            Response unauthorizedStatus = Response
//                    .status(Response.Status.UNAUTHORIZED)
//                    .header("Content-Type", "text/xml; charset=utf-8")
//                    .entity("Erro de conexão.")
//                    .build();
//            requestContext.abortWith(unauthorizedStatus);
//            return;
//        }
//    }

    //Verificar o token com a Base de dados
    
        if(registerHeader != null && registerHeader.size() > 0){
            try{
            String authToken = registerHeader.get(0);
            authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
            
            byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;            
            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            
            
                        
            if(!AutenticacaoDao.verificaEmail(username)){
                //AutenticacaoDao.criarAutenticacao(username, password);
                return;
            }else{
                Response unauthorizedStatus = Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .header("Content-Type", "text/xml; charset=utf-8")
                                        .entity("Login já está sendo utilizado.")
                                        .build();
                requestContext.abortWith(unauthorizedStatus);
                return;
               
            }
            }catch(Exception e){
                Response unauthorizedStatus = Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .header("Content-Type", "text/xml; charset=utf-8")
                                        .entity("Erro de conexão.")
                                        .build();
                requestContext.abortWith(unauthorizedStatus);
              
            }
        }
        if(authHeader != null && authHeader.size() > 0){
            String path = requestContext.getUriInfo().getPath();
            try{
            String authToken = authHeader.get(0);
            authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
            
             byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;
    
            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            
            
            if(AutenticacaoDao.autenticar(username, password) && !"autenticacao/cadastrar".equals(path)){
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
     
    public void cadastrarAutenticacao(String login, String senha) {
        //DatatypeConverter.
        try {
            AutenticacaoDao.criarAutenticacao(login, senha);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verificaToken(TokenInfo tokenInfo, String token) {
        try {
            if (AutenticacaoDao.verificaToken(tokenInfo, token)) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
        public boolean verificaEmail(TokenInfo tokenInfo) {
        try {
            if (AutenticacaoDao.verificaEmail(tokenInfo)) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
