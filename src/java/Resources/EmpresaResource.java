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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.WebServiceContext;
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
    public String cadastrarEmpresa(@HeaderParam("Authorization") List<String> autorizacao, String jsonEmpresa) throws SQLException, IOException {
        
        //TESTE criando arquivo txt para verificar conteudo vindo no jsonEmpresa
//            File arquivo = new File("C:\\imagens\\teste.txt");
//            try( FileWriter fw = new FileWriter(arquivo) ){
//                fw.write(jsonEmpresa);
//                fw.flush();
//            }catch(IOException ex){
//              ex.printStackTrace();
//            }
        //FIM - TESTE
        
        // Pega informacoes do usuario no header e busca id do usuario
//            String authToken = autorizacao.get(0);
//            authToken = authToken.replaceFirst("Basic", "");
//
//            byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
//            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;
//
//            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
//            String login = tokenizer.nextToken();
//            String senha = tokenizer.nextToken();
//            int pessoaid = autdao.getPessoaId(login, senha);
//       
        //Precisa criar verificacao de todos os dados para nao dar erro no DAO
        Empresa objetoEmpresa = gson.fromJson(jsonEmpresa, Empresa.class);
//        int idEntidade = empresadao.cadastrarEmpresa(objetoEmpresa, pessoaid);
//        
//        Imagem img = objetoEmpresa.getImagemPerfil();
//        byte[] imagem = img.getImg().getBytes();
//        String img_name = "img-" + System.currentTimeMillis() + ".jpg";
//        try{
//                try (FileOutputStream fos = new FileOutputStream("C:/imagens/" + img_name )) {
//                    fos.write(imagem);
//                    FileDescriptor fd = fos.getFD();
//                    fos.flush();
//                    fd.sync();
//                } 
//        }
//        catch(Exception e){
//           throw new RuntimeException("Erro ao gravar imagem. " + e);
//        }
//        
//        img.setNomeImagem(img_name);
//        img.setCaminho("imagensPerfil/" + img_name);
//        img.setPessoaid(pessoaid);
//        img.setItemid(idEntidade);
//        imgdao.inserirIMagem(img);
        
        return gson.toJson(objetoEmpresa);
    }
    
}