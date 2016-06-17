/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class FavoritoDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;
    
    public void setFavorito(Favorito favorito) throws SQLException{
        String insereComentario = "INSERT INTO favoritos( idpessoa, idfavoritado, tipofavoritado ) VALUES( ?,?,?)";
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(insereComentario, Statement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1, favorito.getIdPessoa());
            ptmt.setInt(2, favorito.getIdFavoritado());
            ptmt.setString(3, favorito.getTipoFavoritado());

            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idComentario = resultSet.getInt(1);
            
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            
            throw new RuntimeException("Erro ao favoritar no banco de dados. " + ex);

        } finally {
            ptmt.close();    
        }
    }

    public void deleteFavorito(Favorito favorito) throws SQLException {
        String deleteComentario = "DELETE FROM favoritos WHERE idpessoa = ? and idfavoritado = ? and tipofavoritado = ?";
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(deleteComentario);
            ptmt.setInt(1, favorito.getIdPessoa());
            ptmt.setInt(2, favorito.getIdFavoritado());
            ptmt.setString(3, favorito.getTipoFavoritado());

            ptmt.executeUpdate();
    
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            
            throw new RuntimeException("Erro ao favoritar no banco de dados. " + ex);

        } finally {
            ptmt.close();    
        }
    }
       
}
