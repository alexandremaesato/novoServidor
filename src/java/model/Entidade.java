/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Alexandre
 */
public class Entidade {
    private int identidade;
    private int identidade_criada;
    private int deletado;
    private String tabela;
    private int idresponsavel;
    private Date data_criacao;
    private Date data_modificacao;
    private int idcriador;

    public int getIdentidade() {
        return identidade;
    }

    public void setIdentidade(int identidade) {
        this.identidade = identidade;
    }

    public int getIdentidade_criada() {
        return identidade_criada;
    }

    public void setIdentidade_criada(int identidade_criada) {
        this.identidade_criada = identidade_criada;
    }

    public int getDeletado() {
        return deletado;
    }

    public void setDeletado(int deletado) {
        this.deletado = deletado;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public int getIdresponsavel() {
        return idresponsavel;
    }

    public void setIdresponsavel(int idresponsavel) {
        this.idresponsavel = idresponsavel;
    }

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

    public int getIdcriador() {
        return idcriador;
    }

    public void setIdcriador(int idcriador) {
        this.idcriador = idcriador;
    }
        
}
