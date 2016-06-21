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
public class Comentario {
    
    private int comentarioid;
    private String descricao;
    private int pessoaid;
    private int comentarioDependenteid;
    private int comentadoid;
    private int modificado;
    private String tipoComentado;
    private Date data_criacao;
    private Date data_modificacao;
    private Pessoa pessoa;
    private int cont_comentarios_dependentes;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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
    

    public int getComentarioid() {
        return comentarioid;
    }

    public void setComentarioid(int comentarioid) {
        this.comentarioid = comentarioid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getComentarioDependenteid() {
        return comentarioDependenteid;
    }

    public void setComentarioDependenteid(int comentarioDependenteid) {
        this.comentarioDependenteid = comentarioDependenteid;
    }

    public int getComentadoid() {
        return comentadoid;
    }

    public void setComentadoid(int comentadoid) {
        this.comentadoid = comentadoid;
    }

    public int getModificado() {
        return modificado;
    }

    public void setModificado(int modificado) {
        this.modificado = modificado;
    }

    public String getTipoComentado() {
        return tipoComentado;
    }

    public void setTipoComentado(String tipoComentado) {
        this.tipoComentado = tipoComentado;
    }
    
    public int getCont_comentarios_dependentes() {
        return cont_comentarios_dependentes;
    }

    public void setCont_comentarios_dependentes(int cont_comentarios_dependentes) {
        this.cont_comentarios_dependentes = cont_comentarios_dependentes;
    }
}
