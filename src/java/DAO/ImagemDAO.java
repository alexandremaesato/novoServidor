/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Imagem;

/**
 *
 * @author Guilherme
 */
public class ImagemDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public void inserirImagem(Imagem imagem, String tabela, int identidade) throws SQLException{
        String sql1 = "INSERT INTO imagem(fktipo_imagem, caminho, nomeimagem, descricao) VALUES(?,?,?,?);";
        String sql2 = "INSERT INTO entidade(identidade_criada, deletado, tabela, idresponsavel, data_criacao, data_modificacao, idcriador) values(?,?,?,?,?,?,?);";
        String sql3 = "INSERT INTO relacao(identidade, tabela_entidade, idrelacionada, tabela_relacionada) values(?,?,?,?);";

        try {
                       
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1, imagem.getTipoImagem());
            ptmt.setString(2, imagem.getCaminho());
            ptmt.setString(3, imagem.getNomeImagem());
            ptmt.setString(4, imagem.getDescricao());
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idImagem = resultSet.getInt(1);
            
            ptmt = con.prepareStatement(sql2);
            ptmt.setInt(1, idImagem);
            ptmt.setInt(2, 0);
            ptmt.setString(3, "imagem");
            ptmt.setInt(4, imagem.getPessoaid());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
            ptmt.setString(5, dateFormat.format(date));
            ptmt.setString(6, dateFormat.format(date));
            ptmt.setInt(7, imagem.getPessoaid());
            ptmt.executeUpdate();
            
            ptmt = con.prepareStatement(sql3);
            ptmt.setInt(1, identidade);
            ptmt.setString(2, tabela);
            ptmt.setInt(3, idImagem);
            ptmt.setString(4, "imagem");
            ptmt.executeUpdate();
            
            con.commit();

        } catch (SQLException ex) {
            con.rollback();
            throw new RuntimeException("Erro ao inserir imagem no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void atualizarImagemWeb(Imagem imagem) throws Exception {
        String sql1 = "UPDATE imagem set nomeimagem = ? WHERE idimagem = ?";
        String sql2 = "UPDATE entidade set data_modificacao = ? WHERE identidade_criada = ? AND tabela = 'imagem'";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            con.setAutoCommit(false);
            ptmt.setString(1, imagem.getNomeImagem());
            ptmt.setInt(2, imagem.getImagemid());
            ptmt.executeUpdate();
            
            ptmt = con.prepareStatement(sql2);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
            ptmt.setString(1, dateFormat.format(date));
            ptmt.setInt(2, imagem.getImagemid());
            ptmt.executeUpdate();
            
            con.commit();
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
        }
    }
}
