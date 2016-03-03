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
    
    public Empresa pegarEmpresaporId(int id) throws SQLException{
        String queryEmpresa      = "SELECT * FROM empresa "
                                    + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "WHERE idempresa = ?;";
        
        String queryImagemPerfil = "SELECT DISTINCT imagem.* FROM imagem "
                                    + "INNER JOIN entidade ON imagem.idimagem = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                                    + "WHERE imagem.fktipo_imagem = 1 AND relacao.identidade = ?;";
        
        String queryImagens      = "SELECT DISTINCT imagem.* FROM imagem "
                                    + "INNER JOIN entidade ON imagem.idimagem = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                                    + "WHERE imagem.fktipo_imagem IN (2,3) AND relacao.identidade = ?;";
        
        String queryComentarios  = "SELECT DISTINCT comentario.* FROM comentario "
                                    + "INNER JOIN entidade ON comentario.idcomentario = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON comentario.idcomentario = relacao.idrelacionada AND relacao.tabela_relacionada = 'comentario' "
                                    + "WHERE relacao.identidade = ?;";
        
        String queryTelefone     = "SELECT DISTINCT telefone.* FROM telefone "
                                    + "INNER JOIN entidade ON telefone.idtelefone = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON telefone.idtelefone = relacao.idrelacionada AND relacao.tabela_relacionada = 'telefone' "
                                    + "WHERE relacao.identidade = ?;";
        
        String queryAvaliacao    = "SELECT DISTINCT avaliacao.* FROM avaliacao "
                                    + "INNER JOIN entidade ON avaliacao.idavaliacao = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON avaliacao.idavaliacao = relacao.idrelacionada AND relacao.tabela_relacionada = 'avaliacao' "
                                    + "WHERE relacao.identidade = ?;";
        
        String queryProdutos     = "SELECT DISTINCT produto.* FROM produto "
                                    + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "INNER JOIN relacao ON produto.idproduto = relacao.idrelacionada AND relacao.tabela_relacionada = 'produto' "
                                    + "WHERE relacao.identidade = ?;";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(queryEmpresa);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            Empresa empresa = new Empresa();
            
            if(resultSet.next()){
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNome(resultSet.getString("nome"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
            }
            
            
            
            return empresa;
        } finally {
            ptmt.close();
        }
    }
    
}
