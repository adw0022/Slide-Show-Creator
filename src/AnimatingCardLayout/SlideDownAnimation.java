/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnimatingCardLayout;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author lasko_000
 */
public class SlideDownAnimation implements Animation {

  SpecialPanel animationPanel = null;
  private AnimationListener listener = null;
  boolean direction = true;
  int       animationDuration = 2000;
  
      static int ix = 1;

  public void setDirection(boolean direction){
    this.direction = direction;
  }

  public void setAnimationDuration(int animationDuration){
    this.animationDuration = (animationDuration < 1000)?1000:animationDuration;
  }


  public Component animate(final Component toHide, final Component toShow, AnimationListener listener) {
    this.listener = listener;
    animationPanel = new SpecialPanel(this, (direction)?toHide:toShow, (direction)?toShow:toHide);
    animationPanel.needToStartThread = true;
    animationPanel.beginAngle = (direction)?0:180;
    animationPanel.endAngle = (direction)?180:0;
    animationPanel.setAnimationDuration(animationDuration);
    return animationPanel;


  }

  public Component getAnimationPanel() {
    return animationPanel;
  }

  void rotationFinished() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        animationPanel = null;
        listener.animationFinished();
        listener = null;
      }
    });
  }


  class SpecialPanel extends JPanel{
  SlideDownAnimation owner;

  BufferedImage firstImage;
  BufferedImage secondImage;
  Component component1;
  Component component2;

  float angle = 0;

  public float beginAngle = 0;
  public float endAngle = 360;

  float deltaAngle = 0.5f;
  float effectTime = 2000;
  long dt = Math.round(effectTime* deltaAngle/180);
  int counter = 0;
  long totalDrawTime = 0;

  public boolean needToStartThread = false;

      SpecialPanel(SlideDownAnimation owner,BufferedImage firstImage, BufferedImage secondImage){
          this.owner = owner;
          this.firstImage = firstImage;
          this.secondImage = secondImage;
          angle = beginAngle;
          setOpaque(false);
      }

      SpecialPanel(SlideDownAnimation owner,Component component1, Component component2){
          this.owner = owner;
          this.component1 = component1;
          this.component2 = component2;
          angle = beginAngle;
          setOpaque(false);
      }

      public void setAnimationDuration(int animationDuration){
        effectTime = (animationDuration < 1000)?1000:animationDuration;
        dt = Math.round(effectTime* deltaAngle/180);
      }

      void startThread(float val1,float val2){
          ix = -1 * ix;  //generates alternating sign effect
          counter = 0;
          totalDrawTime = 0;
          this.beginAngle = val1;
          this.endAngle = val2;
          if(endAngle < beginAngle)   deltaAngle = -Math.abs(deltaAngle);
          else                        deltaAngle = Math.abs(deltaAngle);
          angle = beginAngle;          
          final Runnable repaint = new Runnable() { //am@kikamedical.com Arnaud Masson
             public void run() {
                 repaint();
                 getToolkit().sync();
             }                                      };
          Thread t = new Thread(new Runnable(){
              public void run(){
                  float absDeltaAngle=Math.abs(deltaAngle);
                  long startTime = System.currentTimeMillis();
                  long initTime = System.currentTimeMillis();
                  while(true){
                      long time = System.currentTimeMillis();
                      angle += deltaAngle*(time - startTime)/dt;//idea am@kikamedical.com Arnaud Masson
                      startTime = time;
                      if(((angle >= endAngle-deltaAngle/2) && (deltaAngle > 0)) ||
                         ((angle <= endAngle-deltaAngle/2) && (deltaAngle < 0))){
                          angle = endAngle;
                          if(Math.abs(angle - 360) < absDeltaAngle / 2) angle = 0;
                          if(Math.abs(angle - 180) < absDeltaAngle / 2) angle = 180;
                          repaint();
                          if(counter != 0) System.out.println("total count "+counter+" time "+(System.currentTimeMillis() - initTime)+" average time "+(totalDrawTime/counter));
                          break;
                      }
                      if(angle >= 360) angle = 0;
                      try{
                          Thread.sleep(dt);
                          repaint();
                          getToolkit().sync();
                         //SwingUtilities.invokeAndWait(repaint);  //idea am@kikamedical.com Arnaud Masson
                      }catch(Throwable tt){
                      }
                  }
                  if(owner != null) owner.rotationFinished();
                  synchronized(SpecialPanel.this){
                      if(component1 != null) firstImage = null;
                      if(component2 != null) secondImage = null;
                  }
              }
          });
          t.start();
      }

      public void update(Graphics g){
          paint(g);
      }

      public synchronized void paint(Graphics g){
          if(needToStartThread){
              totalDrawTime = 0;
              counter = 0;
              needToStartThread = false;
              startThread(beginAngle,endAngle);
              if(firstImage == null){
                  firstImage = createImageFromComponent(component1);
              }
              if(secondImage == null){
                  secondImage = createImageFromComponent(component2);
              }
          }
          if(firstImage == null || secondImage == null) return;
          Graphics2D g2d = (Graphics2D)g;
          int ww = firstImage.getWidth();
          int hh = firstImage.getHeight();
          {
              BufferedImage currImage = null;
              int []currPixels = null;
              int h = firstImage.getHeight();
              int offset = (int)(h*angle/180);
              if(offset < 0) offset = 0;
              if(offset > h) offset = h;

              long beforeDraw = System.currentTimeMillis();
              g2d.drawImage(firstImage,null,0,0);
              g2d.drawImage(secondImage,null,0,ix * (h - offset));
              totalDrawTime += (System.currentTimeMillis() - beforeDraw);
              counter++;

        }
      }

      BufferedImage createImageFromComponent(Component comp){
          BufferedImage retImage = null;
          if(comp == null) return retImage;
          try{
              GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
              GraphicsDevice gd = genv.getDefaultScreenDevice();
              GraphicsConfiguration gc = gd.getDefaultConfiguration();
              java.awt.image.ColorModel cm = gc.getColorModel();
            boolean hasAlpha = cm.hasAlpha();
            int cw = comp.getSize().width;
            int ch = comp.getSize().height;
            if(hasAlpha){
                retImage = gc.createCompatibleImage(cw,ch);
            }else{
                retImage = new BufferedImage(cw,ch,BufferedImage.TYPE_INT_ARGB);
            }
              if(retImage == null) return retImage;
              Graphics og = retImage.getGraphics();
                  comp.paint(og);
              og.dispose();
          }catch(Throwable t){
          }
          return retImage;

      }

  }


}
