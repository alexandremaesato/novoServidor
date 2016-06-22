/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import model.Imagem;
import model.ImagemDAO;
import model.Pessoa;
import model.PessoaDAO;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("pessoa")
public class PessoaResource {

    @Context
    private UriInfo context;
    @Context
    private ServletContext servletcontext;
    private final ImagemDAO imgdao = new ImagemDAO();

    /**
     * Creates a new instance of PessoaResource
     */
    public PessoaResource() {
    }

    /**
     * Retrieves representation of an instance of Resources.PessoaResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/getPessoa/{idPessoa}")
    public String getPessoa(@PathParam("idPessoa") String idPessoa) throws SQLException {
        PessoaDAO pessoaDao = new PessoaDAO();        
        Pessoa pessoa = pessoaDao.getPessoaById(Integer.parseInt(idPessoa));
        Gson gson = new Gson();
        if( pessoa != null){
           return gson.toJson(pessoa);
        }else{
           return "Erro ao recuperar o Perfil";
        }
        
    }

    /**
     * PUT method for updating or creating an instance of PessoaResource
     * @param content representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    @Path("/editarPessoa")
    public String editarPessoa(String value) throws UnsupportedEncodingException, SQLException {
        value = URLDecoder.decode(value, "UTF-8");
        value = value.substring(0, value.length() - 1);
        String[] keyValuePairs = value.split("=", 2);
        Gson gson = new Gson();
        PessoaDAO pessoaDao = new PessoaDAO();
        Pessoa pessoa = new Pessoa();
        Imagem img;
        
        pessoa = gson.fromJson(keyValuePairs[1], Pessoa.class);
        pessoaDao.updatePessoa(pessoa);
        img = pessoa.getImagemPerfil();
        if (img.hasImagem()) {
            byte[] imagem = parseBase64Binary(img.getImg());
            String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
            String path = servletcontext.getRealPath("/");
            if( path != null ){
                int pos = path.indexOf("build");
                path = path.substring(0, pos);
                new File(path + "web/uploads").mkdirs();
                path = path + "web/uploads/";
            }
            
            try (FileOutputStream fos = new FileOutputStream(path + img_name)) {
                    fos.write(imagem);
                    FileDescriptor fd = fos.getFD();
                    fos.flush();
                    fd.sync();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao gravar imagem. " + e);
            }
            
            img.setNomeImagem(img_name);
            img.setCaminho("/uploads/");
            img.setPessoaid(pessoa.getPessoaid());
            img.setItemid(pessoa.getPessoaid());
            imgdao.inserirImagem(img, "pessoa", pessoa.getPessoaid());
        }
        return gson.toJson("Salvo!");
    }
    
    @GET
    @Path("/buscarPerfilUsuario/{id}")
    @Produces("application/json")
    public String buscarPerfilUsuario(@PathParam("id") String id) {
        
        try {
            Pessoa pessoa = new PessoaDAO().pegarPessoaPorId(new Integer(id));
            return new Gson().toJson(pessoa);
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson("Erro no servidor: " + e.getMessage());
        }
    }
    
    @POST
    @Path("/atualizarPessoa")
    @Consumes("application/json")
    @Produces("application/json")
    public void atualizarPessoa(String json) {
        
        try {
            Pessoa p = new Gson().fromJson(json, Pessoa.class);
            new PessoaDAO().atualizarPessoa(p);
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @POST
    @Path("/atualizarSenha")
    @Consumes("application/json")
    @Produces("application/json")
    public String atualizarSenha(String json) {
        
        try {
            Pessoa p = new Gson().fromJson(json, Pessoa.class);
            new PessoaDAO().atualizarSenha(p);
            
            Pessoa pessoa = new PessoaDAO().pegarPessoaPorId(p.getPessoaid());
            return new Gson().toJson(pessoa);
                        
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson("Erro no servidor: " + e.getMessage());
        }
    }
}
