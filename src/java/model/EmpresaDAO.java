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
    
    public int cadastrarEmpresa(Empresa emp, int pessoaid) throws SQLException{
        
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
            
            
            ptmt = con.prepareStatement(cadastrarEntidade, Statement.RETURN_GENERATED_KEYS);
                ptmt.setInt(1, idEmpresa);
                ptmt.setInt(2, 0);
                ptmt.setString(3, "empresa");
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
            
            
            Endereco endereco = emp.getEndereco();
            ptmt = con.prepareStatement(cadastrarEndereco, Statement.RETURN_GENERATED_KEYS);
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
            ptmt.executeUpdate();
            
            List<Telefone> telefones = emp.getTelefones();
            for( Telefone telefone : telefones ){
                ptmt = con.prepareStatement(cadastrarTelefone, Statement.RETURN_GENERATED_KEYS);
                    ptmt.setString(1, telefone.getNumero());
                    ptmt.setString(2, telefone.getTipoTelefone());
                ptmt.executeUpdate();
                resultSet = ptmt.getGeneratedKeys();
                resultSet.next();
                int idTelefone = resultSet.getInt(1);

                ptmt = con.prepareStatement(cadastrarRelacao);
                    ptmt.setInt(1, idEmpresa);
                    ptmt.setString(2, "empresa");
                    ptmt.setInt(3, idTelefone);
                    ptmt.setString(4, "telefone");
                ptmt.executeUpdate();
            }
            
            return idEntidade;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao inserir empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void atualizarEmpresa(Empresa emp) throws SQLException{
        
        String alteraEmpresa = "UPDATE empresa SET nomeempresa = ?, cnpj = ?, descricao = ? WHERE idempresa = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(alteraEmpresa);
                ptmt.setString(1, emp.getNomeEmpresa());
                ptmt.setString(2, emp.getCnpj());
                ptmt.setString(3, emp.getDescricao());
                ptmt.setInt(4, emp.getEmpresaId());
            
            ptmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void apagarEmpresa(Empresa emp) throws SQLException{
        
        String alteraEmpresa = "UPDATE entidade SET deletado = 1 WHERE identidade_criada = ?";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(alteraEmpresa);
                ptmt.setInt(1, emp.getEmpresaId());
            
            ptmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao apagar empresa do banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Empresa> pegarEmpresas() throws SQLException{
        
        String buscaEmpresa = "SELECT DISTINCT "
                               + "empresa.*, imagem.*, endereco.*, "
                               + "(SELECT COUNT(*) FROM comentario "
                                  + "INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                                  + "WHERE relacao.identidade = empresa.idempresa AND relacao.tabela_entidade = 'empresa') AS qtdecomentarios, "
                               +"(SELECT COUNT(*) FROM avaliacao "
                               +" WHERE avaliacao.idavaliado = empresa.idempresa) AS qtdeavaliacoes, "
                               + "(SELECT AVG(avaliacao) FROM avaliacao "
                                  + "WHERE avaliacao.idavaliado = empresa.idempresa) AS avaliacaogeral "
                
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
                imagemPerfil.setNomeImagem(resultSet.getString("nomeimagem"));
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
                empresa.setAvaliacaoNota(Math.round(resultSet.getInt("avaliacaogeral")));
                
                
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

        String buscarEmpresa = "SELECT * FROM empresa "
                + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                + "WHERE idempresa = ?;";

        String buscarImagemPerfil = "SELECT DISTINCT imagem.* FROM imagem "
                + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                + "WHERE imagem.fktipo_imagem = 1 AND relacao.identidade = ?;";

        String buscarImagens = "SELECT DISTINCT imagem.* FROM imagem "
                + "INNER JOIN relacao ON imagem.idimagem = relacao.idrelacionada AND relacao.tabela_relacionada = 'imagem' "
                + "WHERE imagem.fktipo_imagem IN (2,3) AND relacao.identidade = ?;";

        String buscarComentarios = "SELECT DISTINCT comentario.*, pessoa.* FROM comentario "
                + "INNER JOIN relacao ON comentario.idcomentario = relacao.idrelacionada AND relacao.tabela_relacionada = 'comentario' "
                + "INNER JOIN pessoa ON comentario.fkpessoa = pessoa.idpessoa "
                + "WHERE relacao.identidade = ?;";

        String buscarTelefones = "SELECT DISTINCT telefone.* FROM telefone "
                + "INNER JOIN relacao ON telefone.idtelefone = relacao.idrelacionada AND relacao.tabela_relacionada = 'telefone' "
                + "WHERE relacao.identidade = ?;";

        String buscarAvaliacao = "SELECT DISTINCT avaliacao.*, pessoa.* FROM avaliacao "
                + "INNER JOIN relacao ON avaliacao.idavaliacao = relacao.idrelacionada AND relacao.tabela_relacionada = 'avaliacao' "
                + "INNER JOIN pessoa ON avaliacao.idpessoa = pessoa.idpessoa "
                + "WHERE relacao.identidade = ?;";

        String buscarEndereco = "SELECT DISTINCT endereco.* FROM endereco "
                + "INNER JOIN relacao ON endereco.idendereco = relacao.idrelacionada AND relacao.tabela_relacionada = 'endereco' "
                + "WHERE relacao.identidade = ?;";

        String qtdAvaliacao = "SELECT count(*) as qtd FROM avaliacao where idavaliado = ? and tipoavaliacao = 'empresa'";
        
        String qtdComentario = "SELECT count(*) as qtd FROM comentario " 
                + "inner join relacao on identidade = ? and tabela_entidade = 'empresa' "
                + "and tabela_relacionada = 'comentario' "
                + "and idrelacionada = idcomentario";

        String avaliacaoGeralProduto = "SELECT *, avg(avaliacao) as resultado FROM relacao "
                + "inner join avaliacao aval on aval.idavaliado = idrelacionada and tabela_relacionada = aval.tipoavaliacao "
                + "where tabela_entidade ='empresa' and identidade = ? "
                + "group by descricao";
        
        String buscarEntidade = "SELECT * FROM empresa "
                + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                + "where idempresa = ? "
                + "GROUP BY empresa.idempresa ";
        
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
                imagemPerfil.setNomeImagem(resultSet.getString("nomeimagem"));
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
                imagem.setNomeImagem(resultSet.getString("nomeimagem"));
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
                comentario.setComentarioid(resultSet.getInt("idcomentario"));
                comentario.setComentadoid(resultSet.getInt("fkidcomentario_dependente"));
                comentario.setDescricao(resultSet.getString("descricao"));
                comentario.setComentarioDependenteid(resultSet.getInt("fkidcomentario_dependente"));
                comentario.setPessoaid(resultSet.getInt("fkpessoa"));
                comentario.setModificado(resultSet.getInt("modificado"));
                comentario.setData_modificacao(resultSet.getDate("data_modificacao"));
                    Pessoa pcoment = new Pessoa();
                    pcoment.setPessoaid(resultSet.getInt("idpessoa"));
                    pcoment.setNome(resultSet.getString("nome"));
                    pcoment.setSobrenome(resultSet.getString("sobrenome"));
                comentario.setPessoa(pcoment);
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
                avaliacao.setData_modificacao(resultSet.getDate("data_modificacao"));
                    Pessoa paval = new Pessoa();
                    paval.setPessoaid(resultSet.getInt("idpessoa"));
                    paval.setNome(resultSet.getString("nome"));
                    paval.setSobrenome(resultSet.getString("sobrenome"));
                avaliacao.setPessoa(paval);
                avaliacoes.add(avaliacao);
            }
            empresa.setAvaliacoes(avaliacoes);
            
            resultSet = retornaResultadoQuery(buscarEndereco, id);
            Endereco endereco = new Endereco();
            if(resultSet.next()){
                endereco.setBairro(resultSet.getString("bairro"));
                endereco.setCep(resultSet.getString("cep"));
                endereco.setCidade(resultSet.getString("cidade"));
                endereco.setEnderecoid(resultSet.getInt("idendereco"));
                endereco.setComplemento(resultSet.getString("complemento"));
                endereco.setEstado(resultSet.getString("estado"));
                endereco.setNumero(resultSet.getString("numero"));
                endereco.setPais(resultSet.getString("pais"));
                endereco.setRua(resultSet.getString("rua"));
            }
            empresa.setEndereco(endereco);
            
            resultSet = retornaResultadoQuery(qtdAvaliacao, id);
            if(resultSet.next()){
                empresa.setQtdeAvaliacoes(resultSet.getInt("qtd"));
            }
            
            resultSet = retornaResultadoQuery(qtdComentario, id);
            if(resultSet.next()){
                empresa.setQtdeComentarios(resultSet.getInt("qtd"));
            }
            
            resultSet = retornaResultadoQuery(buscarEntidade, id);
            if(resultSet.next()){
                Entidade entidade = new Entidade();
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                empresa.setEntidade(entidade);
            }
         
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
        
        String buscarProdutos = "SELECT DISTINCT produto.*, imagem.*, "
                + "	(SELECT COUNT(*) FROM comentario "
                + "		INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                + "		WHERE relacao.identidade = produto.idproduto AND relacao.tabela_entidade = 'produto') AS qtdecomentarios, "
                + "(SELECT COUNT(*) FROM avaliacao WHERE produto.idproduto = avaliacao.idavaliado " 
                + "AND avaliacao.tipoavaliacao = 'produto') AS qtdeavaliacoes, "
                + "    (SELECT avg(avaliacao) FROM relacao "
                + "        inner join avaliacao aval on aval.idavaliado = idrelacionada and tabela_relacionada = aval.tipoavaliacao "
                + "        where tabela_entidade ='empresa' and identidade = ?  and idrelacionada = produto.idproduto) as media "
                + "FROM produto "
                + "INNER JOIN entidade ON produto.idproduto = entidade.identidade_criada AND entidade.deletado = 0  "
                + "LEFT JOIN relacao rp ON produto.idproduto = rp.idrelacionada AND rp.tabela_relacionada = 'produto'  "
                + "LEFT JOIN relacao ri ON ri.identidade = produto.idproduto AND ri.tabela_relacionada = 'imagem'  "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada  "
                + "WHERE rp.identidade = ?;";
        
        try{
            List<Produto> produtos = new ArrayList<Produto>();
            //resultSet = retornaResultadoQuery(buscarProdutos, id);
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(buscarProdutos);
            ptmt.setInt(1, id);
            ptmt.setInt(2, id);
            resultSet = ptmt.executeQuery();
            while(resultSet.next()){
                Produto produto = new Produto();
                produto.setProdutoid(resultSet.getInt("idproduto"));
                produto.setNomeProduto(resultSet.getString("nomeproduto"));
                produto.setDescricao(resultSet.getString("descricao"));
                produto.setPreco(resultSet.getDouble("preco"));
                produto.setCategoria(resultSet.getInt("fkcategoria"));
                produto.setCulinaria(resultSet.getInt("fktipo_culinaria"));
                
                Imagem imagemPerfil = new Imagem();
                imagemPerfil.setImagemid(resultSet.getInt("idimagem"));
                imagemPerfil.setNomeImagem(resultSet.getString("nomeimagem"));
                imagemPerfil.setCaminho(resultSet.getString("caminho"));
                imagemPerfil.setDescricao(resultSet.getString("descricao"));
                imagemPerfil.setTipoImagem(resultSet.getInt("fktipo_imagem"));
                
                produto.setImagemPerfil(imagemPerfil);
                produto.setQtdeComentarios(resultSet.getInt("qtdecomentarios"));
                produto.setQtdeAvaliacoes(resultSet.getInt("qtdeavaliacoes"));
                produto.setAvaliacaoGeral(resultSet.getInt("media"));
                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar produto no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Empresa> carregarEmpresas(int nota) throws SQLException{
//        String sqlCarregaEmpresas = "SELECT DISTINCT *, "
//                                        + "FORMAT(((SELECT CASE WHEN COUNT(avaliacao) = 0 THEN COALESCE(SUM(avaliacao), 0) ELSE COALESCE(SUM(avaliacao), 0)/COUNT(avaliacao) END FROM avaliacao "
//                                        +		"INNER JOIN relacao ON relacao.idrelacionada = avaliacao.idavaliado AND relacao.tabela_relacionada = 'produto' "
//                                        +		"WHERE avaliacao.tipoavaliacao = 'prato' AND relacao.identidade = entidade.identidade "
//                                        +		"AND relacao.tabela_relacionada = 'empresa') + "
//                                        +	  "(SELECT CASE WHEN COUNT(avaliacao) = 0 THEN COALESCE(SUM(avaliacao), 0) ELSE COALESCE(SUM(avaliacao), 0)/COUNT(avaliacao) END FROM avaliacao "
//                                        +		"WHERE tipoavaliacao = 'servico' AND idavaliado = entidade.identidade) + "
//                                        +	  "(SELECT CASE WHEN COUNT(avaliacao) = 0 THEN COALESCE(SUM(avaliacao), 0) ELSE COALESCE(SUM(avaliacao), 0)/COUNT(avaliacao) END FROM avaliacao "
//                                        +		"WHERE tipoavaliacao = 'ambiente' AND idavaliado = entidade.identidade)) / 3, 0) AS totalGeral "
//                                        + "FROM empresa "
//                                        + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.tabela = 'empresa' AND entidade.deletado = 0 "
//                                        + "LEFT JOIN relacao re ON re.identidade = entidade.identidade AND re.tabela_entidade = 'empresa' AND re.tabela_relacionada = 'endereco' "
//                                        + "LEFT JOIN endereco ON endereco.idendereco = re.idrelacionada "
//                                        + "LEFT JOIN relacao ri ON ri.identidade = entidade.identidade AND ri.tabela_entidade = 'empresa' AND ri.tabela_relacionada = 'imagem' "
//                                        + "LEFT JOIN entidade ei ON ei.identidade = ri.idrelacionada AND ei.deletado = 0 AND ei.tabela = 'imagem' "
//                                        + "LEFT JOIN imagem ON imagem.idimagem = ei.identidade_criada "
//                                        + "GROUP BY totalGeral DESC";
        String sqlCarregaEmpresas = "SELECT DISTINCT  "
                + "empresa.*, imagem.*, endereco.*,  "
                + "(SELECT COUNT(*) FROM comentario  "
                + " INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario'  "
                + " WHERE relacao.identidade = empresa.idempresa AND relacao.tabela_entidade = 'empresa') AS qtdecomentarios,  "
                + "(SELECT COUNT(*) FROM avaliacao  "
                + "WHERE avaliacao.idavaliado = empresa.idempresa) AS qtdeavaliacoes,  "
                + "(SELECT AVG(avaliacao) FROM avaliacao "
                + " WHERE avaliacao.idavaliado = empresa.idempresa) AS avaliacaogeral "
                + "FROM empresa "
                + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                + "LEFT JOIN relacao ri ON ri.identidade = empresa.idempresa AND ri.tabela_relacionada = 'imagem' "
                + "LEFT JOIN relacao ren ON ren.identidade = empresa.idempresa AND ren.tabela_relacionada = 'endereco' "
                + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada AND imagem.fktipo_imagem = 1 "
                + "LEFT JOIN endereco  ON endereco.idendereco = ren.idrelacionada "
                + "GROUP BY empresa.idempresa "
                + "order by avaliacaogeral DESC "
                + "limit 10 "
                + "OFFSET ?";
        
//        if(nota >= 0){
//            sqlCarregaEmpresas += " HAVING totalGeral < " + nota;
//        }
//        
//        sqlCarregaEmpresas += " LIMIT 2";
        
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(sqlCarregaEmpresas);
            ptmt.setInt(1, nota);
            resultSet = ptmt.executeQuery();
            List<Empresa> empresas = new ArrayList<Empresa>();
            while(resultSet.next()){
                Empresa empresa = new Empresa();
                empresa.setEmpresaId(resultSet.getInt("idempresa"));
                empresa.setNomeEmpresa(resultSet.getString("nomeempresa"));
                empresa.setCnpj(resultSet.getString("cnpj"));
                empresa.setDescricao(resultSet.getString("descricao"));
                
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
                empresa.setEndereco(endereco);
                
                Imagem img = new Imagem();
                img.setNomeImagem(resultSet.getString("nomeimagem"));
                img.setDescricao(resultSet.getString("descricao"));
                img.setImagemid(resultSet.getInt("idimagem"));
                img.setTipoImagem(resultSet.getInt("fktipo_imagem"));
                img.setCaminho(resultSet.getString("caminho"));
                empresa.setImagemPerfil(img);
                
                empresa.setAvaliacaoNota(resultSet.getInt("avaliacaogeral"));
                empresas.add(empresa);
            }
            
            return empresas;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao carregar empresas do banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public void atualizaAvaliacaoGeralById(int id){    
    }
    
    public void setSouDono(int idresponsavel, int identidade_criada) throws SQLException{
        String cadastrarEntidade = "UPDATE entidade SET idresponsavel=?, data_modificacao=?"
                + "where tabela='empresa' and identidade_criada = ?;";
        
        try {
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(cadastrarEntidade);
                ptmt.setInt(1, idresponsavel);                
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                ptmt.setString(2, dateFormat.format(date));
                ptmt.setInt(3, identidade_criada);
                
            
            ptmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar entidade no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
    
    public List<Empresa> buscarMinhasEmpresas(Integer id) throws SQLException{
        
        String buscaEmpresa = "SELECT DISTINCT "
                               + "empresa.*, imagem.*, endereco.*, entidade.*, "
                               + "(SELECT COUNT(*) FROM comentario "
                                  + "INNER JOIN relacao ON relacao.idrelacionada = comentario.idcomentario AND relacao.tabela_relacionada = 'comentario' "
                                  + "WHERE relacao.identidade = empresa.idempresa AND relacao.tabela_entidade = 'empresa') AS qtdecomentarios, "
                               +"(SELECT COUNT(*) FROM avaliacao "
                               +" WHERE avaliacao.idavaliado = empresa.idempresa) AS qtdeavaliacoes, "
                               + "(SELECT AVG(avaliacao) FROM avaliacao "
                                  + "WHERE avaliacao.idavaliado = empresa.idempresa) AS avaliacaogeral "
                
                               + "FROM empresa "
                               + "INNER JOIN entidade ON empresa.idempresa = entidade.identidade_criada AND entidade.deletado = 0 "
                               + "LEFT JOIN relacao ri ON ri.identidade = empresa.idempresa AND ri.tabela_relacionada = 'imagem' "
                               + "LEFT JOIN relacao ren ON ren.identidade = empresa.idempresa AND ren.tabela_relacionada = 'endereco' "
                               + "LEFT JOIN imagem  ON imagem.idimagem = ri.idrelacionada AND imagem.fktipo_imagem = 1 "
                               + "LEFT JOIN endereco  ON endereco.idendereco = ren.idrelacionada "
                               + "WHERE entidade.idresponsavel = ? "
                               + "GROUP BY empresa.idempresa";
        
        try{
            con = ConnectionFactory.getConnection();
            ptmt = con.prepareStatement(buscaEmpresa);
            ptmt.setInt(1, id);
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
                imagemPerfil.setNomeImagem(resultSet.getString("nomeimagem"));
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
                empresa.setAvaliacaoNota(Math.round(resultSet.getInt("avaliacaogeral")));
                
                Entidade entidade = new Entidade();
                entidade.setIdentidade(resultSet.getInt("identidade"));
                entidade.setIdentidade_criada(resultSet.getInt("identidade_criada"));
                entidade.setDeletado(resultSet.getInt("deletado"));
                entidade.setTabela(resultSet.getString("tabela"));
                entidade.setIdresponsavel(resultSet.getInt("idresponsavel"));
                entidade.setIdcriador(resultSet.getInt("idcriador"));
                entidade.setData_criacao(resultSet.getDate("data_criacao"));
                entidade.setData_modificacao(resultSet.getDate("data_modificacao"));
                empresa.setEntidade(entidade);
                
                empresas.add(empresa);
            }
            return empresas;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao busca empresa no banco de dados. "+ex);
        } finally {
            ptmt.close();
        }
    }
}
