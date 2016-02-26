/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Guilherme
 */
public class Empresa {
    
    private int empresaId;
    private String nome;
    private String cnpj;
    private String descricao;
    //private int imagemPerfil;
    //private int imagensNaoOficiais; //List<Imagem>
    //private int imagensOficiais; //List<Imagem>
    //private int comentarios; //List<Comentario>
    //private int avaliacoes; //List<Avaliacao>
    //private int telefones; //List<Telefone>
    //private String endereco;
    //private int produtos; //List<Produto>
    //private int avaliacaoNota; //List

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
