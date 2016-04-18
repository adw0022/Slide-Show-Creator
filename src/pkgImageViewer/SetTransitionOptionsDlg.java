package pkgImageViewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

//=============================================================================
/** Class: SetDisplayOptionsDlg
 *  Purpose: This class implements a modal dialog box to allow the user to
 *     set all the display options for the ImageViewer application.
 *  Author: Dr. Rick Coleman
 *  Date: April 2008
 */
//=============================================================================
public class SetTransitionOptionsDlg extends JDialog
{
	/** OK button */
	private JButton m_OKBtn;
	
	/** Cancel button */
	private JButton m_CancelBtn;

	/** Exit status */
	private int m_iExitStatus;
	
	/** Title font used for large labels */
	public static final Font SysTitleFontB = new Font("SansSerif", Font.BOLD, 16);
	
	/** Label font used for small labels */
	public static final Font SysSmallLabelFontB = new Font("SansSerif", Font.BOLD, 14);	
        
        /** Cross-Fade radio button */
	private JRadioButton m_CrossFadeRB;
        
        /** Fade to Black radio button */
	private JRadioButton m_FadetoBlackRB;
        
        /** Fade to White radio button */
	private JRadioButton m_FadetoWhiteRB;
        
        /** Fade from Black radio button */
	private JRadioButton m_FadefromBlackRB;
        
        /** Fade from White radio button */
	private JRadioButton m_FadefromWhiteRB;
        
        /** Random radio button */
	private JRadioButton m_RandomRB;
        
        /** SlideUp radio button */
	private JRadioButton m_SlideUpRB;
        
        /** SlideDown radio button */
	private JRadioButton m_SlideDownRB;
        
        /** SlideLeft radio button */
	private JRadioButton m_SlideLeftRB;
        
        /** SlideRight radio button */
	private JRadioButton m_SlideRightRB;
        
	
	
