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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Guilherme
 */
public class EmpresaDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public void cadastrarEmpresa(Empresa emp, Pessoa p) throws SQLException{
        String cadastrarEmpresa  = "INSERT INTO empresa(nomeempresa, cnpj, descricao) values(?,?,?);";
        String cadastrarEntidade = "INSERT INTO entidade(identidade_criada, deletado, tabela, idresponsavel, data_criacao, data_modificacao, idcriador) values(?,?,?,?,?,?,?);";
        String cadastrarEndereco = "INSERT INTO endereco(rua, numero, complemento, cep, bairro, cidade, estado, pais) values(?,?,?,?,?,?,?,?);";
        String cadastrarTelefone = "INSERT INTO telefone(numero, tipo_telefone) values(?,?);";
        String cadastrarRelacao  = "INSERT INTO relacao(identidade, tabela_entidade, idrelacionada, tabela_relacionada) values(?,?,?,?);";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(cadastrarEmpresa, Statement.RETURN_GENERATED_KEYS);
                ptmt.setString(1, emp.getNomeEmpresa());
                ptmt.setString(2, emp.getCnpj());
                ptmt.setString(3, emp.getDescricao());
            
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idEmpresa = resultSet.getInt(1);
            
            
            ptmt = con.prepareStatement(cadastrarEntidade);
                ptmt.setInt(1, idEmpresa);
                ptmt.setInt(2, 0);
                ptmt.setString(3, "empresa");
                ptmt.setInt(4, p.getPessoaid());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                ptmt.setString(5, dateFormat.format(date));
                ptmt.setString(6, dateFormat.format(date));
                ptmt.setInt(7, p.getPessoaid());
            ptmt.executeUpdate();
            
            
            Endereco endereco = emp.getEndereco();
            ptmt = con.prepareStatement(cadastrarEndereco);
                ptmt.setString(1, endereco.getRua());
                ptmt.setString(2, endereco.getNumero());
                ptmt.setString(3, endereco.getComplemento());
                ptmt.setString(4, endereco.getCep());
                ptmt.setString(5, endereco.getBairro());
                ptmt.setString(6, endereco.getCidade());
                ptmt.setString(7, endereco.getEstado());
                ptmt.setString(8, endereco.getPais());
            ptmt.executeUpdate();
            resultSet = ptmt.getGeneratedKeys();
            resultSet.next();
            int idEndereco = resultSet.getInt(1);
            
            ptmt = con.prepareStatement(cadastrarRelacao);
                ptmt.setInt(1, idEmpresa);
                ptmt.setString(2, "empresa");
                ptmt.setInt(3, idEndereco);
                ptmt.setString(4, "endereco");
            
            
            List<Telefone> telefones = emp.getTelefones();
            for( Telefone telefone : telefones ){
                ptmt = con.prepareStatement(cadastrarTelefone);
                    ptmt.setString(1, telefone.getNumero());
                    ptmt.setString(1, telefone.getTipoTelefone());
                ptmt.executeUpdate();
                resultSet = ptmt.getGeneratedKeys();
                resultSet.next();
                int idTelefone = resultSet.getInt(1);

                ptmt = con.prepareStatement(cadastrarRelacao);
                    ptmt.setInt(1, idEmpresa);
                    ptmt.setString(2, "empresa");
                    ptmt.setInt(3, idTelefone);
                    ptmt.setString(4, "telefone");
            }
            
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void atualizarEmpresa(Empresa emp){
        //TODO
    }
    
