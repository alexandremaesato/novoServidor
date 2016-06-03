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
 * @author Guilherme
 */
public class ProdutoDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public int cadastrarProduto(Produto prod, int pessoaid) throws SQLException{
        
        String cadastrarEmpresa  = "INSERT INTO produto(nomeproduto, descricao, preco, fkcategoria, fktipo_culinaria) values(?,?,?,?,?);";
        String cadastrarEntidade = "INSERT INTO entidade(identidade_criada, deletado, tabela, idresponsavel, data_criacao, data_modificacao, idcriador) values(?,?,?,?,?,?,?);";
        String cadastrarRelacao  = "INSERT INTO relacao(identidade, tabela_entidade, idrelacionada, tabela_relacionada) values(?,?,?,?);";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(cadastrarEmpresa, Statement.RETURN_GENERATED_KEYS);
                ptmt.setString(1, prod.getNomeProduto());
                ptmt.setString(2, prod.getDescricao());
                ptmt.setDouble(3, prod.getPreco());
                ptmt.setInt(4, prod.getCategoria());
                ptmt.setInt(5, prod.getCulinaria());
            
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idProduto = resultSet.getInt(1);
            
            
            ptmt = con.prepareStatement(cadastrarEntidade, Statement.RETURN_GENERATED_KEYS);
                ptmt.setInt(1, idProduto);
                ptmt.setInt(2, 0);
                ptmt.setString(3, "produto");
                ptmt.setInt(4, pessoaid);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                ptmt.setString(5, dateFormat.format(date));
                ptmt.setString(6, dateFormat.format(date));
                ptmt.setInt(7, pessoaid);
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idEntidade = resultSet.getInt(1);
            

            ptmt = con.prepareStatement(cadastrarRelacao);
                ptmt.setInt(1, prod.getEmpresaid());
                ptmt.setString(2, "empresa");
                ptmt.setInt(3, idEntidade);
                ptmt.setString(4, "produto");
            ptmt.executeUpdate();
            
            return idEntidade;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
}
