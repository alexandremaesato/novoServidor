/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.HashMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import model.EnderecoDao;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("Endereco")
public class EnderecoResource {

    @Context
    private UriInfo context;
    private Gson gson = new Gson();

    /**
     * Creates a new instance of EnderecoResource
     */
    public EnderecoResource() {
    }

    /**
     * Retrieves representation of an instance of Resources.EnderecoResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of EnderecoResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @GET
    @Path("/getCidades/{estado}")
    @Consumes("application/string")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCidades(@PathParam("estado") String estado) throws SQLException {
        EnderecoDao enderecoDao = new EnderecoDao();
        
        HashMap<String,String> result = new HashMap<String,String>();
        String cidades = gson.toJson(enderecoDao.getCidades(estado));
        result.put("result", estado);
        return cidades;
    }
    
    @GET
    @Path("/getBairros/{cidade}")
    @Consumes("application/string")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBairros(@PathParam("cidade") String cidade) throws SQLException {
        EnderecoDao enderecoDao = new EnderecoDao();
        String bairros = gson.toJson(enderecoDao.getBairros(cidade));
        System.out.println("============================================"+bairros);
        return bairros;
    }
}
