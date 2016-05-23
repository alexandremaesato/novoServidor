/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import model.Avaliacao;
import model.AvaliacaoDAO;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("avaliacao")
public class AvaliacaoResource {

    @Context
    private UriInfo context;

    public AvaliacaoResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/setAvaliacao")
    public String setAvaliacao(String content) throws UnsupportedEncodingException, SQLException {
        content = URLDecoder.decode(content, "UTF-8");
        content = content.substring(0, content.length() - 1);
        String[] keyValuePairs = content.split("=", 2);   
        
        Gson gson = new Gson();
        Avaliacao avaliacao = gson.fromJson(keyValuePairs[1], Avaliacao.class);
        AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
        avaliacaoDAO.updateAvaliacao(avaliacao);
        return gson.toJson(avaliacaoDAO.getAvaliacao(avaliacao));
        
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/getAvaliacao/{idFrom}/{idAvaliado}/{tipoAvaliacao}")
    public String getAvaliacao(@PathParam("idFrom") String idFrom, @PathParam("idAvaliado") String idAvaliado, @PathParam("tipoAvaliacao") String tipoAvaliacao) {
        try {
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
            Avaliacao avaliacao = new Avaliacao();
            Gson gson = new Gson();
            Integer id1 = Integer.parseInt(idFrom);
            Integer id2 = Integer.parseInt(idAvaliado);            
            return gson.toJson(avaliacaoDAO.getAvaliacao(id1, id2, tipoAvaliacao));
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/getAvaliacaoById/{id}")
    public String getAvaliacao(@PathParam("id") String id) {
        try {
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
            Avaliacao avaliacao = new Avaliacao();
            Gson gson = new Gson();
            Integer idi = Integer.parseInt(id);
            return gson.toJson(avaliacaoDAO.getAvaliacaoById(idi));
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
