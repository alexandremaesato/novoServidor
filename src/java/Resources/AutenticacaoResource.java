/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("autenticacao")
public class AutenticacaoResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AutenticacaoResource
     */
    public AutenticacaoResource() {
    }

    @POST
    @Path("/logar")
      
    public String logar(String val) {
        return "";
    }
    
}
