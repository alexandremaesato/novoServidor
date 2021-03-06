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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import model.Avaliacao;
import DAO.AvaliacaoDAO;
import model.Comentario;
import DAO.ComentarioDAO;
import model.Pessoa;
import DAO.PessoaDAO;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("comentario")
public class ComentarioResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ComentarioResource
     */
    public ComentarioResource() {
    }

    /**
     * Retrieves representation of an instance of Resources.ComentarioResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getComentarioDetalhes/{idEmpresa}")
    public String getComentarioDetalhes(@PathParam("idEmpresa") String idEmpresa) throws SQLException {
        List<Pessoa> pessoas = new ArrayList<>();
        List<Comentario>comentarios;
        Gson gson = new Gson();
        
        ComentarioDAO comentarioDAO = new ComentarioDAO();
        comentarios = comentarioDAO.getComentariosByIdEmpresa(Integer.parseInt(idEmpresa));
        
        PessoaDAO pessoaDao = new PessoaDAO();
//        for(int i=0; i<pessoas.size(); i++){
//            pessoas.add(pessoaDao.getPessoaById(comentarios.get(i).getPessoaid()));
//        }
        Map<String,String> result = new HashMap<String,String>();
        result.put("comentarios",gson.toJson(comentarios));
        //result.put("pessoas",gson.toJson(pessoas));
        
        return result.toString();
    }

    /**
     * PUT method for updating or creating an instance of ComentarioResource
     * @param content representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/setComentario")
    public String setComentario(String content) throws UnsupportedEncodingException, SQLException {
        content = URLDecoder.decode(content, "UTF-8");
        content = content.substring(0, content.length() - 1);
        String[] keyValuePairs = content.split("=", 2);   
        
        Gson gson = new Gson();
        Comentario comentario = gson.fromJson(keyValuePairs[1], Comentario.class);
        ComentarioDAO comentarioDAO = new ComentarioDAO();
        if( comentarioDAO.inserirComentario(comentario)){
            return "Comentario Inserido com sucesso";
        }
        return "Erro ao inserir o comentário";
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/getComentariosByIdEmpresa/{id}")
    public String getAvaliacoesByIdEmpresa(@PathParam("id") String id) {
        try {
            ComentarioDAO comentarioDAO = new ComentarioDAO();
            Gson gson = new Gson();
            Integer idi = Integer.parseInt(id);

            Map<String, String> result = new HashMap<String, String>();
            List<Comentario> lista = new ArrayList<Comentario>();
            lista = comentarioDAO.getComentariosByIdEmpresaSemLimite(idi);

            return gson.toJson(lista);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/setComentarioJson")
    public String setComentarioJson(String content) throws Exception { 
        
        Gson gson = new Gson();
        Comentario comentario = gson.fromJson(content, Comentario.class);
        ComentarioDAO comentarioDAO = new ComentarioDAO();
        if( comentarioDAO.inserirComentario(comentario)){
            return "Comentario Inserido com sucesso";
        }
        return "Erro ao inserir o comentário";
    }
    
    @GET
    @Produces("application/json")
    @Path("/getComentariosByIdEmpresaJson/{id}")
    public String getComentariosByIdEmpresaJson(@PathParam("id") String id) {
        Gson gson = new Gson();
        try {
            ComentarioDAO comentarioDAO = new ComentarioDAO();
            
            List<Comentario> lista = comentarioDAO.getComentariosByIdEmpresaSemLimite(new Integer(id));

            return gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson("Servidor indisponivel");
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/pegarComentariosDependentes/{id}")
    public String pegarComentariosDependentes(@PathParam("id") String idcomentario) throws Exception {
        
        try {
            List<Comentario> comentarios = new ComentarioDAO().pegarComentariosDependentes(new Integer(idcomentario));
            return new Gson().toJson(comentarios);
        } catch (Exception e) {
            e.printStackTrace();
            return "Servidor indisponível";
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/excluirComentario/{idempresa}/{idcomentario}")
    public String excluirComentario(@PathParam("idempresa") String idempresa, @PathParam("idcomentario") String idcomentario) throws Exception {
        
        try {
            ComentarioDAO cDao = new ComentarioDAO();
            cDao.excluirComentario(new Integer(idcomentario));
            List<Comentario> comentarios = new ComentarioDAO().getComentariosByIdEmpresaSemLimite(new Integer(idempresa));
            return new Gson().toJson(comentarios);
        } catch (Exception e) {
            e.printStackTrace();
            return "Servidor indisponível";
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/atualizarComentarios/{idempresa}")
    public String excluirComentario(@PathParam("idempresa") String idempresa) throws Exception {
        
        try {
            List<Comentario> comentarios = new ComentarioDAO().getComentariosByIdEmpresaSemLimite(new Integer(idempresa));
            return new Gson().toJson(comentarios);
        } catch (Exception e) {
            e.printStackTrace();
            return "Servidor indisponível";
        }
    }
}
