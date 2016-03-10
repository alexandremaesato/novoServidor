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
    static ResultSet resultSet    = null;
    
    public static boolean autenticar(String login, String senha) throws SQLException{
        String sql = "";
        ResultSet rs; 
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, login);
            ptmt.setString(2, senha);
            
            ptmt.executeQuery();
            rs = ptmt.getResultSet();
            return rs.next();
        }catch (SQLException ex) {
            throw new RuntimeException("Erro ao se logar no banco de dados. "+ex);
            
        } finally {
            ptmt.close();
            return false;
        }
    }
    
    public static void gravarToken(String token, String login) throws SQLException{
        String sql    = "update";
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, token);
            ptmt.setString(2, login);
            ptmt.executeUpdate();
    
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir token no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public static void limpaToken(String login) throws SQLException{
        String sql    = "update";
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, login);
            ptmt.executeUpdate();
    
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao limpar o token no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public static String getToken(String login) throws SQLException{
        String sql1    = "select";//pegar o token
        String sql2    = "update";//setar para false o tokenStatus
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
            throw new RuntimeException("Erro ao limpar o token no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public static void criarAutenticacao(String login, String senha) throws SQLException{
        String sql    = "insert";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ptmt.setString(1, login);
            ptmt.setString(2, senha);
            ptmt.executeQuery();
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao criar Autenticação no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
}
