/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Guilherme
 */
@Path("Services")
public class ServicesResource {

    @Context
    private UriInfo context;
    private Gson gson = new Gson();
    private EmpresaDAO empresadao = new EmpresaDAO();

    /**
     * Creates a new instance of ServicesResource
     */
    public ServicesResource() {
    }

    @GET
    @Path("/pegarEmpresas")
    @Produces(MediaType.APPLICATION_JSON)
    public String pegarEmpresas() throws SQLException{
        
        EmpresaDAO empresadao = new EmpresaDAO();
        List<Empresa> empresas = empresadao.pegarEmpresas();
        
        String emps = gson.toJson(empresas);
        
        return emps;
    }

    @POST
    @Path("/cadastrarEmpresa")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String cadastrarEmpresa(@PathParam("empresa") String empresa, @PathParam("pessoa") String pessoa) throws SQLException {
        
        Empresa objetoEmpresa = gson.fromJson(empresa, Empresa.class);
        Pessoa objetoPessoa   = gson.fromJson(pessoa, Pessoa.class);
        empresadao.cadastrarEmpresa(objetoEmpresa, objetoPessoa);
        
        return "OK";
    }
    
}
