
package AnimatingCardLayout;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SlideShow extends JFrame
{   
   static int m_iTimeDelay;
   final static int ANIM_DUR = 2500;
   final static int TIMER_DELAY = 1000;
   final static int DEFAULT_WINDOW_SIZE = 500;
   static double SpeedupFactor = 1.0;
   
   //default size of sound player control
   final static int SOUNDPlAYER_DEFAULT_WINDOW_X_SIZE = 1858;
   final static int SOUNDPlAYER_DEFAULT_WINDOW_Y_SIZE = 60;
   
//  int theValue = (new pkgImageViewer.ImageViewer()).getValue();


   AnimatingCardLayout acl;

   Animation [] animations =
   {      
      new FadeAnimation (), //0     
      //++++++++++++++++++++++++++add specific Fade Animations
      new FadeToBlackAnimation(), //1
      new FadeToWhiteAnimation(), //2
      new FadeFromBlackAnimation(), //3
      new FadeFromWhiteAnimation(), //4
      new SlideUpAnimation(), //5
      new SlideDownAnimation(), //6
      new SlideLeftAnimation(), //7
      new SlideRightAnimation() //8
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
        
        /** Sound directory to show */
	static String m_sSoundFile;
	
	/** Vector of image names */
	static Vector m_vImageNames = null;
        
        /** Vector of transition identifier */
	static Vector m_vTransitionNumber = null;
        
        static int m_iTransitionTypes;  
        
        
        //------------------------------------------
      
     //++++++++++++++++++++  include SoundPlayer as inner class   
     /**
     * This class is a Swing component that can load and play a sound clip,
     * displaying progress and controls. The main() method is a test program. This
     * component can play sampled audio or MIDI files, but handles them differently.
     * For sampled audio, time is reported in microseconds, tracked in milliseconds
     * and displayed in seconds and tenths of seconds. For midi files time is
     * reported, tracked, and displayed in MIDI "ticks". This program does no
     * transcoding, so it can only play sound files that use the PCM encoding.
     */
    public class SoundPlayer extends JComponent {
      boolean midi; // Are we playing a midi file or a sampled one?

      Sequence sequence; // The contents of a MIDI file

      Sequencer sequencer; // We play MIDI Sequences with a Sequencer

      Clip clip; // Contents of a sampled audio file

      boolean playing = false; // whether the sound is current playing

      // Length and position of the sound are measured in milliseconds for
      // sampled sounds and MIDI "ticks" for MIDI sounds
      int audioLength; // Length of the sound.

      int audioPosition = 0; // Current position within the sound

      // The following fields are for the GUI
      JButton play; // The Play/Stop button

      JButton ffwrd; // The ffrwd button
      
      JButton rwd; // The rewind button
      
      JSlider progress; // Shows and sets current position in sound

      JLabel time; // Displays audioPosition as a number

      Timer timer; // Updates slider every 100 milliseconds

      

      // Create an SoundPlayer component for the specified file.
      public SoundPlayer(File f, boolean isMidi) throws IOException, UnsupportedAudioFileException,
          LineUnavailableException, MidiUnavailableException, InvalidMidiDataException {
        if (isMidi) { // The file is a MIDI file
          midi = true;
          // First, get a Sequencer to play sequences of MIDI events
          // That is, to send events to a Synthesizer at the right time.
          sequencer = MidiSystem.getSequencer(); // Used to play sequences
          sequencer.open(); // Turn it on.

          // Get a Synthesizer for the Sequencer to send notes to
          Synthesizer synth = MidiSystem.getSynthesizer();
          synth.open(); // acquire whatever resources it needs

          // The Sequencer obtained above may be connected to a Synthesizer
          // by default, or it may not. Therefore, we explicitly connect it.
          Transmitter transmitter = sequencer.getTransmitter();
          Receiver receiver = synth.getReceiver();
          transmitter.setReceiver(receiver);

          // Read the sequence from the file and tell the sequencer about it
          sequence = MidiSystem.getSequence(f);
          sequencer.setSequence(sequence);
          audioLength = (int) sequence.getTickLength(); // Get sequence length
        } else { // The file is sampled audio
          midi = false;
          // Getting a Clip object for a file of sampled audio data is kind
          // of cumbersome. The following lines do what we need.
          AudioInputStream ain = AudioSystem.getAudioInputStream(f);
          try {
            DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ain);
          } finally { // We're done with the input stream.
            ain.close();
          }
          // Get the clip length in microseconds and convert to milliseconds
          audioLength = (int) (clip.getMicrosecondLength() / 1000);
        }

        // Now create the basic GUI
        play = new JButton("Play"); // Play/stop button
        progress = new JSlider(0, audioLength, 0); // Shows position in sound
        time = new JLabel("0"); // Shows position as a #
        ffwrd = new JButton("ffwrd"); // ffwd button
        rwd = new JButton("rewind"); // rewoind button
        
        // When clicked, start or stop playing the sound
        play.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (playing)
              stop();
            else
              play();
          }
        });

        // When clicked, fast forward the slideshow 
        ffwrd.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              AnimatingCardLayout my_acl = (AnimatingCardLayout) pictures.getLayout();
              
              if (SpeedupFactor == 1.0) {
                  System.out.println("i have sped up.");
                  SpeedupFactor = 20.0;
                  my_acl.setAnimationDuration ((int) (ANIM_DUR / SpeedupFactor));
              }
              else {
                  SpeedupFactor = 1.0;
                  my_acl.setAnimationDuration ((int) (ANIM_DUR / SpeedupFactor));
              }
              
              pictures.setLayout (my_acl);//assign layout to pictures            
              
              //speed up main timer between slides
              
              SlideShow.this.timer.setDelay((int) (TIMER_DELAY / SpeedupFactor * m_iTimeDelay ));
          }
        });//end frwd
        
        
        // When clicked, rewind the slideshow 
        rwd.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              stop();
              Collections.reverse(images);
              index = (images.size() - index - 1) + 2;
              
               AnimatingCardLayout my_acl = (AnimatingCardLayout) pictures.getLayout();
              
              if (SpeedupFactor == 1.0) {
                  System.out.println("i have sped up.");
                  SpeedupFactor = 20.0;
                  my_acl.setAnimationDuration ((int) (ANIM_DUR / SpeedupFactor));
              }
              else {
                  SpeedupFactor = 1.0;
                  my_acl.setAnimationDuration ((int) (ANIM_DUR / SpeedupFactor));
              }
              
              pictures.setLayout (my_acl);//assign layout to pictures            
              
              //speed up main timer between slides
              
              SlideShow.this.timer.setDelay((int) (TIMER_DELAY / SpeedupFactor * m_iTimeDelay ));
       
              play();
          }
        });//end frwd
        
        
        
        // Whenever the slider value changes, first update the time label.
        // Next, if we're not already at the new position, skip to it.
        progress.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            int value = progress.getValue();
            // Update the time label
            if (midi)
              time.setText(value + "");
            else
              time.setText(value / 1000 + "." + (value % 1000) / 100);
            // If we're not already there, skip there.
            if (value != audioPosition)
              skip(value);
          }
        });

        // This timer calls the tick() method 10 times a second to keep
        // our slider in sync with the music.
        timer = new javax.swing.Timer(100, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick();
          }
        });

        // put those controls in a row
        Box row = Box.createHorizontalBox();
        row.add(play);
        row.add(ffwrd);
        row.add(rwd);
        row.add(progress);
        row.add(time);
        

        // And add them to this component.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(row);

        // Now add additional controls based on the type of the sound
        if (midi)
          addMidiControls();
        else
          addSampledControls();
      }//end constructor of SoundPlayer

      /** Start playing the sound at the current position */
      public void play() {
        if (midi)
          sequencer.start();
        else
          clip.start();
        timer.start();
        play.setText("Stop");
        playing = true;
        
         //+++++++++++++++++++++++ when music is started: start the slide show too 
        SlideShow.this.timer.start(); //access timer in SlideSow inner class
                                     //keyword this is necessary to cure shadowing
        //+++++++++++++++++++++++
      }

      /** Stop playing the sound, but retain the current position */
      public void stop() {
        timer.stop();
        if (midi)
          sequencer.stop();
        else
          clip.stop();
        play.setText("Play");
        playing = false;
        
        //+++++++++++++++++++++++ when music is stopped: stop the slide show too 
        SlideShow.this.timer.stop(); //access timer in SlideSow inner class
                                     //keyword this is necessary to cure shadowing
        //+++++++++++++++++++++++
      }

      /** Stop playing the sound and reset the position to 0 */
      public void reset() {
        stop();
        if (midi)
          sequencer.setTickPosition(0);
        else
          clip.setMicrosecondPosition(0);
        audioPosition = 0;
        progress.setValue(0);
      }

      /** Skip to the specified position */
      public void skip(int position) { // Called when user drags the slider
        if (position < 0 || position > audioLength)
          return;
        audioPosition = position;
        if (midi)
          sequencer.setTickPosition(position);
        else
          clip.setMicrosecondPosition(position * 1000);
        progress.setValue(position); // in case skip() is called from outside
      }

      /** Return the length of the sound in ms or ticks */
      public int getLength() {
        return audioLength;
      }

      // An internal method that updates the progress bar.
      // The Timer object calls it 10 times a second.
      // If the sound has finished, it resets to the beginning
      void tick() {
        if (midi && sequencer.isRunning()) {
          audioPosition = (int) sequencer.getTickPosition();
          progress.setValue(audioPosition);
        } else if (!midi && clip.isActive()) {
          audioPosition = (int) (clip.getMicrosecondPosition() / 1000);
          progress.setValue(audioPosition);
        } else
          reset();
      }

      // For sampled sounds, add sliders to control volume and balance
      void addSampledControls() {
        try {
          FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
          if (gainControl != null)
            this.add(createSlider(gainControl));
        } catch (IllegalArgumentException e) {
          // If MASTER_GAIN volume control is unsupported, just skip it
        }

        try {
          // FloatControl.Type.BALANCE is probably the correct control to
          // use here, but it doesn't work for me, so I use PAN instead.
          FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
          if (panControl != null)
            this.add(createSlider(panControl));
        } catch (IllegalArgumentException e) {
        }
      }

      // Return a JSlider component to manipulate the supplied FloatControl
      // for sampled audio.
      JSlider createSlider(final FloatControl c) {
        if (c == null)
          return null;
        final JSlider s = new JSlider(0, 1000);
        final float min = c.getMinimum();
        final float max = c.getMaximum();
        final float width = max - min;
        float fval = c.getValue();
        s.setValue((int) ((fval - min) / width * 1000));

        java.util.Hashtable labels = new java.util.Hashtable(3);
        labels.put(new Integer(0), new JLabel(c.getMinLabel()));
        labels.put(new Integer(500), new JLabel(c.getMidLabel()));
        labels.put(new Integer(1000), new JLabel(c.getMaxLabel()));
        s.setLabelTable(labels);
        s.setPaintLabels(true);

        s.setBorder(new TitledBorder(c.getType().toString() + " " + c.getUnits()));

        s.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            int i = s.getValue();
            float f = min + (i * width / 1000.0f);
            c.setValue(f);
          }
        });
        return s;
      }

      // For Midi files, create a JSlider to control the tempo,
      // and create JCheckBoxes to mute or solo each MIDI track.
      void addMidiControls() {
        // Add a slider to control the tempo
        final JSlider tempo = new JSlider(50, 200);

        //slow down or speed up music by adjusting * 100 at the end of formula
        tempo.setValue((int) (sequencer.getTempoFactor() * 100));
        tempo.setBorder(new TitledBorder("Tempo Adjustment (%)"));
        java.util.Hashtable labels = new java.util.Hashtable();
        labels.put(new Integer(50), new JLabel("50%"));
        labels.put(new Integer(100), new JLabel("100%"));
        labels.put(new Integer(200), new JLabel("200%"));
        tempo.setLabelTable(labels);
        tempo.setPaintLabels(true);
        // The event listener actually changes the tmpo
        tempo.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            sequencer.setTempoFactor(tempo.getValue() / 100.0f);
          }
        });

    //    display tempo slider
    //    this.add(tempo);

        // Create rows of solo and checkboxes for each track
        Track[] tracks = sequence.getTracks();
        for (int i = 0; i < tracks.length; i++) {
          final int tracknum = i;
          // Two checkboxes per track
          final JCheckBox solo = new JCheckBox("solo");
          final JCheckBox mute = new JCheckBox("mute");
          // The listeners solo or mute the track
          solo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sequencer.setTrackSolo(tracknum, solo.isSelected());
            }
          });
          mute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sequencer.setTrackMute(tracknum, mute.isSelected());
            }
          });

          // Build up a row
          Box box = Box.createHorizontalBox();
          box.add(new JLabel("Track " + tracknum));
          box.add(Box.createHorizontalStrut(10));
          box.add(solo);
          box.add(Box.createHorizontalStrut(10));
          box.add(mute);
          box.add(Box.createHorizontalGlue());
          // And add it to this component
    //      this.add(box);    //not  needed in slideShow project
        }
      }//end addMidiControls()
    }//end inner class SoundPlayer 
        
    //declare soundplayer (inner class object) 
    SlideShow.SoundPlayer soundplayer;
    
    
    
     // creates an SoundPlayer in a Frame and displays it
      public SlideShow.SoundPlayer createSoundPlayer() throws IOException, UnsupportedAudioFileException,
          LineUnavailableException, MidiUnavailableException, InvalidMidiDataException {
        SoundPlayer player;


        //this player can read the following file formats:
        // .mid  .aif  .wav
        // does not work for .au  .rmf


        File file = new File(m_sSoundFile); //User selected sound file that will be played
           


        // Determine whether it is midi or sampled audio
        boolean ismidi;
        try {
          // We discard the return value of this method; we just need to know
          // whether it returns successfully or throws an exception
          MidiSystem.getMidiFileFormat(file);
          ismidi = true;
        } catch (InvalidMidiDataException e) {
          ismidi = false;
        }

        // Create a SoundPlayer object to play the sound.
        player = new SoundPlayer(file, ismidi);

        //+++++++++++++++++++++++start music automatically when player starts
        player.play();
        //+++++++++++++++++++++++

        // Put it in a window and dislay it
        
        JFrame f = new JFrame("Player");
       // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(player, "Center");    
       
        f.setSize (SOUNDPlAYER_DEFAULT_WINDOW_X_SIZE, SOUNDPlAYER_DEFAULT_WINDOW_Y_SIZE);
        f.setLocation(0, 1050);
        f.setUndecorated(true);
    //  f.pack();
        f.setVisible(true);

        return player;
      }//end createSoundPlayer
    
      
        
    //---------------  constructor of SlideShow   
   public SlideShow ()
   {
      super ("Slide Show");
      //setDefaultCloseOperation (EXIT_ON_CLOSE);

      pictures = new JPanel ();
      pictures.setBackground (Color.WHITE);

      acl = new AnimatingCardLayout ();
      acl.setAnimationDuration ((int) (ANIM_DUR / SpeedupFactor));
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
                    
                 // [0]- cross-fade 
                 // [1]- fade-to-black
                 // [2]- fade-to-white
                 // [3]- fade-from-black
                 // [4]- fade-from-white
                 // [5]- slide-up   
                 // [6]- slide-down   
                 // [7]- slide-left   
                 // [8]- slide-right   
                 // [9]- random
                 String temp = (String) m_vTransitionNumber.get(index);
                 int temp2 = Integer.valueOf(temp);
                 if (temp2 != 9)
                 {
                    
                    temp = (String) m_vTransitionNumber.get(index);
                    temp2 = Integer.valueOf(temp);
                    acl.setAnimation (animations [temp2]);
                 }
                 else
                 {                  
                    acl.setAnimation (animations [(int) (Math.random ()* animations.length)]);
                 }

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

      timer = new Timer ((int) (TIMER_DELAY / SpeedupFactor * m_iTimeDelay ), al);
      
      //OpenSlideShow(); //better try to call from main
      
      timer.start ();

      setSize (DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
      setExtendedState(MAXIMIZED_BOTH);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible (true);
      
     
       //create soundplayer object
        try{

             soundplayer = createSoundPlayer();

        }catch(IOException | UnsupportedAudioFileException |
               LineUnavailableException | MidiUnavailableException |
               InvalidMidiDataException ee){
            ee.printStackTrace();
        }
        
        
          
   }//end constructor of SlideShow

   
   
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
    public static void OpenSlideShow() {
      
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
     
     //....................... retrieve m_sImageDir
     m_sSoundFile = props.getProperty( "SoundFile");
     
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
    m_iTimeDelay = Integer.parseInt(props.getProperty( "TimeDelay" ));

       System.out.println("SlideShow: finished retrieving properties from properties object");
     
        System.out.print("integer:  m_iTransitionTypes:  ");
        System.out.printf("%d\n",  m_iTransitionTypes);
       
        System.out.print("string:  m_sImageDir:  ");
        System.out.printf("%s\n",  m_sImageDir);
        
        System.out.print("string:  m_sImageDir:  ");
        System.out.printf("%s\n",  m_sSoundFile);
        
        
    }//end OpenSlideShow()

   public static void main (String [] args)
   { 
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
                          //Load all images listed in vector m_vImageNames to ArrayList images

                          for (Object filePathObject: m_vImageNames)
                          { 
                                   String imageName = (String) filePathObject; //cast nas string
                                  
                                   ImageIcon ii;
                                   ii = new ImageIcon (imageName);
                                   images.add (ii);
                               
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
