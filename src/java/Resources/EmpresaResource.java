/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.HashMap;
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
import model.Empresa;
import model.EmpresaDAO;
import model.Pessoa;

/**
 * REST Web Service
 *
 * @author Guilherme
 */
@Path("Empresa")
@Consumes("application/json")
@Produces("application/json")
public class EmpresaResource {

    @Context
    private UriInfo context;
    private Gson gson = new Gson();
    private EmpresaDAO empresadao = new EmpresaDAO();

    /**
     * Creates a new instance of ServicesResource
     */
    public EmpresaResource() {
    }

    @GET
    @Path("/pegarEmpresas")
    public String pegarEmpresas() throws SQLException{
        
        EmpresaDAO empresadao = new EmpresaDAO();
        List<Empresa> empresas = empresadao.pegarEmpresas();
        
        String emps = gson.toJson(empresas);
        
        return emps;
    }

    @POST
    @Path("/cadastrarEmpresa")
    @Consumes("application/json")
    public String cadastrarEmpresa(String val) throws SQLException {
        
        Gson gson = new Gson();
        HashMap params = gson.fromJson(val, HashMap.class );
        String jsonEmpresa = (String) params.get("empresa").toString();
        String jsonPessoa = (String) params.get("empresa").toString();
       
        //Precisa criar verificacao de todos os dados para nao dar erro no DAO
        Empresa objetoEmpresa = gson.fromJson(jsonEmpresa, Empresa.class);
        Pessoa objetoPessoa   = gson.fromJson(jsonPessoa, Pessoa.class);
        empresadao.cadastrarEmpresa(objetoEmpresa, objetoPessoa);
        
        return "OK";
    }
    
}
