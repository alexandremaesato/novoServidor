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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alexandre
 */
public class EnderecoDao {

    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null; 
    
    public List<String> getCidades(String estado) throws SQLException{
        
        String sql = "SELECT DISTINCT cidade FROM endereco WHERE estado=?";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, estado);
            resultSet = ptmt.executeQuery();
            List<String> cidades = new ArrayList<String>();
            
            while (resultSet.next()) {
                String cidade = null;
                cidade = resultSet.getString("cidade");
                cidades.add(cidade);
            }
            return cidades;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<String> getBairros(String cidade) throws SQLException{
        
        String sql = "SELECT DISTINCT bairro FROM endereco WHERE cidade = ?";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1, cidade);
            resultSet = ptmt.executeQuery();
            List<String> bairros = new ArrayList<String>();
            
            while (resultSet.next()) {
                String bairro = null;
                cidade = resultSet.getString("bairro");
                bairros.add(cidade);
            }
            return bairros;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro"+ex);
        } finally {
            ptmt.close();
        }
    }
}
