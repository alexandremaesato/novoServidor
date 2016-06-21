/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.charset.Charset;
import java.sql.Array;
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
public class ProdutoDAO {
    
    private Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet    = null;
    
    public Entidade cadastrarProduto(Produto prod, int pessoaid) throws SQLException{
        
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
            Entidade entidade = new Entidade();
            entidade.setIdentidade(resultSet.getInt(1));
            entidade.setIdentidade_criada(idProduto);
            
            ptmt = con.prepareStatement(cadastrarRelacao);
                ptmt.setInt(1, prod.getEmpresaid());
                ptmt.setString(2, "empresa");
                ptmt.setInt(3, idProduto);
                ptmt.setString(4, "produto");
            ptmt.executeUpdate();
            
            return entidade;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    private Entidade setGetEntidade(ResultSet resultSet)throws SQLException{
        Entidade entidade = new Entidade();
        entidade.setData_criacao(resultSet.getDate("data_criacao"));
        entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
        entidade.setDeletado(resultSet.getInt("deletado"));
        entidade.setIdcriador(resultSet.getInt("idcriador"));
        entidade.setIdentidade(resultSet.getInt("identidade"));
        entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
        entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
        entidade.setTabela(resultSet.getString("tabela"));
        return entidade;
    }

    public Produto getProdutoById(int idProduto) throws SQLException {
        String sql = "SELECT DISTINCT produto.*, imagem.*, "
                + "	(SELECT COUNT(*) FROM comentario "
                + "		INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                + "		WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado " 
                + "     AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes, "
                
                + "(SELECT avg(avaliacao) FROM avaliacao "
                + "WHERE produto.idproduto = avaliacao.idavaliado AND avaliacao.tipoavaliacao = 'produto') AS media "
                
                + "FROM produto "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 AND entidade.tabela = 'produto' "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto'  "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem'  "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada  "
                + "WHERE idproduto = ?;";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
                ptmt.setInt(1, idProduto);
                //ptmt.setInt(2, idProduto);
                
            resultSet = ptmt.executeQuery();
            if (resultSet.next()){
            Produto p = new Produto();
            p.setProdutoid(idProduto);
            p.setAvaliacaoGeral(resultSet.getInt("media"));

            p.setCategoria(resultSet.getInt("fkcategoria"));
            p.setPreco(resultSet.getDouble("preco"));
            
            Imagem imagem = new Imagem();
            imagem.setCaminho(resultSet.getString("caminho"));
            imagem.setDescricao(resultSet.getString("descricao"));
            imagem.setImagemid(resultSet.getInt("idimagem"));
            imagem.setItemid(idProduto);
            imagem.setNomeImagem(resultSet.getString("nomeimagem"));
            imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));
            
            p.setImagemPerfil(imagem);
            
            p.setNomeProduto(resultSet.getString("nomeproduto"));
            p.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));
            
            AvaliacaoDAO avaliacaoDao = new AvaliacaoDAO();
            //p.setAvaliacoes(avaliacaoDao.getAvaliacoesByIdProduto(idProduto));
            return p;
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Produto> getProdutosFavoritadosById(int idPessoa) throws SQLException {
        String sql = "SELECT DISTINCT produto.*, imagem.*,  "
                + "(SELECT COUNT(*) FROM comentario  "
                + "        INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario'  "
                + "        WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios,  "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado   "
                + "   AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes,  "
                + " "
                + "(SELECT avg(avaliacao) FROM avaliacao  "
                + "        WHERE produto.idproduto = avaliacao.idavaliado AND avaliacao.tipoavaliacao = 'produto') AS media  "
                + " "
                + "FROM produto  "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0   "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto'   "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem'   "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada   "
                + "WHERE idproduto IN ( SELECT idfavoritado FROM favoritos WHERE idpessoa = ?  and tipofavoritado = 'produto')";
        List<Produto> produtos = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, idPessoa);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()){
                Produto p = new Produto();
                p.setProdutoid(resultSet.getInt("idproduto"));
                p.setAvaliacaoGeral(resultSet.getInt("media"));

                p.setCategoria(resultSet.getInt("fkcategoria"));
                p.setPreco(resultSet.getDouble("preco"));

                Imagem imagem = new Imagem();
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setItemid(resultSet.getInt("idproduto"));
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));

                p.setImagemPerfil(imagem);

                p.setNomeProduto(resultSet.getString("nomeproduto"));
                p.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));

                produtos.add(p);
            }
            return produtos;
            
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Produto> getProdutoPorNome(String nome) throws SQLException {
        String sql = "SELECT DISTINCT produto.*, imagem.*, entidade.*, empresa.*, "
                + "	(SELECT COUNT(*) FROM comentario "
                + "		INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                + "		WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado " 
                + "     AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes, "
                
                + "(SELECT avg(avaliacao) FROM avaliacao "
                + "WHERE produto.idproduto = avaliacao.idavaliado AND avaliacao.tipoavaliacao = 'produto') AS media "
                
                + "FROM produto "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 AND entidade.tabela = 'produto' "
                + "INNER JOIN relacao re ON produto.idproduto = re.idrelacionada AND re.tabela_relacionada = 'produto' "
                + "INNER JOIN empresa ON empresa.idempresa = re.identidade "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto' "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem' "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada "
                + "WHERE lower(nomeproduto) like lower(?) group by produto.idproduto";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
                ptmt.setString(1, "%"+nome+"%");
                
            resultSet = ptmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while(resultSet.next()) {
                
                Produto p = new Produto();
                p.setProdutoid(resultSet.getInt("idproduto"));
                p.setAvaliacaoGeral(resultSet.getInt("media"));

                p.setCategoria(resultSet.getInt("fkcategoria"));
                p.setPreco(resultSet.getDouble("preco"));

                Imagem imagem = new Imagem();
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setItemid(p.getProdutoid());
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));

                Entidade entidade = new Entidade();
                entidade.setIdentidade(resultSet.getInt("identidade"));
                entidade.setTabela(resultSet.getString("tabela"));
                entidade.setDeletado(resultSet.getInt("deletado"));
                entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
                entidade.setData_criacao(resultSet.getDate("data_criacao"));
                entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
                entidade.setIdcriador(resultSet.getInt("idcriador"));
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                
                p.setEmpresa(empresa);
                p.setEntidade(entidade);
                p.setImagemPerfil(imagem);
                p.setNomeProduto(resultSet.getString("nomeproduto"));
                p.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));

