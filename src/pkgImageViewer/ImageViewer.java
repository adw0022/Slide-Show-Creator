package pkgImageViewer;

import AnimatingCardLayout.SlideShow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import java.util.Collections;
import javax.swing.UIManager;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import java.awt.image.BufferedImage;
import java.lang.String;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.datatype.Duration;


//=============================================================================
/** Class: ImageViewer
 *  Purpose: This class implements the main window for an image viewer utility.
 *    It allows the user to select a directory of images (.jpg and/or .gif)
 *    and display them sequentially.  The user can switch images by clicking
 *    a button to move forward or backward in the list of images or by 
 *    using a timer to produce a slideshow.
 *  Author: Dr. Rick Coleman
 *  Date: April 2008
 */
//=============================================================================
public class ImageViewer extends JFrame
{    
    
//     private static void addFolderToZip(String string, String srcFolder, ZipOutputStream zip) 
//    {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

	/** Programmer ID */
	public String m_sID = "Dr. Rick Coleman";
	
	/** Panel displaying the images */
	public ImagePanel m_ImagePanel;
        
        /* Panel displaying series of thumbnails */
        public ImageThumbnail m_iThumb; 
        
        /** Image used for displaying thumbnail**/
        public BufferedImage m_theImage;
                
	/** Panel holding the buttons */
	private JPanel m_ButtonPanel;
        
        /** Panel holding the thumbnail */
	private JPanel m_ThumbPanel;
	
        /** Open Slide Show button */
	private JButton OpenSlideShowBtn;
                
	/** Display Options button */
	private JButton m_DisplayOptionsBtn;
        
        /** Transition Settings button */
        private JButton m_SlideSettings;
        
        /** Sound Settings button */
        private JButton m_SoundSettings;
	
	/** Select image directory button */
	private JButton m_SelectImageDirBtn;
        
        /** Shift image left in queue button */
	private JButton m_ShiftLeftBtn;
	
	/** Switch to previous image button */
	private JButton m_PrevImageBtn;
	
	/** Switch to next image button */
	private JButton m_NextImageBtn;
        
        /** Shift image right in queue button */
	private JButton m_ShiftRightBtn;
        
        /** Play slide-show in full-screen mode */
	private JButton m_PlayShow;
        
        /** Save slide-show layout */
	private JButton m_SaveShow;
	
	/** Exit button */
	private JButton m_ExitBtn;     
         
	
	//------------------------------------------
	// Display option variables
	//------------------------------------------
	/** Scale images flag */
	private boolean m_bScaleImages = true;
	
	/** Show image types flag. Default (3) is show both */
	private int m_iShowTypes = 3;
	
	/** Change images manually flag */
	private boolean m_bChangeManually = true;
	
	/** Time delay is using timer to change */
	private int m_iTimeDelay = 5;
        
        public int m_iTransitionTypes = 0;
	
	//------------------------------------------
	// Miscellaneous variables
	//------------------------------------------
	/** Image directory to show */
	private String m_sImageDir;
	
	/** Vector of image names */
	private Vector m_vImageNames = null;
        
        /** Vector of transition identifier */
	private Vector m_vTransitionNumber = null;
	
	/** Index of the current image */
	private int m_iCurImageIdx;
        
        /** Vector of image thumbnail names */
	private Vector m_vThumbNames = null;
                      	
	/** Index of the current image thumbnail */
	private int m_iCurThumbIdx;
	
	/** Image currently displayed */
	private Image  m_TheImage = null;	

	/** Timer for slide-shows */
	private Timer m_SSTimer;
        
        ///////////////////////////
        public int x;
        public int y;