    public List<Empresa> pegarEmpresas() throws SQLException{
        String buscaEmpresa = "SELECT DISTINCT "
                               + "empresa.*, imagem.*, endereco.*, "
                               + "(SELECT COUNT(*) FROM comentario "
                                  + "INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                                  + "WHERE relacao.identidade = empresa.idempresa AND relacao.tabela_entidade = 'empresa') AS qtdecomentarios, "
                               + "(SELECT COUNT(*) FROM avaliacao "
                                  +  "INNER JOIN relacao ON relacao.idrelacionada = avaliacao.idavaliacao AND relacao.tabela_relacionada = 'avaliacao' "
                                  + "WHERE relacao.identidade = empresa.idempresa AND relacao.tabela_entidade = 'empresa') AS qtdeavaliacoes "
                               + "FROM empresa "
                               + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                               + "LEFT JOIN relacao ri ON ri.identidade = empresa.idempresa AND ri.tabela_relacionada = 'imagem' "
                               + "LEFT JOIN relacao ren ON ren.identidade = empresa.idempresa AND ren.tabela_relacionada = 'endereco' "
                               + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada AND imagem.fktipo_imagem = 1 "
                               + "LEFT JOIN endereco  ON endereco.idendereco = ren.idrelacionada "
                               + "GROUP BY empresa.idempresa";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(buscaEmpresa);
            resultSet = ptmt.executeQuery();
            List<Empresa> empresas = new ArrayList<Empresa>();
            
