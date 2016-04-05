/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnimatingCardLayout;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.ArrayList;

import javax.swing.*;


public class SlideShow extends JFrame
{
   final static int ANIM_DUR = 2500;
   final static int DEFAULT_WINDOW_SIZE = 500;
   final static int TIMER_DELAY = 6000;

   AnimatingCardLayout acl;

   Animation [] animations =
   {
      new FadeAnimation (),
      new SlideAnimation (),
      //++++++++++++++++++++++++++add specific Fade Animations
      new FadeToBlackAnimation(),
      new FadeToWhiteAnimation(),
      new FadeFromBlackAnimation(),
      new FadeFromWhiteAnimation()
   };

   static ArrayList<ImageIcon> images = new ArrayList<> ();

   boolean showStartupMessage = true;

   int index;

   JPanel pictures;

   Timer timer;

   public SlideShow ()
   {
      super ("Slide Show");
      setDefaultCloseOperation (EXIT_ON_CLOSE);

      pictures = new JPanel ();
      pictures.setBackground (Color.black);

      acl = new AnimatingCardLayout ();
      acl.setAnimationDuration (ANIM_DUR);
      pictures.setLayout (acl);

      JLabel picture = new JLabel ();

      picture.setHorizontalAlignment (JLabel.CENTER);
      pictures.add (picture, "pic1");

      picture = new JLabel ();
      picture.setHorizontalAlignment (JLabel.CENTER);
      pictures.add (picture, "pic2");

      ActionListener al;
      al = new ActionListener ()
           {
               public void actionPerformed (ActionEvent ae)
               {
                  if (index == images.size ())
                  {
                      timer.stop (); // End the slideshow
                      return;
                  }
                  
                 // +++++++++++++++++non-random selection of transition 
                 // use one of the following codes:
                 
                 // [0]- slide      
                 // [1]- cross-fade 
                 // [2]- fade-to-black
                 // [3]- fade-to-white
                 // [4]- fade-from-black
                 // [5]- fade-from-white    
                  
                  acl.setAnimation (animations [2]);

                  //random selection of transition
                  //acl.setAnimation (animations [(int) (Math.random ()*
                  //                              animations.length)]);

                  if ((index & 1) == 0) // Even indexes
                  {
                      JLabel pic = (JLabel) pictures.getComponent (1);
                      pic.setIcon (images.get (index++));
                      try
                      {
                          acl.show (pictures, "pic2");
                      }
                      catch (IllegalStateException  ise)
                      {
                          index--; // Retry picture on next timer invocation
                      }
                  }
                  else // Odd indexes
                  {
                      JLabel pic = (JLabel) pictures.getComponent (0);
                      pic.setIcon (images.get (index++));
                      try
                      {
                          acl.show (pictures, "pic1");
                      }
                      catch (IllegalStateException  ise)
                      {
                          index--; // Retry picture on next timer invocation
                      }
                  }
               }
           };

      setContentPane (pictures);

      timer = new Timer (TIMER_DELAY, al);
      timer.start ();

      setSize (DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
      setVisible (true);
   }

   public void paint (Graphics g)
   {
      super.paint (g);

      if (showStartupMessage)
      {
          g.setColor (Color.yellow);
          g.drawString ("One moment please...", 30, 60);
          showStartupMessage = false;
      }
   }   
   

   public static void main (String [] args)
   {
       
//      if (args.length != 1)
//      {
//          System.err.println ("usage: java SlideShow imagePath");
//          return;
//      }
  
      final File imagePath = new File ("C:\\Users\\Andrew\\Documents\\GitHub\\Slide-Show-Creator\\src\\pkgImageViewer\\Images");
       
      if (!imagePath.isDirectory ())
      {
          System.err.println (args [0]+" is not a directory path");
          return;
      }

      Runnable r = new Runnable ()
                   {
                       public void run ()
                       {
                          // Load all GIF and JPEG images in the imagePath.

                          File [] filePaths = imagePath.listFiles ();
                          for (File filePath: filePaths)
                          { 
                               if (filePath.isDirectory ())
                                   continue;

                               String name;
                               name = filePath.getName ().toLowerCase ();
                               if (name.endsWith (".gif") ||
                                   name.endsWith (".jpg"))
                               {
                                   System.out.println ("Loading "+filePath);

                                   ImageIcon ii;
                                   ii = new ImageIcon (filePath.toString ());
                                   images.add (ii);
                               }
                          }

                          if (images.size () < 2)
                          {
                              System.err.println ("too few images");
                              System.exit (0);
                          }

                          new SlideShow ();
                       }
                   };
      EventQueue.invokeLater (r);
   }
}