	//------------------------------------------------
	/** Default constructor */
	//------------------------------------------------
	public SetTransitionOptionsDlg(Frame owner, boolean modal)
	{
		super(owner, modal); // Call the super class constructor
		// Create the dialog frame
		this.setSize(340, 325);
		// Set the location so that the dialog box pops up centered on the owner
		this.setLocation((owner.getWidth() - this.getWidth()) / 2 , 
				         (owner.getHeight() - this.getHeight())/ 2); 
		this.setTitle("Set View Options");
		this.setLayout(null); // We'll do our own layouts, thank you.
		this.getContentPane().setBackground(Color.lightGray); // Set visible area to light gray
		// Don't let user close dialog with X-button
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Add the label above the check box
		JLabel lbl = new JLabel("Transitions");
		lbl.setSize(100, 20);
		lbl.setFont(SysTitleFontB);
		lbl.setLocation(100,5);
		this.add(lbl);        
                
                // Cross-Fade radio button
                m_CrossFadeRB = new JRadioButton("Cross-Fade");
		m_CrossFadeRB.setSize(120, 20);
		m_CrossFadeRB.setLocation(100, 45);
		m_CrossFadeRB.setSelected(false);
		m_CrossFadeRB.setBackground(Color.lightGray);
		this.add(m_CrossFadeRB);
		
                // Fade to Black radio button
                m_FadetoBlackRB = new JRadioButton("Fade-to-Black");
		m_FadetoBlackRB.setSize(120, 20);
		m_FadetoBlackRB.setLocation(100, 65);
		m_FadetoBlackRB.setSelected(false);
		m_FadetoBlackRB.setBackground(Color.lightGray);
		this.add(m_FadetoBlackRB);
                
                // Fade to White radio button
                m_FadetoWhiteRB = new JRadioButton("Fade-to-White");
		m_FadetoWhiteRB.setSize(120, 20);
		m_FadetoWhiteRB.setLocation(100, 85);
		m_FadetoWhiteRB.setSelected(false);
		m_FadetoWhiteRB.setBackground(Color.lightGray);
		this.add(m_FadetoWhiteRB);
                
                // Fade from Black radio button
                m_FadefromBlackRB = new JRadioButton("Fade-from-Black");
		m_FadefromBlackRB.setSize(120, 20);
		m_FadefromBlackRB.setLocation(100, 105);
		m_FadefromBlackRB.setSelected(false);
		m_FadefromBlackRB.setBackground(Color.lightGray);
		this.add(m_FadefromBlackRB);
                
                // Fade from White radio button
                m_FadefromWhiteRB = new JRadioButton("Fade-from-White");
		m_FadefromWhiteRB.setSize(120, 20);
		m_FadefromWhiteRB.setLocation(100, 125);
		m_FadefromWhiteRB.setSelected(false);
		m_FadefromWhiteRB.setBackground(Color.lightGray);
		this.add(m_FadefromWhiteRB);	               
                
                // SlideUp radio button
                m_SlideUpRB = new JRadioButton("Slide-Up");
		m_SlideUpRB.setSize(120, 20);
		m_SlideUpRB.setLocation(100, 145);
		m_SlideUpRB.setSelected(false);
		m_SlideUpRB.setBackground(Color.lightGray);
		this.add(m_SlideUpRB);
                
                // SlideDown radio button
                m_SlideDownRB = new JRadioButton("Slide-Down");
		m_SlideDownRB.setSize(120, 20);
		m_SlideDownRB.setLocation(100, 165);
		m_SlideDownRB.setSelected(false);
		m_SlideDownRB.setBackground(Color.lightGray);
		this.add(m_SlideDownRB);
                
                // SlideLeft radio button
                m_SlideLeftRB = new JRadioButton("Slide-Left");
		m_SlideLeftRB.setSize(120, 20);
		m_SlideLeftRB.setLocation(100, 185);
		m_SlideLeftRB.setSelected(false);
		m_SlideLeftRB.setBackground(Color.lightGray);
		this.add(m_SlideLeftRB);
                
                // SlideRight radio button
                m_SlideRightRB = new JRadioButton("Slide-Right");
		m_SlideRightRB.setSize(120, 20);
		m_SlideRightRB.setLocation(100, 205);
		m_SlideRightRB.setSelected(false);
		m_SlideRightRB.setBackground(Color.lightGray);
		this.add(m_SlideRightRB);
                
                // Random radio button
                m_RandomRB = new JRadioButton("Random");
		m_RandomRB.setSize(120, 20);
		m_RandomRB.setLocation(100, 225);
		m_RandomRB.setSelected(false);
		m_RandomRB.setBackground(Color.lightGray);
		this.add(m_RandomRB);	
                
                // Create the OK button
		m_OKBtn = new JButton("OK");
		m_OKBtn.setSize(50, 20);
		m_OKBtn.setLocation(75, 250);
		m_OKBtn.setBorder(BorderFactory.createCompoundBorder());
		m_OKBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle OK
						m_iExitStatus = 0; // Normal exit
						setVisible(false);
					}
				});
		this.add(m_OKBtn);	
		
		// Create the Cancel button
		m_CancelBtn = new JButton("Cancel");
		m_CancelBtn.setSize(50, 20);
		m_CancelBtn.setLocation(200, 250);
		m_CancelBtn.setBorder(BorderFactory.createCompoundBorder());
		m_CancelBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle cancel
						m_iExitStatus = 1;
						setVisible(false);
					}
				});
		this.add(m_CancelBtn);	
		
	}    
    
	
	//------------------------------------------------
	/** Get the exit status */
	//------------------------------------------------
	public int getExitStatus()
	{
		return m_iExitStatus;
	}
	
	

	//------------------------------------------------
	/** Get the show types setting */
	//------------------------------------------------
	public int getTransitionTypes()
	{
		// Return which radio button is selected		
		if(m_CrossFadeRB.isSelected()) return 0;
                else if(m_FadetoBlackRB.isSelected()) return 1;
                else if(m_FadetoWhiteRB.isSelected()) return 2;
                else if(m_FadefromBlackRB.isSelected()) return 3;
                else if(m_FadefromWhiteRB.isSelected()) return 4;
                else if(m_SlideUpRB.isSelected()) return 5;
                else if(m_SlideDownRB.isSelected()) return 6;
                else if(m_SlideLeftRB.isSelected()) return 7;
                else if(m_SlideRightRB.isSelected()) return 8;                
                else if(m_RandomRB.isSelected()) return 9;
		else return 9;
	}
	
	
}
