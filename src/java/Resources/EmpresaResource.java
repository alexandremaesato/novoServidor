/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    public String cadastrarEmpresa(String val) throws SQLException {
        Gson gson = new Gson(); 
        Empresa emp = new Empresa();
        String coded = null;
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(String keyValue : val.split(" *, *")) {
            String[] pairs = keyValue.split(" *= *", 2);
            coded = pairs[1];
            map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
        }
        
        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(coded);
        String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);
        emp = gson.fromJson(decodeString, Empresa.class);
       
        Imagem img = emp.getImagemPerfil();
        byte[] imagem = img.getImg().getBytes(Charset.forName("UTF-8"));
        String img_name = "img-" + System.currentTimeMillis() + img.getNomeImagem();
        try{
            FileOutputStream fos = new FileOutputStream("imagensPerfil/" + img_name );
            fos.write(imagem);
            FileDescriptor fd = fos.getFD();
            fos.flush();
            fd.sync();
            fos.close(); 
        }
        catch(Exception e){
           throw new RuntimeException("Erro ao gravar imagem. " + e);
        }
        
        img.setNomeImagem(img_name);
        img.setCaminho("imagensPerfil/" + img_name);
//        img.setPessoaid(autdao.getPessoaId(objetoPessoa));
//        img.setItemid(idEntidade);
        imgdao.inserirIMagem(img);
        
        return gson.toJson(emp);
    }
    
}
