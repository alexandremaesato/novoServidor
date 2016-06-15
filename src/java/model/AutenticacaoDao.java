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
 * @author Alexandre
 */
public class AutenticacaoDao {

    static private Connection con = null;
    static PreparedStatement ptmt = null;
    static ResultSet resultSet = null;

    public static boolean autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM autenticacao WHERE login = ? and senha = ?";

        ResultSet rs;
        boolean retorno = false;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, login);
            ptmt.setString(2, senha);
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.first())
                retorno = true;
            

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao se logar no banco de dados. " + ex);

        } finally {
            ptmt.close();
            return retorno;
        }
    }

    public static void gravarToken(TokenInfo tokeninfo, String token) throws SQLException {
        String sql = "update";
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, token);
            ptmt.setString(2, tokeninfo.getEmail());
            ptmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir token no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

    public static void limpaToken(String login) throws SQLException {
        String sql = "update";
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, login);
            ptmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao limpar o token no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

    public static String getToken(String login) throws SQLException {
        String sql1 = "select";//pegar o token
        String sql2 = "update";//setar para false o tokenStatus
        String token;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setString(1, login);
            resultSet = ptmt.executeQuery();
            token = resultSet.getString("token");

            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql2);
            ptmt.setString(1, login);
            ptmt.executeQuery();

            return token;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao limpar o token no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

    public static int criarAutenticacao(String login, String senha) throws SQLException {
        String sql1 = "INSERT INTO autenticacao(login, senha) values(?,?);".toLowerCase();
        String sql2 = "INSERT INTO pessoa(fkautenticacao) values(?);".toLowerCase();

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ptmt.setString(1, login);
            ptmt.setString(2, senha);
            ptmt.executeUpdate();

            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int fkautenticacao = resultSet.getInt(1);

            ptmt = con.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1, fkautenticacao);
            ptmt.executeUpdate();
            
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idPessoa = resultSet.getInt(1);
            
            return idPessoa;

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao criar Autenticação no banco de dados. " + ex);
        } finally {
            ptmt.close();
        }
    }

    public static boolean verificaToken(TokenInfo tokenInfo, String token) throws SQLException {
        String sql1 = "SELECT login FROM autenticacao WHERE login = ?;".toLowerCase();
        String sql2 = "SELECT token FROM tokens WHERE login = ? and token = ?;".toLowerCase();
        boolean result1 = false;
        boolean result2 = true;
        
        ResultSet rs;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setString(1, tokenInfo.getEmail());
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.next()){
                result1 =  true;    
            }
            
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql2);
            ptmt.setString(1, tokenInfo.getEmail());
            ptmt.setString(2, token);
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.next()){
                result2 =  false;
            }
            
            if (result1 && result2){
                return true;
            }
            return false;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro: " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public static boolean verificaEmail(TokenInfo tokenInfo) throws SQLException {
        String sql1 = "SELECT login FROM autenticacao WHERE login = ?;".toLowerCase();
               
        ResultSet rs;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setString(1, tokenInfo.getEmail());
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.next()){
                return true;
            }
        
            return false;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro: " + ex);
        } finally {
            ptmt.close();
        }
    }
    
    public static boolean verificaEmail(String email) throws SQLException {
        String sql1 = "SELECT login FROM autenticacao WHERE login = ?;".toLowerCase();
        boolean retorno = false;       
        ResultSet rs;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setString(1, email);
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.first()){
                retorno = true;
            }     
            return retorno;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro: " + ex);
        } finally {
            ptmt.close();
            return retorno;
        }
    }
    
    public int getPessoaId(String login, String senha) throws SQLException{
        String sql1 = "select idpessoa from pessoa  " +
                      "inner join autenticacao on autenticacao.idautenticacao = pessoa.fkautenticacao " +
                      "where login = ? and senha = ?;";
        ResultSet rs;
        int id = 0;
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql1);
            ptmt.setString(1, login);
            ptmt.setString(2, senha);
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            if(rs.next()){
                id = rs.getInt("idpessoa");
            }     
            return id;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro: " + ex);
        } finally {
            ptmt.close();
        }
    }

}
