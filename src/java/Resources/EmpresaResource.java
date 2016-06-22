/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.xml.wss.impl.misc.Base64;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import DAO.AutenticacaoDao;
import model.Empresa;
import DAO.EmpresaDAO;
import model.Imagem;
import DAO.ImagemDAO;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import model.Favorito;
import DAO.FavoritoDAO;
import model.Filtro;
import DAO.FiltroDAO;

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
    @Path("/getEmpresa/{idEmpresa}/{idPessoa}")
    @Produces("application/json")
    public String getEmpresa(@PathParam("idEmpresa") String idEmpresa, @PathParam("idPessoa") String idPessoa) {
        try {
            Map<String,String> result = new HashMap<>();
            List<Favorito> favoritos = new ArrayList<>();
            FavoritoDAO favoritoDao = new FavoritoDAO();
            favoritos = favoritoDao.getAllFavoritosByIdPessoa(Integer.parseInt(idPessoa));
            Empresa empresa = empresadao.pegarEmpresaPorId(Integer.parseInt(idEmpresa));
            empresa.mountImages(servletcontext.getRealPath("/WEB-INF/uploads/"));
            //String emps = gson.toJson(empresa);
            result.put("empresa", gson.toJson(empresa));
            result.put("favoritos", gson.toJson(favoritos));                    
            return result.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
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
    public String buscarEmpresas(String value) throws SQLException, UnsupportedEncodingException {
        value = URLDecoder.decode(value, "UTF-8");
        value = value.substring(0, value.length() - 1);
        String[] keyValuePairs = value.split("=", 2);

        JsonObject json = new JsonObject();
        /*
            
        value = value.substring(1, value.length() - 1);
        String[] keyValuePairs = value.split(",", 2);   
         */
        Gson gson = new Gson();
        Filtro filtro = gson.fromJson(keyValuePairs[1], Filtro.class);

        FiltroDAO filtroDao = new FiltroDAO();

        List<Empresa> empresasFiltradas = filtroDao.filtraEmpresa(filtro);

        List<Empresa> empresas = empresadao.pegarEmpresas();
        //Pegar ultima empresa apresentada
        //Pegar qual ordenacao
        //Pegar Parametros da filtragem (Culinaria, Endereco, preco max e min)
        String teste = "testando";
        json.add("Empresas", gson.toJsonTree(empresasFiltradas));
        json.add("Favoritos", gson.toJsonTree(teste));

        //String emps = gson.toJsonTree(empresas);
        //emps = emps+gson.toJson(teste);
        return json.toString();
    }

    @POST
    @Path("/buscarEmpresasJson")
    @Consumes("application/json")
    @Produces("application/json")
    public String buscarEmpresasJson(String json) throws SQLException {

        Filtro filtro = gson.fromJson(json, Filtro.class);

        FiltroDAO filtroDao = new FiltroDAO();
        List<Empresa> empresasFiltradas = filtroDao.filtraEmpresa(filtro);

        return gson.toJson(empresasFiltradas);
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

        val = URLDecoder.decode(val, "UTF-8");
        val = val.substring(0, val.length() - 1);
        String[] keyValuePairs = val.split("=", 2);
        Empresa emp;
        emp = gson.fromJson(keyValuePairs[1], Empresa.class);

        Imagem img = emp.getImagemPerfil();
        if (img.hasImagem()) {
            byte[] imagem = parseBase64Binary(img.getImg());
            String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
            String path = servletcontext.getRealPath("/");
            if (path != null) {
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

            int idEntidade = empresadao.cadastrarEmpresa(emp, pessoaid);
            img.setNomeImagem(img_name);
            img.setCaminho("/uploads/");
            img.setPessoaid(pessoaid);
            img.setItemid(idEntidade);
            imgdao.inserirImagem(img, "empresa", idEntidade);
        }

        return gson.toJson("Cadastrado com Sucesso!");
    }

    @GET
    @Path("/carregarEmpresas/{nota}")
    @Produces("application/json")
    public String carregarEmpresas(@PathParam("nota") String nota) throws SQLException, UnsupportedEncodingException, IOException {

        int notaAvaliacao = Integer.parseInt(nota);
        List<Empresa> empresas = empresadao.carregarEmpresas(notaAvaliacao);

        return gson.toJson(empresas);
    }

    @POST
    @Path("/setSouDono/{identidade_criada}/{idreponsavel}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    public String setSouDono(@PathParam("identidade_criada") String id1,
            @PathParam("idreponsavel") String id2) throws UnsupportedEncodingException, SQLException {

        int identidade_criada = Integer.parseInt(id1);
        int idreponsavel = Integer.parseInt(id2);

        EmpresaDAO empresaDao = new EmpresaDAO();
        empresaDao.setSouDono(idreponsavel, identidade_criada);

        return "";
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encode(imageByteArray);
    }

    public static byte[] decodeImage(String imageDataString) throws Base64DecodingException, Base64DecodingException {
        return Base64.decode(imageDataString);
    }

    public String getImageBase64(String nome) {
        File file = null;
        try {
            String path = servletcontext.getRealPath("/WEB-INF/uploads/");
            if (path != null) {
                String directoryPath = servletcontext.getRealPath("/WEB-INF/");
                path = directoryPath + "/uploads/";
                file = new File(path + nome);

                // Reading a Image file from file system
                FileInputStream imageInFile = new FileInputStream(file);
                byte imageData[] = new byte[(int) file.length()];
                imageInFile.read(imageData);
                // Converting Image byte array into Base64 String
                String imageDataString = encodeImage(imageData);
                // Converting a Base64 String into Image byte array
                byte[] imageByteArray = decodeImage(imageDataString);
                // Write a image byte array into file system
                //FileOutputStream imageOutFile = new FileOutputStream(path+"/teste_1.jpg");
                //imageOutFile.write(imageByteArray);
                imageInFile.close();
                //imageOutFile.close();

                return imageDataString;
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    @POST
    @Path("/cadastrarEmpresaWeb")
    @Consumes("application/json")
    @Produces("application/json")
    public String cadastrarEmpresaWeb(@HeaderParam("Authorization") List<String> autorizacao, String json) throws SQLException, IOException, Base64DecodingException {

        // Pega informacoes do usuario no header e busca id do usuario
        String authToken = autorizacao.get(0);
        authToken = authToken.replaceFirst("Basic", "");

        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary(authToken);
        String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8);

        StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
        String login = tokenizer.nextToken();
        String senha = tokenizer.nextToken();
        int pessoaid = autdao.getPessoaId(login, senha);

        Empresa emp = gson.fromJson(json, Empresa.class);

        Imagem img = emp.getImagemPerfil();
        if (img.hasImagem()) {
            byte[] imagem = parseBase64Binary(img.getImg());
            String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
            String path = servletcontext.getRealPath("/");
            if (path != null) {
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

            int idEmpresa = empresadao.cadastrarEmpresa(emp, pessoaid);
            img.setNomeImagem(img_name);
            img.setCaminho("/uploads/");
            img.setPessoaid(pessoaid);
            img.setItemid(idEmpresa);
            imgdao.inserirImagem(img, "empresa", idEmpresa);
        }

        return gson.toJson("Empresa cadastrada com sucesso!");

    }
    
    @POST
    @Path("/atualizarEmpresaWeb")
    @Consumes("application/json")
    @Produces("application/json")
    public String atualizarEmpresaWeb(String json) throws SQLException, IOException, Base64DecodingException {

        Empresa emp = gson.fromJson(json, Empresa.class);

        Imagem img = emp.getImagemPerfil();
        if (img.hasImagem()) {
            byte[] imagem = parseBase64Binary(img.getImg());
            String img_name = "imgPerfil-" + System.currentTimeMillis() + ".jpg";
            String path = servletcontext.getRealPath("/");
            if (path != null) {
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
                
                img.setNomeImagem(img_name);
                imgdao.atualizarImagemWeb(img);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            empresadao.atualizarEmpresaWeb(emp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Empresa empresa = empresadao.pegarEmpresaPorId(emp.getEmpresaId());
        
        return gson.toJson(empresa);

    }

    @GET
    @Path("/buscarMinhasEmpresas/{id}")
    @Produces("application/json")
    public String buscarMinhasEmpresas(@PathParam("id") String id) throws Exception {

        List<Empresa> emps = new EmpresaDAO().buscarMinhasEmpresas(new Integer(id));

        return gson.toJson(emps);
    }
    
    @GET
    @Path("/pegarEmpresaPorId/{idEmpresa}")
    @Produces("application/json")
    public String pegarEmpresaPorId(@PathParam("idEmpresa") String idEmpresa) {
        try {
            Empresa empresa = empresadao.pegarEmpresaPorId(new Integer(idEmpresa));
            return gson.toJson(empresa);
        } catch (Exception e) {
            e.printStackTrace();
            return "Servidor indisponível";
        }
    }
    
    @POST
    @Path("/excluirEmpresaWeb")
    @Consumes("application/json")
    public void excluirEmpresaWeb(String json) {
        try {
            Integer idEmpresa = gson.fromJson(json, Integer.class);
            empresadao.excluirEmpresa(idEmpresa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
