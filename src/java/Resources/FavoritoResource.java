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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import model.Favorito;
import model.FavoritoDAO;

/**
 * REST Web Service
 *
 * @author Alexandre
 */
@Path("favorito")
public class FavoritoResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FavoritoResource
     */
    public FavoritoResource() {
    }

    /**
     * Retrieves representation of an instance of Resources.FavoritoResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of FavoritoResource
     *
     * @param content representation for the resource
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/setFavorito")
    public String setFavorito(String value) throws UnsupportedEncodingException, SQLException {
        value = URLDecoder.decode(value, "UTF-8");
        value = value.substring(0, value.length() - 1);
        String[] keyValuePairs = value.split("=", 2);
        Gson gson = new Gson();

        Favorito favorito = gson.fromJson(keyValuePairs[1], Favorito.class);
        FavoritoDAO favoritoDao = new FavoritoDAO();
        
        if(favoritoDao.hasFavorito(favorito)){
            favoritoDao.updateFavorito(favorito);            
        }else{
            favoritoDao.setFavorito(favorito);
        }
        return "Alterado com sucesso";
//        if (favorito.isCheck()) {
//            try {
//                if(!favorito.hasId()){
//                    favoritoDao.setFavorito(favorito);
//                }else{
//                    favoritoDao.updateFavorito(favorito);
//                }
//                return "Favoritado";
//            } catch (SQLException ex) {
//                Logger.getLogger(FavoritoResource.class.getName()).log(Level.SEVERE, null, ex);
//                return "Houve algum problema para favoritar";
//            }
//        }else{
//            try {
//                favoritoDao.updateFavorito(favorito);
//                return "Retirado o Favorito";
//            } catch (SQLException ex) {
//                Logger.getLogger(FavoritoResource.class.getName()).log(Level.SEVERE, null, ex);
//                return "Houve algum problema retirar o favorito";
//            }
//        }

    }
}
