/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgImageViewer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.lang.String;
import java.awt.Graphics2D;

/**
 *
 * @author sydney
 */

//============================================================================
/** This class implements the panel on which all image thumbnail are drawn */
//===========================================================================

 

 public class ImageThumbnail {

    public ImageThumbnail() {
    }

    public void run(String folder) {
        File dir = new File(folder);
        for (File file : dir.listFiles()) {
            createThumbnail(file);
        }
    }

    private void createThumbnail(File file) {
        try {
            // BufferedImage is the best (Toolkit images are less flexible)
            BufferedImage img = ImageIO.read(file);
            BufferedImage thumb = createEmptyThumbnail();

            // BufferedImage has a Graphics2D
            Graphics2D g2d = (Graphics2D) thumb.getGraphics(); 
            g2d.drawImage(img, 0, 0, 
                          thumb.getWidth() - 1,
                          thumb.getHeight() - 1, 
                          0, 0, 
                          img.getWidth() - 1,
                          img.getHeight() - 1, 
                          null);
            g2d.dispose();
                       
            //File oFile = new File ("/Users/sydney/Desktop/CS 499/Slide-Show-Creator/src/pkgImageViewer/Thumbnail/imgThumb");
            ImageIO.write(thumb, "JPG", createOutputFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private File createOutputFile(File file) {
         
        
         File myFile = new File(file.getAbsolutePath()+ ".thumb.jpg");
         
        return myFile;
        
    }

    private BufferedImage createEmptyThumbnail() {
        return new BufferedImage(128, 72, 
                                 BufferedImage.TYPE_INT_RGB);
    }

    
}
 
