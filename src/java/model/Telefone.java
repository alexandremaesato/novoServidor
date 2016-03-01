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
public class Telefone {
    
    private int telefoneid;
    private String tipoTelefone;
    private String numero;

    public int getTelefoneid() {
        return telefoneid;
    }

    public void setTelefoneid(int telefoneid) {
        this.telefoneid = telefoneid;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
