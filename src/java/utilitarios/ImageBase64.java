/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;

import static Resources.EmpresaResource.encodeImage;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.xml.wss.impl.misc.Base64;
import java.io.File;
import java.io.FileInputStream;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

/**
 *
 * @author Alexandre
 */
public class ImageBase64 {
    
    private String path;
    
    public ImageBase64(String path){
        this.path = path;
    }
    
     public static byte[] decodeImage(String imageDataString) throws Base64DecodingException, org.apache.xml.security.exceptions.Base64DecodingException {
        return Base64.decode(imageDataString);
    }
    
    public String getImageBase64(String nome){
        File file = null;
        try{
            
            if( path != null ){
//                String directoryPath = servletcontext.getRealPath("/WEB-INF/");
//                path = directoryPath+"/uploads/";
                file = new File(path+"\\"+nome);
                
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = decodeImage(imageDataString);
            // Write a image byte array into file system
            //FileOutputStream imageOutFile = new FileOutputStream(path+"/teste_1.jpg");
            //imageOutFile.write(imageByteArray);
            imageInFile.close();
            //imageOutFile.close();
            
            return imageDataString;
            }
            return null;
        }catch (Exception e){
            return null;
        }  
            
    }

}
