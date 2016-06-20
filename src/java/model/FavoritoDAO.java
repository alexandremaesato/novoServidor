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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class FavoritoDAO {

    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;

    public void setFavorito(Favorito favorito) throws SQLException {
        String insereComentario = "INSERT INTO favoritos( idpessoa, idfavoritado, tipofavoritado, `check` ) VALUES( ?,?,?,?) ";
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(insereComentario, Statement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1, favorito.getIdPessoa());
            ptmt.setInt(2, favorito.getIdFavoritado());
            ptmt.setString(3, favorito.getTipoFavoritado());
            ptmt.setInt(4, 1);

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

    public void updateFavorito(Favorito favorito) throws SQLException {
        String deleteComentario = "UPDATE favoritos SET `check` = ? "
                + "WHERE idpessoa = ? and idfavoritado = ? and tipofavoritado = ?  ;";

        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(deleteComentario);
            ptmt.setBoolean(1, favorito.isCheck());
            ptmt.setInt(2, favorito.getIdPessoa());
            ptmt.setInt(3, favorito.getIdFavoritado());
            ptmt.setString(4, favorito.getTipoFavoritado());

            ptmt.executeUpdate();

            con.commit();
        } catch (SQLException ex) {

            con.rollback();

            throw new RuntimeException("Erro ao favoritar no banco de dados. " + ex);

        } finally {
            ptmt.close();
        }
    }

    public boolean hasFavorito(Favorito favorito) throws SQLException {
        String sql = "SELECT idfavoritos FROM favoritos WHERE idpessoa = ? and idfavoritado = ? and tipofavoritado = ?";

        try {

            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, favorito.getIdPessoa());
            ptmt.setInt(2, favorito.getIdFavoritado());
            ptmt.setString(3, favorito.getTipoFavoritado());

            resultSet = ptmt.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

    public List<Favorito> getAllFavoritosByIdPessoa(int idPessoa) throws SQLException {
        String sql = "select distinct favoritos.* from favoritos "
                + "inner join relacao on relacao.identidade = favoritos.idfavoritado  "
                + "and relacao.tabela_entidade = 'empresa'  or relacao.tabela_entidade = 'produto' "
                + "where idpessoa = ? and `check` = true";

        try {
            List<Favorito> favoritos = new ArrayList<>();
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, idPessoa);

            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                Favorito favorito = new Favorito();
                favorito.setCheck(resultSet.getBoolean("check"));
                favorito.setIdFavoritado(resultSet.getInt("idfavoritado"));
                favorito.setIdPessoa(resultSet.getInt("idpessoa"));
                favorito.setTipoFavoritado(resultSet.getString("tipofavoritado"));
                favoritos.add(favorito);
            }
            return favoritos;

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

}
