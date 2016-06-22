/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import DAO.AutenticacaoDao;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("autenticacao")
public class AutenticacaoResource {

    @Context
    private UriInfo context;
   
    private final AutenticacaoDao autdao = new AutenticacaoDao();
    int idPessoa;
    String login = null;    
    String senha = null;
    

    /**
     * Creates a new instance of AutenticacaoResource
     */
    public AutenticacaoResource() {
       
    }

    @POST
    @Path("/logar")
    @Produces(MediaType.APPLICATION_JSON)
    public String logar(@HeaderParam("Authorization") List<String> autorizacao) throws SQLException {
        carregaLoginSenha(autorizacao);
        Integer id = autdao.getPessoaId(login, senha);
        return id.toString();
    }
    
    @POST
    @Path("/cadastrar")
    @Produces(MediaType.APPLICATION_JSON)
    public String cadastrar(@HeaderParam("Register") List<String> autorizacao) throws SQLException {
        carregaLoginSenha(autorizacao);
        Integer idPessoa = null;
        idPessoa = AutenticacaoDao.criarAutenticacao(login, senha);
        return idPessoa.toString();
    }
    
    private void carregaLoginSenha(List<String> autorizacao){
        String authToken = autorizacao.get(0);
        authToken = authToken.replaceFirst("Basic", "");
        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
        String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);
        StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
        login = tokenizer.nextToken();
        senha = tokenizer.nextToken();
        
    }
    
    
    
}
