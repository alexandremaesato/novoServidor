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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guilherme
 */
public class EmpresaDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public void cadastrarEmpresa(Empresa emp) throws SQLException{
        String queryEmpresa  = "INSERT INTO empresa(nome, cnpj, descricao) values(?,?,?);";
        String queryEntidade = "INSERT INTO entidade(identidade_criada, deletado, tabela, idresponsavel, data_criacao, data_modificacao, idcriador) values(?,?,?,?,?,?,?);";
        int idEmpresa = 0;
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(queryEmpresa, Statement.RETURN_GENERATED_KEYS);
            ptmt.setString(1, emp.getNome());
            ptmt.setString(2, emp.getCnpj());
            ptmt.setString(3, emp.getDescricao());
            
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            idEmpresa = resultSet.getInt(1);
            
            
            ptmt = con.prepareStatement(queryEntidade);
            ptmt.setInt(1, idEmpresa);
            ptmt.setInt(2, 0);
            ptmt.setString(3, "empresa");
            ptmt.setInt(4, 1);
            ptmt.setString(5, "2016-02-22");
            ptmt.setString(6, "2016-02-22");
            ptmt.setInt(7, 1);
            
            ptmt.executeUpdate();
            
            
        } finally {
            ptmt.close();
        }
    }
    
    public List<Empresa> pegarEmpresas() throws SQLException{
        String queryEmpresa  = "SELECT * FROM empresa;";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(queryEmpresa);
            resultSet = ptmt.executeQuery();
            List<Empresa> empresas = new ArrayList<Empresa>();
            
            while (resultSet.next()) { 
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNome(resultSet.getString("nome"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
                empresas.add(empresa);
            }
            return empresas;
        } finally {
            ptmt.close();
        }
    }
    
}
