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

/**
 *
 * @author Guilherme
 */
public class ComentarioDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public void inserirComentario(Comentario comentario) throws SQLException{
        
        String insereComentario = "INSERT INTO comentario( descricao, modificado, fkpessoa, fkidcomentario_dependente ) VALUES( ?,?,?,? )";
        String insereRelacao    = "INSERT INTO relacao( identidade, tabela_identidade, idrelacionada, tabela_relacionada ) VALUES( ?,?,?,? )";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(insereComentario, Statement.RETURN_GENERATED_KEYS);
                ptmt.setString(1, comentario.getDescricao());
                ptmt.setInt(2, 0);
                ptmt.setInt(3, comentario.getPessoaid());
                ptmt.setInt(4, comentario.getComentarioDependenteid());
            
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idComentario = resultSet.getInt(1);
            
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(insereRelacao);
                ptmt.setInt(1, comentario.getComentadoid());
                ptmt.setString(2, comentario.getTipoComentado());
                ptmt.setInt(3, idComentario);
                ptmt.setString(4, "comentario");
            
            ptmt.executeUpdate();
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir comentario no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void atualizarComentarios(Comentario comentario) throws SQLException{
        
        String alteraEmpresa = "UPDATE comentario SET descricao = ?, modificado = ? WHERE idcomentario = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(alteraEmpresa);
                ptmt.setString(1, comentario.getDescricao());
                ptmt.setInt(2, comentario.getComentadoid());
            
            ptmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar comentario no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
}
