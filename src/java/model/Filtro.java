/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author DeusFelipe
 */

/*
    --Classe Filtro--
        #Classe que especificam o que há de ser filtrado ao realizar uma busca
*/
public class Filtro {
    String nomeempresa; //tabela empresa
    String nomeproduto; //tabela produto
    float precoMinimo; //tabela produto
    float precoMaximo; //tabela produto
    String nome_categoria; //tabela categoria
    String estado; //tabela endereco
    String cidade; //tabela endereco
    String bairro; //tabela endereco 
    boolean maisComentado; //tabela empresa A SER ADICIONADA COLUNA
    boolean maisBuscado; //tabela empresa A SER ADICIONADA COLUNA
    private String ordenacao;

    public String getNomeempresa() {
        return nomeempresa;
    }

    public void setNomeempresa(String nomeempresa) {
        this.nomeempresa = nomeempresa;
    }

    public String getNomeproduto() {
        return nomeproduto;
    }

    public void setNomeproduto(String nomeproduto) {
        this.nomeproduto = nomeproduto;
    }

    public float getPrecoMinimo() {
        return precoMinimo;
    }

    public void setPrecoMinimo(float precoMinimo) {
        this.precoMinimo = precoMinimo;
    }

    public float getPrecoMaximo() {
        return precoMaximo;
    }

    public void setPrecoMaximo(float precoMaximo) {
        this.precoMaximo = precoMaximo;
    }

    public String getNome_categoria() {
        return nome_categoria;
    }

    public void setNome_categoria(String nome_categoria) {
        this.nome_categoria = nome_categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public boolean isMaisComentado() {
        return maisComentado;
    }

    public void setMaisComentado(boolean maisComentado) {
        this.maisComentado = maisComentado;
    }

    public boolean isMaisBuscado() {
        return maisBuscado;
    }

    public void setMaisBuscado(boolean maisBuscado) {
        this.maisBuscado = maisBuscado;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }
}
