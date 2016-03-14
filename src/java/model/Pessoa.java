/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Guilherme
 */
public class Pessoa {
    
    private int pessoaid;
    private String login;
    private String senha;
    private String cpf;
    private String nomePessoa;
    private String sobrenome;
    private Date dataNascimento;
    private int imagemPerfil;
    private int numeroFavoritados;
    private int numeroDesejados;
    private List listaFavoritos;
    private List listaDesejos;

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNome(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(int imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public int getNumeroFavoritados() {
        return numeroFavoritados;
    }

    public void setNumeroFavoritados(int numeroFavoritados) {
        this.numeroFavoritados = numeroFavoritados;
    }

    public int getNumeroDesejados() {
        return numeroDesejados;
    }

    public void setNumeroDesejados(int numeroDesejados) {
        this.numeroDesejados = numeroDesejados;
    }

    public List getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(List listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    public List getListaDesejos() {
        return listaDesejos;
    }

    public void setListaDesejos(List listaDesejos) {
        this.listaDesejos = listaDesejos;
    }
}
