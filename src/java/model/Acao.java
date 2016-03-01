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
public class Acao {
    
    private int acaoid;
    private int pessoaid;
    private int acionadoid;
    private int tipoAcao;

    public int getAcaoid() {
        return acaoid;
    }

    public void setAcaoid(int acaoid) {
        this.acaoid = acaoid;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getAcionadoid() {
        return acionadoid;
    }

    public void setAcionadoid(int acionadoid) {
        this.acionadoid = acionadoid;
    }

    public int getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(int tipoAcao) {
        this.tipoAcao = tipoAcao;
    }
}
