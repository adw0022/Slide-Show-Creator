/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgImageViewer;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Aaron Phillips
 */
public class FullScreen {

    FullScreen(final String temp) throws IOException {
        Frame frame = new Frame("Test");
    frame.setUndecorated(true);

    frame.add(new Component() {
        BufferedImage img = ImageIO.read(new File(temp));
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    });
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gs = ge.getDefaultScreenDevice();
    gs.setFullScreenWindow(frame);
    frame.validate();}
    }
    

    /**
     * @param args the command line arguments
     */
/*
    public void FullScreen(final String Test) throws IOException {

    Frame frame = new Frame("Test");
    frame.setUndecorated(true);

    frame.add(new Component() {
        BufferedImage img = ImageIO.read(new File(Test));
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    });

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gs = ge.getDefaultScreenDevice();
    gs.setFullScreenWindow(frame);
    frame.validate();}
    
}*/
