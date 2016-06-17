/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Alexandre
 */
public class Favorito {
    private int idPessoa;
    private int idFavoritado;
    private String tipoFavoritado;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdFavoritado() {
        return idFavoritado;
    }

    public void setIdFavoritado(int idFavoritado) {
        this.idFavoritado = idFavoritado;
    }

    public String getTipoFavoritado() {
        return tipoFavoritado;
    }

    public void setTipoFavoritado(String tipoFavoritado) {
        this.tipoFavoritado = tipoFavoritado;
    }
    
}
