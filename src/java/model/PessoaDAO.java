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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class PessoaDAO {
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;
    
    public Pessoa getPessoaById(int idPessoa) throws SQLException{
        String sql = "SELECT distinct * FROM pessoa " +
                     "left join relacao on pessoa.idpessoa = identidade and tabela_entidade = 'pessoa' " +
                     "left join imagem on imagem.idimagem = idrelacionada " +
                     "where idpessoa = ? " +
                     "order by idrelacionada DESC " +
                     "limit 1"; 
        
        Pessoa pessoa = new Pessoa();
        Imagem imagem = new Imagem();
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, idPessoa);
            resultSet = ptmt.executeQuery();
            if(resultSet.next()){
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setSobrenome(resultSet.getString("sobrenome"));
                pessoa.setPessoaid(resultSet.getInt("idpessoa"));
                //pessoa.setDataNascimento(resultSet.getDate("data_nascimento"));
                
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setPessoaid(idPessoa);
                
                pessoa.setImagemPerfil(imagem);
                
            }
            return pessoa;

            
        } catch (SQLException ex){
            throw new RuntimeException("Erro ao pegar os dados no banco de dados. " + ex);
            
        }finally{
            ptmt.close();
        }
    }
    
    public boolean updatePessoa(Pessoa pessoa) throws SQLException{
         String sql = "UPDATE pessoa SET nome = ?, sobrenome = ?  WHERE idpessoa = ?"; 
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, pessoa.getNome());
            ptmt.setString(2, pessoa.getSobrenome());
            //ptmt.setDate(3, pessoa.getDataNascimento());
            ptmt.setInt(3, pessoa.getPessoaid());
            //resultSet = ptmt.executeUpdate();
            if(ptmt.executeUpdate() >0){
                return true;
            }
            return false;

            
        } catch (SQLException ex){
            throw new RuntimeException("Erro ao pegar os dados no banco de dados. " + ex);
        }finally{
            ptmt.close();
        }
        
    }
    
    public Pessoa pegarPessoaPorId(Integer id) throws Exception {
        String pessoa = "select * from pessoa "
                            + "inner join autenticacao on pessoa.fkautenticacao = autenticacao.idautenticacao "
                            + "left join relacao on pessoa.idpessoa = identidade and tabela_entidade = 'pessoa' "
                            + "left join imagem on idimagem = idrelacionada "
                            + "where idpessoa = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(pessoa);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            
            Pessoa p = null;
            if(resultSet.next()) {
                p = new Pessoa();
                p.setNome(resultSet.getString("nome"));
                p.setSobrenome(resultSet.getString("sobrenome"));
                p.setCpf(resultSet.getString("cpf"));
                p.setDataNascimento(resultSet.getDate("data_nascimento"));
                p.setLogin(resultSet.getString("login"));
                p.setPessoaid(id);
                
                    Imagem imagem = new Imagem();
                    imagem.setImagemid(resultSet.getInt("idimagem"));
                    imagem.setCaminho(resultSet.getString("caminho"));
                    imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                p.setImagemPerfil(imagem);
            }
            return p;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pessoa no banco de dados: " + e);
        }
    }

    public void atualizarPessoa(Pessoa p) throws Exception {
        String pessoa = "UPDATE pessoa SET nome = ?, sobrenome = ?, cpf = ? WHERE idpessoa = ?";
        String autenticacao = "UPDATE autenticacao SET login = ? WHERE idautenticacao = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(pessoa);
            ptmt.setString(1, p.getNome());
            ptmt.setString(2, p.getSobrenome());
            ptmt.setString(3, p.getCpf());
            ptmt.setInt(4, p.getPessoaid());
            ptmt.executeUpdate();
            
            ptmt = con.prepareStatement("select fkautenticacao from pessoa where idpessoa = ?");
            ptmt.setInt(1, p.getPessoaid());
            resultSet = ptmt.executeQuery();
            
            Integer idautenticacao = 0;
            if(resultSet.next()) {
                idautenticacao = resultSet.getInt("fkautenticacao");
            }
            
            ptmt = con.prepareStatement(autenticacao);
            ptmt.setString(1, p.getLogin());
            ptmt.setInt(2, idautenticacao);
            ptmt.execute();
            
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new RuntimeException("Erro ao buscar pessoa no banco de dados: " + e);
        }
    }
    
    public void atualizarSenha(Pessoa p) throws Exception {
        String autenticacao = "UPDATE autenticacao SET senha = ? WHERE idautenticacao = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement("select fkautenticacao from pessoa where idpessoa = ?");
            ptmt.setInt(1, p.getPessoaid());
            resultSet = ptmt.executeQuery();
            
            Integer idautenticacao = 0;
            if(resultSet.next()) {
                idautenticacao = resultSet.getInt("fkautenticacao");
            }
            
            ptmt = con.prepareStatement(autenticacao);
            ptmt.setString(1, p.getSenha());
            ptmt.setInt(2, idautenticacao);
            ptmt.execute();
            
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new RuntimeException("Erro ao buscar pessoa no banco de dados: " + e);
        }
    }
    
}
