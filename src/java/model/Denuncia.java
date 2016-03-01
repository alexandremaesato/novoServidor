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
public class Denuncia {
    
    private int denunciaid;
    private int pessoaid;
    private int denunciadoid;
    private int tipoDenuncia;
    private String descricao;

    public int getDenunciaid() {
        return denunciaid;
    }

    public void setDenunciaid(int denunciaid) {
        this.denunciaid = denunciaid;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getDenunciadoid() {
        return denunciadoid;
    }

    public void setDenunciadoid(int denunciadoid) {
        this.denunciadoid = denunciadoid;
    }

    public int getTipoDenuncia() {
        return tipoDenuncia;
    }

    public void setTipoDenuncia(int tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