            while (resultSet.next()) { 
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
                
                Imagem imagemPerfil = new Imagem();
                imagemPerfil.setImagemid(resultSet.getInt("idimagem"));
                imagemPerfil.setNome(resultSet.getString("nomeimagem"));
                imagemPerfil.setCaminho(resultSet.getString("caminho"));
                imagemPerfil.setDescricao(resultSet.getString("descricao"));
                imagemPerfil.setTipoImagem(resultSet.getInt("fktipo_imagem"));
                
                Endereco endereco = new Endereco();
                endereco.setEnderecoid(resultSet.getInt("idendereco"));
                endereco.setRua(resultSet.getString("rua"));
                endereco.setBairro(resultSet.getString("bairro"));
                endereco.setCep(resultSet.getString("cep"));
                endereco.setNumero(resultSet.getString("numero"));
                endereco.setComplemento(resultSet.getString("complemento"));
                endereco.setCidade(resultSet.getString("cidade"));
                endereco.setEstado(resultSet.getString("estado"));
                endereco.setPais(resultSet.getString("pais"));
                
                empresa.setImagemPerfil(imagemPerfil);
                empresa.setEndereco(endereco);
                empresa.setQtdeComentarios(resultSet.getInt("qtdecomentarios"));
                empresa.setQtdeComentarios(resultSet.getInt("qtdeavaliacoes"));
                
                empresas.add(empresa);
            }
            return empresas;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao busca empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public Empresa pegarEmpresaPorId(int id) throws SQLException{
        
        String buscarEmpresa      = "SELECT * FROM empresa "
                                     + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                                     + "WHERE idempresa = ?;";
        
        String buscarImagemPerfil = "SELECT DISTINCT imagem.* FROM imagem "
                                     + "INNER JOIN entidade ON imagem.idimagem = entidade.identidade_criada AND entidade.deletado = 0 "
                                     + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                                     + "WHERE imagem.fktipo_imagem = 1 AND relacao.identidade = ?;";
        
        String buscarImagens      = "SELECT DISTINCT imagem.* FROM imagem "
                                     + "INNER JOIN entidade ON imagem.idimagem = entidade.identidade_criada AND entidade.deletado = 0 "
                                     + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                                     + "WHERE imagem.fktipo_imagem IN (2,3) AND relacao.identidade = ?;";
        
        String buscarComentarios  = "SELECT DISTINCT comentario.* FROM comentario "
                                     + "INNER JOIN entidade ON comentario.idcomentario = entidade.identidade_criada AND entidade.deletado = 0 "
                                     + "INNER JOIN relacao ON comentario.idcomentario = relacao.idrelacionada AND relacao.tabela_relacionada = 'comentario' "
                                     + "WHERE relacao.identidade = ?;";
        
        String buscarTelefones     = "SELECT DISTINCT telefone.* FROM telefone "
                                     + "INNER JOIN relacao ON telefone.idtelefone = relacao.idrelacionada AND relacao.tabela_relacionada = 'telefone' "
                                     + "WHERE relacao.identidade = ?;";
        
        String buscarAvaliacao    = "SELECT DISTINCT avaliacao.* FROM avaliacao "
                                     + "INNER JOIN entidade ON avaliacao.idavaliacao = entidade.identidade_criada AND entidade.deletado = 0 "
                                     + "INNER JOIN relacao ON avaliacao.idavaliacao = relacao.idrelacionada AND relacao.tabela_relacionada = 'avaliacao' "
                                     + "WHERE relacao.identidade = ?;";
        
        try{
            Empresa empresa = new Empresa();
            
            resultSet = retornaResultadoQuery(buscarEmpresa, id);
            if(resultSet.next()){
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
            }
            
            resultSet = retornaResultadoQuery(buscarImagemPerfil, id);
            if(resultSet.next()){
                Imagem imagemPerfil = new Imagem();
                imagemPerfil.setImagemid(resultSet.getInt("idimagem"));
                imagemPerfil.setNome(resultSet.getString("nomeimagem"));
                imagemPerfil.setDescricao(resultSet.getString("descricao"));
                imagemPerfil.setCaminho(resultSet.getString("caminho"));
                empresa.setImagemPerfil(imagemPerfil);
            }
            
            resultSet = retornaResultadoQuery(buscarImagens, id);
            List<Imagem> imagensOficiais = new ArrayList<Imagem>();
            List<Imagem> imagensNaoOficiais = new ArrayList<Imagem>();
            while(resultSet.next()){
                Imagem imagem = new Imagem();
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setNome(resultSet.getString("nomeimagem"));
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
            
            resultSet = retornaResultadoQuery(buscarComentarios, id);
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
            
            resultSet = retornaResultadoQuery(buscarTelefones, id);
            List<Telefone> telefones = new ArrayList<Telefone>();
            while(resultSet.next()){
                Telefone telefone = new Telefone();
                telefone.setTelefoneid(resultSet.getInt("idtelefone"));
                telefone.setNumero(resultSet.getString("numero"));
                telefone.setTipoTelefone(resultSet.getString("tipo_telefone"));
                telefones.add(telefone);
            }
            empresa.setTelefones(telefones);
            
            resultSet = retornaResultadoQuery(buscarAvaliacao, id);
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
            
            List<Produto> produtos = pegarProdutosPorEmpresa(id);
            empresa.setProdutos(produtos);     
            
            return empresa;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar empresa no banco de dados. "+ex);
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
    
    public List<Produto> pegarProdutosPorEmpresa(int id) throws SQLException{
        
        String buscarProdutos     = "SELECT DISTINCT produto.*, imagem.*, "
                                      + "(SELECT COUNT(*) FROM comentario "
                                        + "INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                                        + "WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                                      + "(SELECT COUNT(*) FROM avaliacao "
                                        + "INNER JOIN relacao ON relacao.idrelacionada = avaliacao.idavaliacao AND relacao.tabela_relacionada = 'avaliacao' "
                                        + "WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdeavaliacoes "
                                    + "FROM produto "
                                    + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 "
                                    + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto' "
                                    + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem' "
                                    + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada "
                                    + "WHERE rp.identidade = ?;";
        
        try{
            List<Produto> produtos = new ArrayList<Produto>();
            resultSet = retornaResultadoQuery(buscarProdutos, id);
            while(resultSet.next()){
                Produto produto = new Produto();
                produto.setProdutoid(resultSet.getInt("idproduto"));
                produto.setNomeProduto(resultSet.getString("nomeproduto"));
                produto.setDescricao(resultSet.getString("descricao"));
                produto.setPreco(resultSet.getFloat("preco"));
                produto.setCategoria(resultSet.getInt("fkcategoria"));
                
                Imagem imagemPerfil = new Imagem();
                imagemPerfil.setImagemid(resultSet.getInt("idimagem"));
                imagemPerfil.setNome(resultSet.getString("nomeimagem"));
                imagemPerfil.setCaminho(resultSet.getString("caminho"));
                imagemPerfil.setDescricao(resultSet.getString("descricao"));
                imagemPerfil.setTipoImagem(resultSet.getInt("fktipo_imagem"));
                
                produto.setImagemPerfil(imagemPerfil);
                produto.setQtdeComentarios(resultSet.getInt("qtdecomentarios"));
                produto.setQtdeComentarios(resultSet.getInt("qtdeavaliacoes"));
                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
}
