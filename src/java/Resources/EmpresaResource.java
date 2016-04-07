/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import model.AutenticacaoDao;
import model.Empresa;
import model.EmpresaDAO;
import model.Imagem;
import model.ImagemDAO;
import model.Pessoa;

/**
 * REST Web Service
 *
 * @author Guilherme
 */
@Path("Empresa")
public class EmpresaResource {

    @Context
    private UriInfo context;
    private final Gson gson = new Gson();
    private final EmpresaDAO empresadao = new EmpresaDAO();
    private final AutenticacaoDao autdao = new AutenticacaoDao();
    private final ImagemDAO imgdao = new ImagemDAO();

    /**
     * Creates a new instance of ServicesResource
     */
    public EmpresaResource() {
    }

    @GET
    @Path("/pegarEmpresas")
    @Produces("application/json")
    public String pegarEmpresas() throws SQLException{
        
        List<Empresa> empresas = empresadao.pegarEmpresas();
        
        String emps = gson.toJson(empresas);
        
        return emps;
    }

    @POST
    @Path("/cadastrarEmpresa")
    @Consumes("application/json")
    @Produces("application/json")
    public String cadastrarEmpresa(String val) throws SQLException {
        
//        HashMap params = gson.fromJson(val, HashMap.class );
//        String jsonEmpresa = (String) params.get("empresa").toString();
//        String jsonPessoa = (String) params.get("empresa").toString();
//       
//        //Precisa criar verificacao de todos os dados para nao dar erro no DAO
//        Empresa objetoEmpresa = gson.fromJson(jsonEmpresa, Empresa.class);
//        Pessoa objetoPessoa   = gson.fromJson(jsonPessoa, Pessoa.class);
//        int idEntidade = empresadao.cadastrarEmpresa(objetoEmpresa, objetoPessoa);
//        
//        Imagem img = objetoEmpresa.getImagemPerfil();
//        byte[] imagem = img.getImg().getBytes(Charset.forName("UTF-8"));
//        String img_name = "img-" + System.currentTimeMillis() + img.getNomeImagem();
//        try{
//            FileOutputStream fos = new FileOutputStream("imagensPerfil/" + img_name );
//            fos.write(imagem);
//            FileDescriptor fd = fos.getFD();
//            fos.flush();
//            fd.sync();
//            fos.close(); 
//        }
//        catch(Exception e){
//           throw new RuntimeException("Erro ao gravar imagem. " + e);
//        }
//        
//        img.setNomeImagem(img_name);
//        img.setCaminho("imagensPerfil/" + img_name);
//        img.setPessoaid(autdao.getPessoaId(objetoPessoa));
//        img.setItemid(idEntidade);
//        imgdao.inserirIMagem(img);
        
        return gson.toJson("Cadastrado com Sucesso!");
    }
    
}