	//---------------------------------------------------
	/** Default constructor */
	//---------------------------------------------------
	public ImageViewer()
	{
            	//------------------------------------------
		// Set all parameters for this JFrame object
		//------------------------------------------
		this.setSize(740, 600); // Make the window smaller than the screen
		this.setLocation(50, 50); // Set window location on screen
		this.setTitle("ImageViewer");
		this.getContentPane().setLayout(null); // We'll do our own layouts, thank you.
		this.getContentPane().setBackground(Color.DARK_GRAY); // Set visible area to gray

		// Create the image panel
		m_ImagePanel = new ImagePanel(this);
		this.getContentPane().add(m_ImagePanel); // Add the panel to the window
                
                //Create Thumbnail panel
                createThumbPanel();
                
//                m_ThumbPanel = new JPanel(new GridLayout(4,3));
//		m_ThumbPanel.setSize(this.getSize().width-400, 540);
//		m_ThumbPanel.setLocation(725, this.getSize().height-585);
//		m_ThumbPanel.setBackground(Color.lightGray); // Set the panel color
//		m_ThumbPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                
//                
//                // Use the default Flow Layout manager
//		this.getContentPane().add(m_ThumbPanel);

		// Create the button panel
		m_ButtonPanel = new JPanel();
		m_ButtonPanel.setSize(this.getSize().width-50, 35);
		m_ButtonPanel.setLocation(20, this.getSize().height-80);
		m_ButtonPanel.setBackground(Color.LIGHT_GRAY); // Set the panel color
		m_ButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// Use the default Flow Layout manager
		this.getContentPane().add(m_ButtonPanel);
		
                // Create the OpenSlideShowBtn button
//		OpenSlideShowBtn = new JButton(new ImageIcon("Images/Open Slideshow.jpg"));
		OpenSlideShowBtn = new JButton(new ImageIcon(getClass().getResource("Images/Open Slideshow.jpg")));

		OpenSlideShowBtn.setSize(20, 20);
		OpenSlideShowBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		OpenSlideShowBtn.setToolTipText("Click to open slideshow from saved settings.");
		OpenSlideShowBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle setting the display options
						OpenSlideShow();
					}
				});
		m_ButtonPanel.add(OpenSlideShowBtn);	
                
		// Create the Display Options button
//		m_DisplayOptionsBtn = new JButton(new ImageIcon("Images/DisplayOptions.jpg"));
		m_DisplayOptionsBtn = new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg")));

		m_DisplayOptionsBtn.setSize(20, 20);
		m_DisplayOptionsBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_DisplayOptionsBtn.setToolTipText("Click to set display options.");
		m_DisplayOptionsBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle setting the display options
						setDisplayOptions();
					}
				});
		m_ButtonPanel.add(m_DisplayOptionsBtn);
                
               m_SlideSettings = new JButton(new ImageIcon(getClass().getResource("Images/settings.png")));

		m_SlideSettings.setSize(20, 20);
		m_SlideSettings.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_SlideSettings.setToolTipText("Click to open transition settings.");
		m_SlideSettings.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
                                                //Handle setting the transition options
						setTransitionOptions();
					}
				});
		m_ButtonPanel.add(m_SlideSettings);
		
                m_SoundSettings = new JButton(new ImageIcon(getClass().getResource("Images/sound.png")));

		m_SoundSettings.setSize(20, 20);
		m_SoundSettings.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_SoundSettings.setToolTipText("Click to open sound settings.");
		m_SoundSettings.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
                                                //Handle setting the sound options						
					}
				});
		m_ButtonPanel.add(m_SoundSettings);
                
                
		// Create the select image directory button
//		m_SelectImageDirBtn = new JButton(new ImageIcon("Images/OpenDirectory.jpg"));
		m_SelectImageDirBtn = new JButton(new ImageIcon(getClass().getResource("Images/OpenDirectory.jpg")));
		m_SelectImageDirBtn.setSize(20, 20);
		m_SelectImageDirBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_SelectImageDirBtn.setToolTipText("Click to select directory of images to view.");
		m_SelectImageDirBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle getting the image directory to show
						getImageDir();
						if(m_sImageDir != null)
						{
							buildImageList();
							showImage(m_iCurImageIdx); // Show first image
						}
						// Are we doing a slideshow with timer?
						if(!m_bChangeManually)
						{
							doTimerSlideShow();
						}
					}
				});
		m_ButtonPanel.add(m_SelectImageDirBtn);
                
                                
                m_ShiftLeftBtn = new JButton(new ImageIcon(getClass().getResource("Images/ShiftLeft.jpg")));

		m_ShiftLeftBtn.setSize(20, 20);
		m_ShiftLeftBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_ShiftLeftBtn.setToolTipText("Click to shift image left in vector.");
		m_ShiftLeftBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//Move image left
                                            shiftImageLeft();
					}
				});
		m_ButtonPanel.add(m_ShiftLeftBtn);	
		
		// Create the previous image button
