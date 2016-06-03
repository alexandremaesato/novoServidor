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
import java.util.ArrayList;
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
        String sql = "SELECT distinct * FROM pessoa where idpessoa = ?"; 
        
        Pessoa pessoa = new Pessoa();
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, idPessoa);
            resultSet = ptmt.executeQuery();
            if(resultSet.next()){
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setSobrenome(resultSet.getString("sobrenome"));
                pessoa.setPessoaid(resultSet.getInt("idpessoa"));
            }
            return pessoa;

            
        } catch (SQLException ex){
            throw new RuntimeException("Erro ao pegar os dados no banco de dados. " + ex);
            
        }finally{
            ptmt.close();
        }
    }

}
