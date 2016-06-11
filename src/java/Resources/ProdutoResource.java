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
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import model.AutenticacaoDao;
import model.Avaliacao;
import model.AvaliacaoDAO;
import model.Entidade;
import model.Imagem;
import model.ImagemDAO;
import model.Produto;
import model.ProdutoDAO;

/**
 * REST Web Service
 *
 * @author Guilherme
 */
@Path("Produto")
public class ProdutoResource {

    @Context
    private UriInfo context;
    
    private final Gson gson = new Gson();
    private final AutenticacaoDao autdao = new AutenticacaoDao();
    private final ImagemDAO imgdao = new ImagemDAO();
    private final ProdutoDAO pDao = new ProdutoDAO();
    
    @Context
    private ServletContext servletcontext;
 
    public ProdutoResource() {
    }
    
    @POST
    @Path("/cadastrarProduto")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    public String cadastrarProduto(@HeaderParam("Authorization") List<String> autorizacao, String val) throws SQLException, IOException {
        
        // Pega informacoes do usuario no header e busca id do usuario
            String authToken = autorizacao.get(0);
            authToken = authToken.replaceFirst("Basic", "");

            byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);

            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
            String login = tokenizer.nextToken();
            String senha = tokenizer.nextToken();
            int pessoaid = autdao.getPessoaId(login, senha);

        val = URLDecoder.decode(val, "UTF-8");
        val = val.substring(0, val.length() - 1);
        String[] keyValuePairs = val.split("=", 2);
        Produto prod;
        prod = gson.fromJson(keyValuePairs[1], Produto.class);
        
        try {
            Entidade entidade = pDao.cadastrarProduto(prod, pessoaid);
            Imagem img = prod.getImagemPerfil();
            if (img.hasImagem()) {
                byte[] imagem = parseBase64Binary(img.getImg());
                String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
                String path = servletcontext.getRealPath("/").replace("\\", "/");
                if( path != null ){
                    new File(path + "uploads").mkdirs();
                    path = path + "uploads/";
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
                img.setPessoaid(pessoaid);
                img.setItemid(entidade.getIdentidade());
                
            }else{
                img.setNomeImagem("sem_imagem.jpg");
                img.setCaminho("/images/");
                img.setPessoaid(pessoaid);
                img.setItemid(entidade.getIdentidade_criada());
            }
            imgdao.inserirImagem(img, "produto", entidade.getIdentidade_criada());
            
        } catch (Exception e) {
            return gson.toJson("Erro ao cadastrar. Tente novamente mais tarde!");
        }
        
        return gson.toJson("Cadastrado com Sucesso!");
    }
    
    @GET
    @Path("/getProdutoDetalhes/{idProduto}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    public String getProdutoById(@PathParam("idProduto") String idProduto) throws SQLException{
        Produto produto = new Produto();
        ProdutoDAO produtoDao = new ProdutoDAO();
        Gson gson = new Gson();
        produto = produtoDao.getProdutoById(Integer.parseInt(idProduto));
        
        return gson.toJson(produto, Produto.class);
        
        
    }
}
