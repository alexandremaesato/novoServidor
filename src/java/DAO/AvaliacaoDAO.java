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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Avaliacao;
import model.Pessoa;

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
                        "(idavaliado, idpessoa, avaliacao, descricao, tipoavaliacao, data_criacao, data_modificacao) " +
                        "VALUES (?,?,?,?,?,?,?);";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ptmt.setInt(1, avaliacao.getAvaliadoid());
                ptmt.setInt(2, avaliacao.getPessoaid());
                ptmt.setInt(3, avaliacao.getNota());
                ptmt.setString(4, avaliacao.getDescricao());
                ptmt.setString(5, avaliacao.getTipoAvalicao());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                ptmt.setString(6, dateFormat.format(date));
                ptmt.setString(7, dateFormat.format(date));
            
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
                + "avaliacao = ?, descricao = ?, tipoavaliacao = ?, data_modificacao = ?  WHERE idavaliacao = ?;";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
                ptmt.setInt(1, avaliacao.getAvaliadoid());
                ptmt.setInt(2, avaliacao.getPessoaid());
                ptmt.setInt(3, avaliacao.getNota());
                ptmt.setString(4, avaliacao.getDescricao());
                ptmt.setString(5, avaliacao.getTipoAvalicao());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                ptmt.setString(6, dateFormat.format(date));
                ptmt.setInt(7, avaliacao.getAvaliacaoid());
                
            
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
    
    public int getIdAvaliacao(Avaliacao avaliacao) throws SQLException{
        String sql = "SELECT idavaliacao FROM avaliacao WHERE idpessoa = ? and idavaliado = ? and tipoavaliacao = ?";
        int id =0;
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, avaliacao.getPessoaid());
            ptmt.setInt(2, avaliacao.getAvaliadoid());
            ptmt.setString(3, avaliacao.getTipoAvalicao());

            resultSet = ptmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idavaliacao");
            }
            return -1;
            
            
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
    
    public List<Object> getAvaliacoesByIdEmpresa(int id) throws SQLException{
        String sql = "select distinct * from avaliacao " +
                     "inner join pessoa on pessoa.idpessoa = avaliacao.idpessoa " +
                     "where idavaliado = ? and tipoavaliacao = 'empresa' and data_modificacao IS NOT NULL " + 
                     "order by idavaliacao DESC " +
                     "limit 3";
        
        List<Avaliacao> avaliacoes = new ArrayList<>();
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
                avaliacao.setData_criacao(resultSet.getDate("data_criacao"));
                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao"));
                avaliacoes.add(avaliacao);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setPessoaid(resultSet.getInt("idpessoa"));
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setSobrenome(resultSet.getString("sobrenome"));
                pessoas.add(pessoa);
            }
            List<Object> lista = new ArrayList<>();
            lista.add(avaliacoes);
            lista.add(pessoas);
            
            return lista;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
//    public List<Avaliacao> getAvaliacoesByIdProduto(int id) throws SQLException{
//        String sql = "select distinct * from avaliacao " +
//                     "left join pessoa on pessoa.idpessoa = avaliacao.idpessoa " +
//                     "where idavaliado = ? and tipoavaliacao = 'produto' " + 
//                     "order by data_modificacao DESC ";
//                     
//        List<Avaliacao> avaliacoes = new ArrayList<>();
//        try {
//            con = ConnectionFactory.getConnection();
//            ptmt = con.prepareStatement(sql);
//            ptmt.setInt(1, id);
//            resultSet = ptmt.executeQuery();
//            while (resultSet.next()) {
//                Avaliacao avaliacao = new Avaliacao();
//                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
//                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
//                avaliacao.setDescricao(resultSet.getString("descricao"));
//                avaliacao.setNota(resultSet.getInt("avaliacao"));
//                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
//                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
//                avaliacao.setData_criacao(resultSet.getDate("data_criacao"));
//                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao")); 
//                avaliacoes.add(avaliacao);
//                
//                
//            }
//            return avaliacoes;
//            
//        } catch (SQLException ex) {
//            throw new RuntimeException("Erro ao buscar as avaliações no banco de dados. " + ex);
//        } finally {
//            ptmt.close();
//        }
//    }
    
     public List<Object> getAvaliacoesByIdProduto(int id) throws SQLException{
        String sql = "select distinct * from avaliacao " +
                     "left join pessoa on pessoa.idpessoa = avaliacao.idpessoa " +
                     "where idavaliado = ? and tipoavaliacao = 'produto' " + 
                     "order by data_modificacao DESC ";
                     
        List<Object> objetos = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            List<Avaliacao> avaliacoes = new ArrayList<>();
            List<Pessoa> pessoas = new ArrayList<>();
            
            while (resultSet.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
                avaliacao.setData_criacao(resultSet.getDate("data_criacao"));
                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao")); 
                avaliacoes.add(avaliacao);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setSobrenome(resultSet.getString("sobrenome"));
                pessoas.add(pessoa);
            }
            List<Object> lista = new ArrayList<>();
            lista.add(avaliacoes);
            lista.add(pessoas);
            
            return lista;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar as avaliações no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Avaliacao> pegarAvaliacoesPorIdEmpresa(int id) throws SQLException{
        String sql = "SELECT DISTINCT * FROM avaliacao "
                        + "INNER JOIN pessoa ON pessoa.idpessoa = avaliacao.idpessoa "
                        + "WHERE idavaliado = ? AND tipoavaliacao = 'empresa' "
                        + "ORDER BY data_modificacao DESC";
        
        List<Avaliacao> avaliacoes = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
                avaliacao.setData_criacao(resultSet.getDate("data_criacao"));
                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao"));
                
                Pessoa pessoa = new Pessoa();
                    pessoa.setPessoaid(resultSet.getInt("idpessoa"));
                    pessoa.setNome(resultSet.getString("nome"));
                    pessoa.setSobrenome(resultSet.getString("sobrenome"));
                avaliacao.setPessoa(pessoa);
                avaliacoes.add(avaliacao);
            }
            return avaliacoes;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar a avaliação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Avaliacao> pegarAvaliacoesPorIdProduto(int id) throws Exception {
        String sql = "SELECT DISTINCT * FROM avaliacao "
                        + "INNER JOIN pessoa ON pessoa.idpessoa = avaliacao.idpessoa "
                        + "WHERE idavaliado = ? AND tipoavaliacao = 'produto' "
                        + "ORDER BY data_modificacao DESC";
        
        List<Avaliacao> avaliacoes = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
                avaliacao.setData_criacao(resultSet.getDate("data_criacao"));
                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao"));
                
                Pessoa pessoa = new Pessoa();
                    pessoa.setPessoaid(resultSet.getInt("idpessoa"));
                    pessoa.setNome(resultSet.getString("nome"));
                    pessoa.setSobrenome(resultSet.getString("sobrenome"));
                avaliacao.setPessoa(pessoa);
                avaliacoes.add(avaliacao);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ptmt.close();
        }
        return avaliacoes;
    }
    
}

