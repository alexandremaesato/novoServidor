/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import utilitarios.ImageResize;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.xml.wss.impl.misc.Base64;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.xml.bind.DatatypeConverter;

import jdk.nashorn.internal.parser.JSONParser;
import DAO.AutenticacaoDao;
import DAO.ConnectionFactory;
import model.Empresa;
import model.Pessoa;
//import org.apache.xml.security.exceptions.Base64DecodingException;
import org.omg.DynamicAny.NameValuePair;


import utilitarios.Criptografia;



/**
 *
 * @author Alexandre
 */
public class TestesDoAle {
    public static void main(String args[]) throws SQLException, UnsupportedEncodingException{
//        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary("dXNlcjpwYXNzd29yZA==");
//            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;
//    
//            //String decodeString = Base64.getDecoder().decode(authToken).toString();
//            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
//            String username = tokenizer.nextToken();
//            String password = tokenizer.nextToken();
//            
//            String convertido = Base64.encode("qwe123@".getBytes());
//            
////     System.out.println("Convertido: "+convertido);
////     System.out.println("decode : "+decodeString);
//
//     
////      Image image = null;
////      File imagem = new File("C:\\Users\\Alexandre\\Documents\\imagem.jpg");
//      
////      ImageResize ir = new ImageResize("imagem.jpg");
////      ir.resize();
//        
////        ConnectionFactory c = (ConnectionFactory) ConnectionFactory.getConnection();
////        c.toString();
//
//        
//        
//        String teste = "teste=true&avaliacao=%7B%22avaliacaoid%22%3A0%2C%22avaliadoid%22%3A2%2C%22nota%22%3A0%2C%22pessoaid%22%3A0%2C%22tipoAvalicao%22%3A%22empresa%22%7D&";
//        
//        String value = URLDecoder.decode(teste, "UTF-8");
//        
//        System.out.println("Valor : "+value);
        
//    String oldStr = "empresa=%7B%22cnpj%22%3A%22%22%2C%22descricao%22%3A%22a%22%2C%22empresaId%22%3A0%2C%22endereco%22%3A%7B%22bairro%22%3A%22a%22%2C%22cep%22%3A%22%22%2C%22cidade%22%3A%22a%22%2C%22complemento%22%3A%22%22%2C%22enderecoid%22%3A0%2C%22estado%22%3A%22a%22%2C%22numero%22%3A%22444%22%2C%22pais%22%3A%22a%22%2C%22rua%22%3A%22a%22%7D%2C%22imagemPerfil%22%3A%7B%22imagemid%22%3A0%2C%22itemid%22%3A0%2C%22pessoaid%22%3A0%2C%22tipoImagem%22%3A1%7D%2C%22nomeEmpresa%22%3A%22a%22%2C%22qtdeAvaliacoes%22%3A0%2C%22qtdeComentarios%22%3A0%2C%22telefones%22%3A%5B%7B%22numero%22%3A%22%22%2C%22telefoneid%22%3A0%2C%22tipoTelefone%22%3A%22%22%7D%5D%7D&";
//    String newStr;
//    newStr = URLDecoder.decode(URLEncoder.encode(oldStr, "Windows-1252"),"UTF-8");
//    //newStr = URLDecoder.decode(URLEncoder.encode(oldStr, "UTF-8"),"iso8859-1");
//    System.out.println(newStr);

    List<String> num = new ArrayList<>();
    num.add("1");
    num.add("1");
    num.add("1");
    num.add("1");
    
    System.out.println(num);
    }
    
    
   
    
}