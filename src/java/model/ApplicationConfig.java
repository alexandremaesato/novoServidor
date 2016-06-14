/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Guilherme
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Resources.AutenticacaoResource.class);
        resources.add(Resources.AvaliacaoResource.class);
        resources.add(Resources.ComentarioResource.class);
        resources.add(Resources.EmpresaResource.class);
        resources.add(Resources.EnderecoResource.class);
        resources.add(Resources.PessoaResource.class);
        resources.add(Resources.ProdutoResource.class);
        resources.add(Resources.SecurityFilter.class);
        resources.add(Resources.SegurancaResource.class);
    }
    
}
