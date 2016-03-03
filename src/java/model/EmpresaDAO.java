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
    
    public Empresa pegarEmpresaPorId(int id) throws SQLException{
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
            Empresa empresa = new Empresa();
            
            resultSet = retornaResultadoQuery(queryEmpresa, id);
            if(resultSet.next()){
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNome(resultSet.getString("nome"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
            }
            
            resultSet = retornaResultadoQuery(queryImagemPerfil, id);
            if(resultSet.next()){
                Imagem imagemPerfil = new Imagem();
                imagemPerfil.setImagemid(resultSet.getInt("idimagem"));
                imagemPerfil.setNome(resultSet.getString("nome"));
                imagemPerfil.setDescricao(resultSet.getString("descricao"));
                imagemPerfil.setCaminho(resultSet.getString("caminho"));
                empresa.setImagemPerfil(imagemPerfil);
            }
            
            resultSet = retornaResultadoQuery(queryImagens, id);
            List<Imagem> imagensOficiais = new ArrayList<Imagem>();
            List<Imagem> imagensNaoOficiais = new ArrayList<Imagem>();
            while(resultSet.next()){
                Imagem imagem = new Imagem();
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setNome(resultSet.getString("nome"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));
                if(imagem.getTipoImagem() == 2){
                    imagensOficiais.add(imagem);
                } else{
                    imagensNaoOficiais.add(imagem);
                }
            }
            empresa.setImagensOficiais(imagensOficiais);
            empresa.setImagensNaoOficiais(imagensNaoOficiais);
            
            resultSet = retornaResultadoQuery(queryComentarios, id);
            List<Comentario> comentarios = new ArrayList<Comentario>();
            while(resultSet.next()){
                Comentario comentario = new Comentario();
                comentario.setComentadoid(resultSet.getInt("idcomentario"));
                comentario.setDescricao(resultSet.getString("descricao"));
                comentario.setComentarioDependenteid(resultSet.getInt("fkidcomentario_dependente"));
                comentario.setPessoaid(resultSet.getInt("fkpessoa"));
                comentario.setModificado(resultSet.getInt("modificado"));
                comentarios.add(comentario);
            }
            empresa.setComentarios(comentarios);
            
            resultSet = retornaResultadoQuery(queryTelefone, id);
            List<Telefone> telefones = new ArrayList<Telefone>();
            while(resultSet.next()){
                Telefone telefone = new Telefone();
                telefone.setTelefoneid(resultSet.getInt("idtelefone"));
                telefone.setNumero(resultSet.getString("numero"));
                telefone.setTipoTelefone(resultSet.getString("tipo_telefone"));
                telefones.add(telefone);
            }
            empresa.setTelefones(telefones);
            
            resultSet = retornaResultadoQuery(queryAvaliacao, id);
            List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>();
            while(resultSet.next()){
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setAvaliacaoid(resultSet.getInt("idavaliacao"));
                avaliacao.setDescricao(resultSet.getString("descricao"));
                avaliacao.setNota(resultSet.getInt("avaliacao"));
                avaliacao.setAvaliadoid(resultSet.getInt("idavaliado"));
                avaliacao.setPessoaid(resultSet.getInt("idpessoa"));
                avaliacao.setTipoAvalicao(resultSet.getString("tipoavaliacao"));
                avaliacoes.add(avaliacao);
            }
            empresa.setAvaliacoes(avaliacoes);
            
            resultSet = retornaResultadoQuery(queryProdutos, id);
            List<Produto> produtos = new ArrayList<Produto>();// criar metodo pra pegar produtos
            
            
            
            
            return empresa;
        } finally {
            ptmt.close();
        }
    }
    
    public ResultSet retornaResultadoQuery(String query, int id) throws SQLException{
        con = ConnectionFactory.getConnection();
        ptmt = con.prepareStatement(query);
        ptmt.setInt(1, id);
        resultSet = ptmt.executeQuery();
        return resultSet;
    }
    
}
