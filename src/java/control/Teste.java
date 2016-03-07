/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Empresa;
import model.EmpresaDAO;

/**
 *
 * @author Guilherme
 */
public class Teste {
    public static void main(String args[]) throws SQLException{
        
        
        EmpresaDAO empdao = new EmpresaDAO();
        
        List<Empresa> empresas = empdao.pegarEmpresas();
//        Empresa emp = empdao.pegarEmpresaPorId(12);
        
        for( Empresa emp : empresas ){
            
            System.out.print(emp.getNomeEmpresa());
        }
        
        
    }
}