//                AvaliacaoDAO avaliacaoDao = new AvaliacaoDAO();
                //p.setAvaliacoes(avaliacaoDao.getAvaliacoesByIdProduto(idProduto));
                produtos.add(p);
            }
            return produtos;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Produto> getProdutoPorCategoria(Integer categoria) throws SQLException {
        String sql = "SELECT DISTINCT produto.*, imagem.*, entidade.*, empresa.*, "
                + "	(SELECT COUNT(*) FROM comentario "
                + "		INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                + "		WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado " 
                + "     AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes, "
                
                + "(SELECT avg(avaliacao) FROM avaliacao "
                + "WHERE produto.idproduto = avaliacao.idavaliado AND avaliacao.tipoavaliacao = 'produto') AS media "
                
                + "FROM produto "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 AND entidade.tabela = 'produto' "
                + "INNER JOIN relacao re ON produto.idproduto = re.idrelacionada AND re.tabela_relacionada = 'produto'  "
                + "INNER JOIN empresa ON empresa.idempresa = re.identidade "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto'  "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem'  "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada  "
                + "WHERE fkcategoria = ? group by produto.idproduto";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
                ptmt.setInt(1, categoria);
                
            resultSet = ptmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while(resultSet.next()) {
                
                Produto p = new Produto();
                p.setProdutoid(resultSet.getInt("idproduto"));
                p.setAvaliacaoGeral(resultSet.getInt("media"));

                p.setCategoria(resultSet.getInt("fkcategoria"));
                p.setPreco(resultSet.getDouble("preco"));

                Imagem imagem = new Imagem();
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setItemid(p.getProdutoid());
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));

                Entidade entidade = new Entidade();
                entidade.setIdentidade(resultSet.getInt("identidade"));
                entidade.setTabela(resultSet.getString("tabela"));
                entidade.setDeletado(resultSet.getInt("deletado"));
                entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
                entidade.setData_criacao(resultSet.getDate("data_criacao"));
                entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
                entidade.setIdcriador(resultSet.getInt("idcriador"));
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                
                p.setEmpresa(empresa);
                p.setEntidade(entidade);
                p.setImagemPerfil(imagem);
                p.setNomeProduto(resultSet.getString("nomeproduto"));
                p.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));

