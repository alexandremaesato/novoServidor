/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Guilherme
 */
public class Produto {
    
    private int produtoid;
    private int categoria;
    private String descricao;
    private Double preco;
    private Imagem imagemPerfil;
    private List<Imagem> imagensNaoOficiais;
    private List<Imagem> imagensOficiais;
    private List<Comentario> comentarios;
    private List<Avaliacao> avaliacoes;

    public int getProdutoid() {
        return produtoid;
    }

    public void setProdutoid(int produtoid) {
        this.produtoid = produtoid;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Imagem getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(Imagem imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public List<Imagem> getImagensNaoOficiais() {
        return imagensNaoOficiais;
    }

    public void setImagensNaoOficiais(List<Imagem> imagensNaoOficiais) {
        this.imagensNaoOficiais = imagensNaoOficiais;
    }

    public List<Imagem> getImagensOficiais() {
        return imagensOficiais;
    }

    public void setImagensOficiais(List<Imagem> imagensOficiais) {
        this.imagensOficiais = imagensOficiais;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
