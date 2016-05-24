/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import model.AutenticacaoDao;
import model.Empresa;
import model.EmpresaDAO;
import model.Imagem;
import model.ImagemDAO;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

/**
 * REST Web Service
 *
 * @author Guilherme
 */
@Path("Empresa")
public class EmpresaResource {

    @Context
    private UriInfo context;
    @Context
    private ServletContext servletcontext;
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
    public String pegarEmpresas() throws SQLException {

        List<Empresa> empresas = empresadao.pegarEmpresas();

        String emps = gson.toJson(empresas);

        return emps;
    }

    @POST
    @Path("/buscarEmpresas")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String buscarEmpresas(String value) throws SQLException {

        JsonObject json = new JsonObject();

        
        value = value.substring(1, value.length() - 1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
        Map<String, String> map = new HashMap<>();

        for (String pair : keyValuePairs) //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value 
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        String a = map.get("teste");

        
        Gson gson = new Gson();

        List<Empresa> empresas = empresadao.pegarEmpresas();
        //Pegar ultima empresa apresentada
        //Pegar qual ordenacao
        //Pegar Parametros da filtragem (Culinaria, Endereco, preco max e min)
        String teste = "testando";
        json.add("Empresas", gson.toJsonTree(empresas));
        json.add("Teste", gson.toJsonTree(teste));
        //String emps = gson.toJsonTree(empresas);
        //emps = emps+gson.toJson(teste);

        return json.toString();
    }

    @POST
    @Path("/cadastrarEmpresa")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    public String cadastrarEmpresa(@HeaderParam("Authorization") List<String> autorizacao, String val) throws SQLException, IOException {
        
        // Pega informacoes do usuario no header e busca id do usuario
        String authToken = autorizacao.get(0);
        authToken = authToken.replaceFirst("Basic", "");

        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
        String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);

        StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
        String login = tokenizer.nextToken();
        String senha = tokenizer.nextToken();
        int pessoaid = autdao.getPessoaId(login, senha);

        // Decode do hashmap para json e instanciacao/cadastramento de empresa
        Empresa emp;
        String coded = null;
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String keyValue : val.split(" *, *")) {
            String[] pairs = keyValue.split(" *= *", 2);
            coded = pairs[1];
            map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
        }

        encodedHelloBytes = DatatypeConverter.parseBase64Binary(coded);
        decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);
        emp = gson.fromJson(decodeString, Empresa.class);
        int idEntidade = empresadao.cadastrarEmpresa(emp, pessoaid);
        
        Imagem img = emp.getImagemPerfil();
        if (img.hasImagem()) {
            byte[] imagem = parseBase64Binary(img.getImg());
            String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
            String path = servletcontext.getRealPath("/");
            if( path != null ){
                int pos = path.indexOf("build");
                path = path.substring(0, pos);
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
            img.setCaminho("uploads/" + img_name);
            img.setPessoaid(pessoaid);
            img.setItemid(idEntidade);
            imgdao.inserirImagem(img);
        }
        
        return gson.toJson("Cadastrado com Sucesso!");
    }
    
    @GET
    @Path("/carregarEmpresas/{nota}")
    @Produces("application/json")
    public String carregarEmpresas(@PathParam("nota") String nota) throws SQLException, UnsupportedEncodingException, IOException{
        
        int notaAvaliacao = Integer.parseInt(nota);
        List<Empresa> empresas = empresadao.carregarEmpresas(notaAvaliacao);
        
        return gson.toJson(empresas);
    }

}