//                AvaliacaoDAO avaliacaoDao = new AvaliacaoDAO();
                //p.setAvaliacoes(avaliacaoDao.getAvaliacoesByIdProduto(idProduto));
                produtos.add(p);
            }
            return produtos;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    
    public List<Produto> buscarMeusProdutos(Integer id) throws SQLException {
        String sql = "SELECT DISTINCT produto.*, imagem.*, entidade.*, empresa.*, "
                + "	(SELECT COUNT(*) FROM comentario "
                + "		INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                + "		WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado " 
                + "     AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes, "
                
                + "(SELECT avg(avaliacao) FROM avaliacao "
                + "WHERE produto.idproduto = avaliacao.idavaliado AND avaliacao.tipoavaliacao = 'produto') AS media "
                
                + "FROM produto "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 AND entidade.tabela = 'produto' "
                + "INNER JOIN relacao re ON produto.idproduto = re.idrelacionada AND re.tabela_relacionada = 'produto'  "
                + "INNER JOIN empresa ON empresa.idempresa = re.identidade "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto'  "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem'  "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada  "
                + "WHERE entidade.idresponsavel = ? group by produto.idproduto";

        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, id);
                
            resultSet = ptmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while(resultSet.next()) {
                
                Produto p = new Produto();
                p.setProdutoid(resultSet.getInt("idproduto"));
                p.setAvaliacaoGeral(resultSet.getInt("media"));

                p.setCategoria(resultSet.getInt("fkcategoria"));
                p.setPreco(resultSet.getDouble("preco"));

                Imagem imagem = new Imagem();
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setItemid(p.getProdutoid());
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));

                Entidade entidade = new Entidade();
                entidade.setIdentidade(resultSet.getInt("identidade"));
                entidade.setTabela(resultSet.getString("tabela"));
                entidade.setDeletado(resultSet.getInt("deletado"));
                entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
                entidade.setData_criacao(resultSet.getDate("data_criacao"));
                entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
                entidade.setIdcriador(resultSet.getInt("idcriador"));
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                
                p.setEmpresa(empresa);
                p.setEntidade(entidade);
                p.setImagemPerfil(imagem);
                p.setNomeProduto(resultSet.getString("nomeproduto"));
                p.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));

//                AvaliacaoDAO avaliacaoDao = new AvaliacaoDAO();
                //p.setAvaliacoes(avaliacaoDao.getAvaliacoesByIdProduto(idProduto));
                produtos.add(p);
            }
            return produtos;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public Produto buscarProdutoPorId(Integer id) throws Exception {
        
        String produto = "SELECT DISTINCT produto.*, imagem.*, empresa.*, entidade.* "
                            + "FROM produto "
                            + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0 AND entidade.tabela = 'produto' "
                            + "INNER JOIN relacao re ON produto.idproduto = re.idrelacionada AND re.tabela_relacionada = 'produto' "
                            + "INNER JOIN empresa ON empresa.idempresa = re.identidade "
                            + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto' "
                            + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem' "
                            + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada "
                            + "WHERE idproduto = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(produto);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();
            
            Produto p = null;
            if(resultSet.next()) {
                p = new Produto();
                p.setProdutoid(id);
                p.setNomeProduto(resultSet.getString("nomeproduto"));
                p.setProdutoid(resultSet.getInt("idproduto"));
                p.setCategoria(resultSet.getInt("fkcategoria"));
                p.setPreco(resultSet.getDouble("preco"));
                p.setDescricao(resultSet.getString("descricao"));

                Imagem imagem = new Imagem();
                imagem.setCaminho(resultSet.getString("caminho"));
                imagem.setDescricao(resultSet.getString("descricao"));
                imagem.setImagemid(resultSet.getInt("idimagem"));
                imagem.setItemid(p.getProdutoid());
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
                imagem.setTipoImagem(resultSet.getInt("fktipo_imagem"));

                Entidade entidade = new Entidade();
                entidade.setIdentidade(resultSet.getInt("identidade"));
                entidade.setTabela(resultSet.getString("tabela"));
                entidade.setDeletado(resultSet.getInt("deletado"));
                entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
                entidade.setData_criacao(resultSet.getDate("data_criacao"));
                entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
                entidade.setIdcriador(resultSet.getInt("idcriador"));
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                
                p.setEmpresa(empresa);
                p.setEntidade(entidade);
                p.setImagemPerfil(imagem);
                p.setAvaliacoes(new AvaliacaoDAO().pegarAvaliacoesPorIdProduto(id));
            }
            
            return p;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao recuperar o produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void editarProduto(Produto p) throws Exception {
        
        String produto = "update produto set nomeproduto = ?, fkcategoria = ?, preco = ?, descricao = ? where idproduto = ?";
        String entidade = "update entidade set data_modificacao = ? where identidade_criada = ? and tabela = 'produto'";
        String relacao = "update relacao set identidade = ? where idrelacionada = ? and tabela_relacionada = 'produto'";
        
        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            ptmt = con.prepareStatement(produto);
            ptmt.setString(1, p.getNomeProduto());
            ptmt.setInt(2, p.getCategoria());
            ptmt.setDouble(3, p.getPreco());
            ptmt.setString(4, p.getDescricao());
            ptmt.setInt(5, p.getProdutoid());
            ptmt.executeUpdate();
            
            ptmt = con.prepareStatement(entidade);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
            ptmt.setString(1, dateFormat.format(date));
            ptmt.setInt(2, p.getProdutoid());
            ptmt.executeUpdate();
            
            ptmt = con.prepareStatement(relacao);
            ptmt.setInt(1, p.getEmpresaid());
            ptmt.setInt(2, p.getProdutoid());
            ptmt.executeUpdate();
            
            con.commit();
            
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
        } finally {
            ptmt.close();
        }
    }
    
    public void excluirProduto(Integer id) throws Exception {
        
        String entidade = "update entidade set deletado = 1 where identidade_criada = ? and tabela = 'produto'";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(entidade);
            ptmt.setInt(1, id);
            ptmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