//		m_PrevImageBtn = new JButton(new ImageIcon("Images/BackArrow.jpg"));
		m_PrevImageBtn = new JButton(new ImageIcon(getClass().getResource("Images/BackArrow.jpg")));
		m_PrevImageBtn.setSize(20, 20);
		m_PrevImageBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_PrevImageBtn.setToolTipText("View previous image.");
		m_PrevImageBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Show the previous image
						showPreviousImage();
					}
				});
		m_ButtonPanel.add(m_PrevImageBtn);	
		
		// Create the next image button
//		m_NextImageBtn = new JButton(new ImageIcon("Images/NextArrow.jpg"));
		m_NextImageBtn = new JButton(new ImageIcon(getClass().getResource("Images/NextArrow.jpg")));
		m_NextImageBtn.setSize(20, 20);
		m_NextImageBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_NextImageBtn.setToolTipText("View next image.");
		m_NextImageBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Show the next image
						showNextImage();
					}
				});
		m_ButtonPanel.add(m_NextImageBtn);
                
                m_ShiftRightBtn = new JButton(new ImageIcon(getClass().getResource("Images/ShiftRight.jpg")));

		m_ShiftRightBtn.setSize(20, 20);
		m_ShiftRightBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_ShiftRightBtn.setToolTipText("Click to shift image right in vector.");
		m_ShiftRightBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//Move image right
                                            shiftImageRight();
					}
				});
		m_ButtonPanel.add(m_ShiftRightBtn);
                
                m_PlayShow = new JButton(new ImageIcon(getClass().getResource("Images/play.png")));
		m_PlayShow.setSize(20, 20);
		m_PlayShow.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_PlayShow.setToolTipText("Click to Play slideshow.");
		m_PlayShow.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
                                            String[] args = null;
                                            
                                            //call player of slides with transitions and soundtrack
                                            AnimatingCardLayout.SlideShow.main(args);                                                
                                        }
                                });                                   
				
		m_ButtonPanel.add(m_PlayShow);
                
                
                
                m_SaveShow = new JButton(new ImageIcon(getClass().getResource("Images/save.png")));
		m_SaveShow.setSize(20, 20);
		m_SaveShow.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_SaveShow.setToolTipText("Click to save slideshow.");
		m_SaveShow.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
                                            SaveSlideShow();    //save settings
                                            saveImagesZip();    //save images
					}
				});
		m_ButtonPanel.add(m_SaveShow);

		// Create the exit button
		m_ExitBtn = new JButton(new ImageIcon(getClass().getResource("Images/Exit.jpg")));
		m_ExitBtn.setSize(20, 20);
		m_ExitBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_ExitBtn.setToolTipText("Click to exit the application.");
		m_ExitBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						// Exit the application with status=0 (normal exit)
						System.exit(0);
					}
				});
		m_ButtonPanel.add(m_ExitBtn);	
		
		// Make the window visible
		this.setVisible(true);
	}          
       
         

	//----------------------------------------------------------------------
	/** Show a dialog box for the user to set the display options */
	//----------------------------------------------------------------------
	private void setDisplayOptions()
	{
		int retVal;
		
		// Create and show a dialog box
		SetDisplayOptionsDlg dlg = new SetDisplayOptionsDlg(this, true);
		dlg.setVisible(true); // show it
		retVal = dlg.getExitStatus();
		if(retVal == 0) // If the user clicked OK get the values
		{
			m_bScaleImages = dlg.getScaleImage();
			m_iShowTypes = dlg.getShowTypes();
			m_bChangeManually = dlg.getChangeManually();
			m_iTimeDelay = dlg.getTimeDelay();
		}
		dlg.dispose(); // Destroy the dialog box
	}
        
        public void setTransitionOptions()
	{
		int retVal;
		
		// Create and show a dialog box
		SetTransitionOptionsDlg dlg1 = new SetTransitionOptionsDlg(this, true);
		dlg1.setVisible(true); // show it
		retVal = dlg1.getExitStatus();
		if(retVal == 0) // If the user clicked OK get the values
		{			
			m_iTransitionTypes = dlg1.getTransitionTypes();                    
		}
		dlg1.dispose(); // Destroy the dialog box   
	}
        
	
	//----------------------------------------------------------------------
	/** Show an open file dialog box in order to get the directory of
	 *   images to display. */
	//----------------------------------------------------------------------
	private void getImageDir()
	{
		int retValue;	// Return value from the JFileChooser
		
	     JFileChooser chooser = new JFileChooser();	// Create the file chooser dialog box
	     chooser.setDialogTitle("Select Image Directory"); // Set dialog title
	     chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only select dirs
	     chooser.setApproveButtonText("Select");
	     retValue = chooser.showOpenDialog(this); // Show the dialog box
	     if(retValue == JFileChooser.APPROVE_OPTION) // User selected a file
	     {
	    	 // Got a directory so get it's full path
	    	 m_sImageDir = chooser.getSelectedFile().getAbsolutePath();
	     }
//		System.out.println("Dir: " + m_sImageDir);
	}
	
	//----------------------------------------------------------------------
	/** Build the list of images to show */
	//----------------------------------------------------------------------
	private void buildImageList()
	{
        File		chosenDir; // Directory of images
        File[]		fileList;  // Array of files in the directory
        String		fileName;  // Name of a file

        // Create the vector of names
        if(m_vImageNames != null) // If we already have one
        	m_vImageNames.removeAllElements(); // Clean it out
        else                      // If we don't have one
        	m_vImageNames = new Vector(); // Create a new one.
        // Open the directory
        chosenDir = new File(m_sImageDir);
        if(chosenDir != null)	// If we opened it successfully
        {
        	fileList = chosenDir.listFiles(); // Get a list of all files
        	// Go through the list and get the complete path of all image
        	// files (those with .jpg and/or .gif)
        	for(int i=0; i<fileList.length; i++)
        	{
        		fileName = fileList[i].getAbsolutePath(); // Get path name
        		// Is it a .jpg file?
        		if((fileName.endsWith(".jpg")) || (fileName.endsWith(".JPG")))  
        		{
        			// 1 == show only JPG      3 == show JPG and GIF
        			if((m_iShowTypes == 1) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
                                        
        		}
        		else if((fileName.endsWith(".jpeg")) || (fileName.endsWith(".JPEG")))  
        		{
        			// 1 == show only JPG      3 == show JPG and GIF
        			if((m_iShowTypes == 1) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
        		}
        		// Is it a .gif file?
        		else if((fileName.endsWith(".gif")) || (fileName.endsWith(".GIF"))) // Is it a .gif file?
        		{
        			// 2 == show only GIF      3 == show JPG and GIF
        			if((m_iShowTypes == 2) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
        		}
        	} // end for loop
        	m_iCurImageIdx = 0; // Initialize the current image index 
        } // end if(chosenDir != null)
        
        // Create the vector of names
        if(m_vTransitionNumber != null) // If we already have one
        	m_vTransitionNumber.removeAllElements(); // Clean it out
        else                      // If we don't have one
        	m_vTransitionNumber = new Vector(); // Create a new one.
        int temp = m_vImageNames.size();
        for(int i=0; i<temp; i++){
            m_vTransitionNumber.add("0");
        }
        
        /*
        for(int i=0; i< m_vImageNames.size(); i++)
        {
        	fileName = (String)(m_vImageNames.elementAt(i));
        	System.out.println(fileName);
        }
        */
	}
        
        //----------------------------------------------------------------------
	/** Show image thumbnail of the chosen file. */
	//----------------------------------------------------------------------
	
        private void createThumbnail() 
        {         
            ImageThumbnail m_iThumb = new ImageThumbnail();
            File chosenDir = new File(m_sImageDir);
            String dir = chosenDir.getAbsolutePath();
            m_iThumb.run(dir);
        }
        
        //----------------------------------------------------------------------
	/** Build the list of images thumbnail to show */
	//----------------------------------------------------------------------
	private void buildThumbList()
	{
            File		chosenDir; // Directory of images
            File[]		fileList;  // Array of files in the directory
            String		fileName;  // Name of a file
            File                thumbNames = null;


            // Create the vector of names
            if(m_vThumbNames != null) // If we already have one
                    m_vThumbNames.removeAllElements(); // Clean it out
            else                      // If we don't have one
                    m_vThumbNames = new Vector(); // Create a new one.
            // Open the directory
            chosenDir = new File(m_sImageDir);
            if(chosenDir != null)	// If we opened it successfully
            {
                    fileList = chosenDir.listFiles(); // Get a list of all files
                    // Go through the list and get the complete path of all image
                    // files (those with .jpg and/or .gif)
                    for(int i=0; i<fileList.length; i++)
                    {
                            fileName = fileList[i].getAbsolutePath(); // Get path name
                            // Is it a .jpg file?
                            if(fileName.endsWith(".thumb.jpg"))   
                            {
                                    // 1 == show only JPG      3 == show JPG and GIF
                                    //if((m_iShowTypes == 1) || (m_iShowTypes == 3))

                                            m_vThumbNames.add(fileName); // Add this one to the list
                                            File file = new File(fileName);
                                            file.renameTo(new File ("/Users/Andrew/Documents/GitHub/Slide-Show-Creator/src/Thumbnails" + file.getName()));


                            }

                    } // end for loop
                    m_iCurThumbIdx = 0; // Initialize the current image index 
            } // end if(chosenDir != null)        


           
            for(int i=0; i< m_vThumbNames.size(); i++)
            {
                    fileName = (String)(m_vThumbNames.elementAt(i));
                    System.out.println(fileName);
                    File file = new File(fileName);
                    file.renameTo(new File ("/Users/Andrew/Documents/GitHub/Slide-Show-Creator/src/Thumbnails"));

            }
        
	}
        
        
        //----------------------------------------------------------------------
	/** Create Thumbnail panel. */
	//----------------------------------------------------------------------
        private void createThumbPanel()
        {
//              this.setSize(740, 600); // Make the window smaller than the screen
//		this.setLocation(50, 50); // Set window location on screen
//		this.setTitle("ImageViewer");
//		this.getContentPane().setLayout(null); // We'll do our own layouts, thank you.
//		this.getContentPane().setBackground(Color.gray); // Set visible area to gray
//            
//            
//                m_ThumbPanel = new JPanel(new GridLayout(4,3));
//		m_ThumbPanel.setSize(this.getSize().width-400, 540);
//		m_ThumbPanel.setLocation(725, this.getSize().height-585);
//		m_ThumbPanel.setBackground(Color.lightGray); // Set the panel color
//		m_ThumbPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
////                
            
                m_ThumbPanel = new JPanel(new GridLayout(4,3));
		m_ThumbPanel.setSize(740-400, 540);
		m_ThumbPanel.setLocation(725, 600-585);
		m_ThumbPanel.setBackground(Color.lightGray); // Set the panel color
		m_ThumbPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
                
//                                JButton button = new JButton();
//                 try {
//                   Image img = ImageIO.read(getClass().getResource("Images/DisplayOptions.jpg"));
//                   button.setIcon(new ImageIcon(img));
//                   m_ThumbPanel.add(button);
//                 } catch (IOException ex) {
//                 }
////                  
//                File thumbFile = new File("/Users/sydney/Desktop/CS 499/Slide-Show-Creator/src/pkgImageViewer/Thumbnail/");
//                File [] listOfFiles = thumbFile.listFiles();
//                for (int i=0; i< listOfFiles.length; i++)
//                {
//                    String thumbName = listOfFiles[i].getName();
//                    m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Thumbnail/"+ thumbName))));
//                }
//		
                 
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));
//                m_ThumbPanel.add(new JButton(new ImageIcon(getClass().getResource("Images/DisplayOptions.jpg"))));

                
                // Use the default Flow Layout manager
		this.getContentPane().add(m_ThumbPanel);
        }
        
	
	//----------------------------------------------------------------------
	/** Show the image at index. */
	//----------------------------------------------------------------------
        @SuppressWarnings("empty-statement")
	private void showImage(int idx)
	{
        File		imageFile; // the jpg or gif file
		// Make sure we have an image file
 //       System.out.println("Image idx = " + idx);
        imageFile = new File((String)(m_vImageNames.elementAt(idx)));
		if(!imageFile.exists()) // If we failed to opened it
		{
			JOptionPane.showMessageDialog(this, 
					"Error: Unable to load " + (String)(m_vImageNames.elementAt(idx)), 
					"Error Loading Image", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Load the image
        Toolkit tk = Toolkit.getDefaultToolkit();
        if(m_TheImage != null)
        	m_TheImage = null; // Clear the previous image
        m_TheImage = tk.getImage((String)(m_vImageNames.elementAt(idx)));
        while(m_TheImage.getWidth(this) < 0); // Wait for all of the image to load
        // Draw the image and center it
        m_ImagePanel.setImage(m_TheImage);
        m_ImagePanel.paint(m_ImagePanel.getGraphics());
	}
	
	//----------------------------------------------------------------------
	/** Show the previous image. */
	//----------------------------------------------------------------------
	private void showPreviousImage()
	{
		if(m_iCurImageIdx > 0)
		{			
                        m_iCurImageIdx--; // Decrement to previous image
			showImage(m_iCurImageIdx); // Show it
		}
	}
        
        //----------------------------------------------------------------------
	/** Move Current image left by one. */
	//----------------------------------------------------------------------
	private void shiftImageLeft()
	{
		if(m_iCurImageIdx > 0)
		{
                        int temp = m_iCurImageIdx--;
			Collections.swap(m_vImageNames, m_iCurImageIdx, temp);
                        Collections.swap(m_vTransitionNumber, m_iCurImageIdx, temp); 
			showImage(m_iCurImageIdx); // Show it
		}
	}
        
        //----------------------------------------------------------------------
	/** Move Current image right by one. */
	//----------------------------------------------------------------------
	private void shiftImageRight()
	{
		if(m_iCurImageIdx < (m_vImageNames.size() - 1))
		{
                        int temp = m_iCurImageIdx++;
			Collections.swap(m_vImageNames, m_iCurImageIdx, temp);
                        Collections.swap(m_vTransitionNumber, m_iCurImageIdx, temp);
			showImage(m_iCurImageIdx); // Show it
		}
	}
	
	//----------------------------------------------------------------------
	/** Show the next image. */
	//----------------------------------------------------------------------
	private void showNextImage()
	{
		if(m_iCurImageIdx < (m_vImageNames.size() - 1))
		{
			m_iCurImageIdx++; // Increment to next image
			showImage(m_iCurImageIdx); // Show it
		}
	}

	//----------------------------------------------------------------------
	/** Show the next image. */
	//----------------------------------------------------------------------
	private void doTimerSlideShow()
	{
		// Disable the previous and next buttons while the slideshow runs
		m_PrevImageBtn.setEnabled(false);
		m_NextImageBtn.setEnabled(false);
		
		// Create a javax.swing.timer
		m_SSTimer = new Timer(m_iTimeDelay * 1000,
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Show the next image
					if(m_iCurImageIdx < m_vImageNames.size() - 1)
					{
						showNextImage();
					}
					else
					{
						m_SSTimer.stop();
						// Enable the previous and next buttons again
						m_PrevImageBtn.setEnabled(true);
						m_NextImageBtn.setEnabled(true);
					}
				}
			});
		m_SSTimer.setRepeats(true); // Repeat till we kill it
		m_SSTimer.start();  // Start the timer
	}     
        
        
       public static void pack(final Path folder, final Path zipFilePath) throws IOException 
       {
            try(FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
                ZipOutputStream zos = new ZipOutputStream(fos))
            {
                Files.walkFileTree(folder, new SimpleFileVisitor<Path>() 
                {
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                    {
                        zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }

                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException 
                    {
                        zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }

                });
            }
        }     
       
       public boolean deleteDirectory(File directory) 
       {
            if(directory.exists())
            {
                File[] files = directory.listFiles();
                if(null!=files)
                {
                    for(int i=0; i<files.length; i++) 
                    {
                        if(files[i].isDirectory()) 
                        {
                            deleteDirectory(files[i]);
                        }
                        else 
                        {
                            files[i].delete();
                        }
                    }
                }
            }
            return(directory.delete());
        }
   
        //----------------------------------------------------------------------
	/** Save images in .zip file */
	//----------------------------------------------------------------------
	private void saveImagesZip()
	{
        if(m_vImageNames != null)
        {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            s = s.concat("/savefile");
            File settings = new File(s, "settings.txt");
            new File(s).mkdir();
            FileWriter fw = null;
                try 
                {
                    fw = new FileWriter(settings.getAbsoluteFile());
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedWriter bw = new BufferedWriter(fw);

            for(int i=0; i< m_vImageNames.size(); i++)
            {
                String place = "/image";
                place = place.concat(String.valueOf(i));
                String destemp = s;
                String imgpath = (String) m_vImageNames.get(i);
                File source = new File(imgpath);

                if((imgpath.endsWith(".jpg")) || (imgpath.endsWith(".JPG")))  
                {
                    place = place.concat(".jpg");
                    destemp = destemp.concat(place);
                }
                else if((imgpath.endsWith(".jpeg")) || (imgpath.endsWith(".JPEG")))  
                {
                    place = place.concat(".jpeg");
                    destemp = destemp.concat(place);
                }    
                else if((imgpath.endsWith(".gif")) || (imgpath.endsWith(".GIF")))
                {
                    place = place.concat(".gif");
                    destemp = destemp.concat(place);
                }

                File destination = new File(destemp);

                try 
                {
                    Files.copy(source.toPath(), destination.toPath(), REPLACE_EXISTING);
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                }

                String tempbuffer = (String) m_vTransitionNumber.get(i);

                try 
                {
                    bw.write(tempbuffer);
                    bw.newLine();
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try 
            {
                bw.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
            }                                                
            String destzip = s.concat(".zip");
            Path destin = Paths.get(destzip);
            Path sourcefile = Paths.get(s);

            try 
            {
                pack(sourcefile, destin);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
            File savedir = new File(s);
            deleteDirectory(savedir);
        }
    }//end saveImagesZip   
        
    //----------------------------------------------------------------------
    /** Save parameters of slide show to .xml file in project directory
    *  using properties object                                           */
    //----------
    public void SaveSlideShow() {
        
        Properties props = new Properties(); //create properties object
        String[] stringArray = null; //auxiliary array  
        
        //....................... save m_sImageDir
        props.setProperty( "ImageDir", m_sImageDir );
         
        
        //............................save m_vImageNames
        String stringOfImageNames;
        stringArray = new String[m_vImageNames.size()];//auxiliary array  
        
        //convert vector m_vImageNames to array stringArray of image names         
        for (int i=0; i < m_vImageNames.size(); i++ ) {
            
            stringArray[i] = m_vImageNames.elementAt(i).toString();   
        }
         
        //convert string array to stringOfImageNames using join() function
        stringOfImageNames = join(",", stringArray);
         
        //set property on property object
        props.setProperty("ImageNames", stringOfImageNames);
         
        stringArray = null;    //clear stringArray
        
        
         //............................save m_vTransitionNumber
        String stringOftransitionNames;
        stringArray = new String[m_vTransitionNumber.size()];   
        //convert vector m_vTransitionNumber to stringArray  
        
        for (int i=0; i < m_vTransitionNumber.size(); i++ ) {
            
            stringArray[i] = m_vTransitionNumber.elementAt(i).toString();   
        }
         
        //convert string array to stringOftransitionNames using join() function
        stringOftransitionNames = join(",", stringArray);
         
        //set property on property object
        props.setProperty("TransitionNames", stringOftransitionNames);
         
        stringArray = null;    //clear stringArray
        
        
         //......................... save m_bScaleImages
         props.setProperty("ScaleImages", m_bScaleImages ? "true" : "false");
         
         //.......................... save m_iShowTypes
         props.setProperty( "ShowTypes", String.valueOf(m_iShowTypes) );
         
          //........................... save m_bChangeManually
         props.setProperty("ChangeManually", m_bChangeManually ? "true" : "false");
         
         //............................. save m_iTimeDelay
         props.setProperty( "TimeDelay", String.valueOf(m_iTimeDelay) );
         
         //............................. save m_iTransitionTypes
         props.setProperty( "TransitionTypes", String.valueOf(m_iTransitionTypes) );
         
        //------------------------------------
        //write properties to .xml file in default project directory
        try {
            //hard code file name to be "slideshow.xml"
            File configFile = new File("slideshow.xml");
            OutputStream outputStream = new FileOutputStream(configFile);
            props.storeToXML(outputStream, "slideShow settings");
            outputStream.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            //I/O error
        }        
        
        //------------------------------------
        //additionally, write properties to .xml file in directory of images
        try {
            //hard code file name to be "slideshow.xml"
            File configFile = new File(m_sImageDir + "\\slideshow.xml");
            OutputStream outputStream = new FileOutputStream(configFile);
            props.storeToXML(outputStream, "slideShow settings");
            outputStream.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            //I/O error
        }      
        
    }//end SaveSlideShow()
     
    
    //----------------------------------------------------------------------
     /**
     * utility function used by SaveSlideShow()
     * join() creates a single String from String array 
     * using delimiter provided in the first argument
     * @param delimiter
     * @param s     String array to be joined
     * @return      single String representing contents of s */
    //----------
    private String join(String delimiter, String[] s) {
        int ls = s.length;
        switch (ls)
        {
            case 0: return "";
            case 1: return s[0];
            case 2: return s[0].concat(delimiter).concat(s[1]);
            default:
                int l1 = ls / 2;
                String[] s1 = Arrays.copyOfRange(s, 0, l1); 
                String[] s2 = Arrays.copyOfRange(s, l1, ls); 
                return join(delimiter, s1).concat(delimiter).
                                           concat(join(delimiter, s2));
        }
    }//end join
      
    
    //----------------------------------------------------------------------
    /** Load parameters of slide show from .xml file in project directory
    *  using properties object                                           */
    //----------
    public void OpenSlideShow() {
      
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
     
     //............................retrieve m_vImageNames
     m_vImageNames = new Vector();
     String stringOfImageNames;   
     stringOfImageNames = props.getProperty("ImageNames");
     
    //convert back from single string to stringArray using split      
    stringArray = stringOfImageNames.split(",");

    //assign strings from stringArray as elements of vector m_vImageNames 
    
    System.out.println("in OpenSlideShow: retrieving image names and assigning to m_vImageNames ");
    
    for(String s: stringArray) {
        
        m_vImageNames.add(s); // Add this one to vector
        System.out.println(s);  
    }
    stringArray = null;    //clear stringArray
    
    showImage(m_iCurImageIdx); // Show first image 
    
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
     
        
     //......................... retrieve m_bScaleImages 
     m_bScaleImages = (props.getProperty("ScaleImages")).equals("true");
     
     //.......................... retrieve m_iShowTypes
     m_iShowTypes = Integer.parseInt(props.getProperty( "ShowTypes" ));

     //........................... retrieve m_bChangeManually
     m_bChangeManually = (props.getProperty("ChangeManually")).equals("true");

     //............................. retrieve m_iTimeDelay
     m_iTimeDelay = Integer.parseInt(props.getProperty( "TimeDelay"));

    //............................. retrieve m_iTransitionTypes
    m_iTransitionTypes = Integer.parseInt(props.getProperty( "TransitionTypes" ));

       System.out.println("finished retrieving properties from properties object");
    
        System.out.print("boolean:  scaleimage:  ");
        System.out.println(m_bScaleImages);
        System.out.print("boolean:  changemanul");
        System.out.println(m_bChangeManually);
        System.out.print("integer:  m_iShowTypes:  ");
        System.out.printf("%d\n",  m_iShowTypes);
        System.out.print("integer:  m_iTransitionTypes:  ");
        System.out.printf("%d\n",  m_iTransitionTypes);
        System.out.print("integer:  m_iTimeDelay:  ");
        System.out.printf("%d\n",  m_iTimeDelay);
        System.out.print("string:  m_sImageDir:  ");
        System.out.printf("%s\n",  m_sImageDir);
        
    }//end OpenSlideShow()


        //----------------------------------------------------------------------
	/** Main function for this demonstration
	 * @param args - Array of strings from the command line
	 */
	//----------------------------------------------------------------------
	public static void main(String[] args) 
	{
            // When you start this application this function gets called by the
            //  operating system.  Main just creates an ImageViewer object.
            //  To follow the execution trail from here go to the ImageViewer
            //  constructor.

            try {
               // Set System L&F
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } 
            catch (UnsupportedLookAndFeelException e) {
               // handle exception
            }
            catch (ClassNotFoundException e) {
               // handle exception
            }
            catch (InstantiationException e) {
               // handle exception
            }
            catch (IllegalAccessException e) {
               // handle exception
            }
                         new ImageViewer();
              
	}
}
