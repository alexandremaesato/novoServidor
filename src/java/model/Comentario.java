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
public class Comentario {
    
    private int comentarioid;
    private String descricao;
    private int pessoaid;
    private int comentarioDependenteid;
    private int comentadoid;

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
}
