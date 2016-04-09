
package AnimatingCardLayout;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import pkgImageViewer.SetTransitionOptionsDlg;


public class SlideShow extends JFrame
{
   final static int ANIM_DUR = 2500;
   final static int DEFAULT_WINDOW_SIZE = 500;
   final static int TIMER_DELAY = 6000;
   
//  int theValue = (new pkgImageViewer.ImageViewer()).getValue();


   AnimatingCardLayout acl;

   Animation [] animations =
   {
      new SlideAnimation (), 
      new FadeAnimation (),      
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

        //------------------------------------------
	// Miscellaneous variables
	//------------------------------------------
	/** Image directory to show */
	static String m_sImageDir;
	
	/** Vector of image names */
	Vector m_vImageNames = null;
        
        /** Vector of transition identifier */
	static Vector m_vTransitionNumber = null;
        
        static int m_iTransitionTypes;
        
        //------------------------------------------
        
        
        
   public SlideShow ()
   {
      super ("Slide Show");
      //setDefaultCloseOperation (EXIT_ON_CLOSE);

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
                 
                               
                 acl.setAnimation (animations [m_iTransitionTypes]);

                  //random selection of transition
//                  acl.setAnimation (animations [(int) (Math.random ()*
//                                                animations.length)]);

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
      
      //OpenSlideShow(); //better try to call from main
      
      timer.start ();

      setSize (DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
      setExtendedState(MAXIMIZED_BOTH);
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
   
    //----------------------------------------------------------------------
    /** Load parameters of slide show from .xml file in project directory
    *  using properties object                                           */
    //----------
    static void OpenSlideShow() {
      
        Properties props = new Properties(); //create properties object
        String[] stringArray = null; //auxiliary array  
        
        //load properties from disk file to properties object
         
        try {
            File configFile = new File("slideshow.xml");
            InputStream inputStream = new FileInputStream(configFile);
            props.loadFromXML(inputStream);

            inputStream.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
        
     //retrieve properties from the properties object
    
     //....................... retrieve m_sImageDir
     m_sImageDir = props.getProperty( "ImageDir");
     
     
    
     //............................retrieve m_vTransitionNumber
     m_vTransitionNumber = new Vector();
     String stringOftransitionNames;   
     stringOftransitionNames = props.getProperty("TransitionNames");
     
    //convert back from single string to stringArray using split      
    stringArray = stringOftransitionNames.split(",");

    //assign strings from stringArray as elements of vector m_vTransitionNumber 
    
    System.out.println("in OpenSlideShow: retrieving transition names and assigning to m_vTransitionNumber ");
    
    for(String s: stringArray) {
        
        m_vTransitionNumber.add(s); // Add this one to vector
        System.out.println(s);  
    }
    stringArray = null;    //clear stringArray
     

    //............................. retrieve m_iTransitionTypes
    m_iTransitionTypes = Integer.parseInt(props.getProperty( "TransitionTypes" ));

       System.out.println("SlideShow: finished retrieving properties from properties object");
     
        System.out.print("integer:  m_iTransitionTypes:  ");
        System.out.printf("%d\n",  m_iTransitionTypes);
       
        System.out.print("string:  m_sImageDir:  ");
        System.out.printf("%s\n",  m_sImageDir);
        
    }//end OpenSlideShow()

   public static void main (String [] args)
   {
       
//      if (args.length != 1)
//      {
//          System.err.println ("usage: java SlideShow imagePath");
//          return;
//      }
  
      OpenSlideShow();
      
      final File imagePath = new File (m_sImageDir); 
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
