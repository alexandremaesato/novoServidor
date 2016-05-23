package utilitarios;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * @author mkyong
 *
 */
public class ImageResize {

	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;
        
        private static final int[] HDPI = {600,300}; // {Largura,Altura}
        private static final int[] XHDPI = {1600,1300}; // {Largura,Altura}
        private static final String PATH = "C:\\Users\\Alexandre\\Documents\\temp\\"; 
        String imageName;
       
	
	public ImageResize(String name){
            this.imageName = name;
        }
        
        public void resize() {
            
	try{
			
		BufferedImage originalImage = ImageIO.read(new File(PATH+imageName));
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			
		//Aqui sao exemplos de como devem ficar as saidas
                //JPG ou PNG ?
                //HPDI, XHDPI... sao as proporcoes que queremos guardar conforme o tamanho do celular
                //Lembrar que ainda temos que colocar todos os tamanhos corretamente
                
                BufferedImage resizeImageJpg = resizeImage(originalImage, type, HDPI);
		ImageIO.write(resizeImageJpg, "jpg", new File(PATH+imageName+"_HDPI.jpg")); 
                
                resizeImageJpg = resizeImage(originalImage, type, XHDPI);
		ImageIO.write(resizeImageJpg, "jpg", new File(PATH+imageName+"_XHDPI.jpg")); 

		BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type, XHDPI);
		ImageIO.write(resizeImageHintPng, "png", new File(PATH+imageName+"_XHDPI_hint.png")); 
				
	}catch(IOException e){
		System.out.println(e.getMessage());
	}
		
    }
	
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int[] format){
	BufferedImage resizedImage = new BufferedImage(format[0], format[1], type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, format[0], format[1], null);
	g.dispose();
		
	return resizedImage;
    }
	
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int[] format){
		
	BufferedImage resizedImage = new BufferedImage(format[0], format[1], type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, format[0], format[1], null);
	g.dispose();	
	g.setComposite(AlphaComposite.Src);

	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
	
	return resizedImage;
    }	
}