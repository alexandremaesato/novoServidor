/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Guilherme
 */
public class Avaliacao {
    
    private int avaliacaoid;
    private int avaliadoid;
    private int pessoaid;
    private int nota;
    private String descricao;
    private String tipoAvalicao;
    private Date data_criacao;
    private Date data_modificacao;

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Date getData_modificacao() {
        return data_modificacao;
    }

    public void setData_modificacao(Date data_modificacao) {
        this.data_modificacao = data_modificacao;
    }

    public int getAvaliacaoid() {
        return avaliacaoid;
    }

    public void setAvaliacaoid(int avaliacaoid) {
        this.avaliacaoid = avaliacaoid;
    }

    public int getAvaliadoid() {
        return avaliadoid;
    }

    public void setAvaliadoid(int avaliadoid) {
        this.avaliadoid = avaliadoid;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoAvalicao() {
        return tipoAvalicao;
    }

    public void setTipoAvalicao(String tipoAvalicao) {
        this.tipoAvalicao = tipoAvalicao;
    }
    
    public boolean hasAvaliacao(){
        if(this.avaliacaoid == 0 &&
           this.avaliadoid == 0 &&
           this.nota == 0 &&
           this.pessoaid == 0){
           return true;
        }
           return false;
    }
}
