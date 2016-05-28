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
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class AvaliacaoDAO {
    private Connection con = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet    = null;
    
    public int setAvaliacao(Avaliacao avaliacao) throws SQLException{
        String sql = "INSERT INTO avaliacao " +
                        "(idavaliado, idpessoa, avaliacao, descricao, tipoavaliacao) " +
                        "VALUES (?,?,?,?,?);";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ptmt.setInt(1, avaliacao.getAvaliadoid());
                ptmt.setInt(2, avaliacao.getPessoaid());
                ptmt.setInt(3, avaliacao.getNota());
                ptmt.setString(4, avaliacao.getDescricao());
                ptmt.setString(5, avaliacao.getTipoAvalicao());
            
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idAvaliacao = resultSet.getInt(1);
            
            
            return idAvaliacao;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir avaliação no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void updateAvaliacao(Avaliacao avaliacao) throws SQLException{
        
        String sql = "UPDATE avaliacao SET idavaliado = ?, idpessoa = ?, "
                + "avaliacao = ?, descricao = ?, tipoavaliacao = ? WHERE idavaliacao = ?;";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
                ptmt.setInt(1, avaliacao.getAvaliadoid());
                ptmt.setInt(2, avaliacao.getPessoaid());
                ptmt.setInt(3, avaliacao.getNota());
                ptmt.setString(4, avaliacao.getDescricao());
                ptmt.setString(5, avaliacao.getTipoAvalicao());
                ptmt.setInt(6, avaliacao.getAvaliacaoid());
            
            ptmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar a avaliação no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public Avaliacao getAvaliacao(Avaliacao avaliacao) throws SQLException{
        String sql = "SELECT * FROM avaliacao WHERE idpessoa = ? and idavaliado = ? and tipoavaliacao = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, avaliacao.getPessoaid());
            ptmt.setInt(2, avaliacao.getAvaliadoid());
            ptmt.setString(3, avaliacao.getTipoAvalicao());

            resultSet = ptmt.executeQuery();
            if (resultSet.next()) {
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
            }
            return avaliacao;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public Avaliacao getAvaliacao(int idPessoa, int idAvaliado, String tipoAvaliacao) throws SQLException{
        String sql = "SELECT * FROM avaliacao WHERE idpessoa = ? and idavaliado = ? and tipoavaliacao = ?";
        String sqlCreate = "INSERT INTO avaliacao (idpessoa, idavaliado, tipoavaliacao) VALUES (?,?,?);";
        Avaliacao avaliacao = new Avaliacao();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, idPessoa);
            ptmt.setInt(2, idAvaliado);
            ptmt.setString(3, tipoAvaliacao);
            resultSet = ptmt.executeQuery();
            if (resultSet.next()) {
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
            }else{
                ptmt = con.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
                ptmt.setInt(1, idPessoa);
                ptmt.setInt(2, idAvaliado);
                ptmt.setString(3, tipoAvaliacao);
                ptmt.executeUpdate();
                resultSet = ptmt.getGeneratedKeys();
                resultSet.next();
                avaliacao.setAvaliacaoid(resultSet.getInt(1));
                avaliacao.setAvaliadoid(idAvaliado);
                avaliacao.setPessoaid(idPessoa);
                avaliacao.setTipoAvalicao(tipoAvaliacao);
            }
            return avaliacao;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public Avaliacao getAvaliacaoById(int id) throws SQLException{
        String sql = "SELECT * FROM avaliacao WHERE idavaliacao = ?";
        
        Avaliacao avaliacao = new Avaliacao();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            if (resultSet.next()) {
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
            }
            return avaliacao;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    

}

