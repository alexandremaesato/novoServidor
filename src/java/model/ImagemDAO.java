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

/**
 *
 * @author Guilherme
 */
public class ImagemDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public void inserirIMagem(Imagem imagem) throws SQLException{
        String sql1 = "INSERT INTO imagem(fktipo_imagem, conteudo, nomeimagem, descricao) VALUES(?,?,?,?);";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setInt(1, imagem.getTipoImagem());
            ptmt.setString(2, imagem.getImg());
            ptmt.setString(3, imagem.getNomeImagem());
            ptmt.setString(4, imagem.getDescricao());
            ptmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir imagem no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
}
