/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import utilitarios.ImageBase64;

/**
 *
 * @author Guilherme
 */
public class Empresa {
    
    private int empresaId;
    private String nomeEmpresa;
    private String cnpj;
    private String descricao;
    private Imagem imagemPerfil;
    private List<Imagem> imagensNaoOficiais;
    private List<Imagem> imagensOficiais;
    private List<Comentario> comentarios;
    private List<Avaliacao> avaliacoes;
    private List<Telefone> telefones;
    private Endereco endereco;
    private List<Produto> produtos;
    private int avaliacaoNota;
    private int qtdeComentarios;
    private int qtdeAvaliacoes;
    private Entidade entidade;

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nome) {
        this.nomeEmpresa = nome;
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

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public int getAvaliacaoNota() {
        return avaliacaoNota;
    }

    public void setAvaliacaoNota(int avaliacaoNota) {
        this.avaliacaoNota = avaliacaoNota;
    }

    public int getQtdeComentarios() {
        return qtdeComentarios;
    }

    public void setQtdeComentarios(int qtdeComentarios) {
        this.qtdeComentarios = qtdeComentarios;
    }

    public int getQtdeAvaliacoes() {
        return qtdeAvaliacoes;
    }

    public void setQtdeAvaliacoes(int qtdeAvaliacoes) {
        this.qtdeAvaliacoes = qtdeAvaliacoes;
    }
    
    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }
    
    public void mountImages(String path){
        ImageBase64 imageBase64 = new ImageBase64(path);
        for( Imagem imagem : this.imagensNaoOficiais ){
            imagem.setImg(imageBase64.getImageBase64(imagem.getNomeImagem()));
        }
        for( Imagem imagem : this.imagensOficiais ){
            String i = imageBase64.getImageBase64(imagem.getNomeImagem());
            imagem.setImg(i);
        }
        if(this.imagemPerfil != null){
            this.imagemPerfil.setImg(imageBase64.getImageBase64(this.imagemPerfil.getNomeImagem())); 
        }
       
     
    }
    
}
