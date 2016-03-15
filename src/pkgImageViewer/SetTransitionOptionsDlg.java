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
	
	/** Fade-in radio button */
	private JRadioButton m_FadeInRB;
        
        /** Fade-out radio button */
	private JRadioButton m_FadeOutRB;
        
        /** Wipe-up radio button */
	private JRadioButton m_WipeUpRB;
        
        /** Wipe-down radio button */
	private JRadioButton m_WipeDownRB;
        
        /** Wipe-left radio button */
	private JRadioButton m_WipeLeftRB;
        
        /** Wipe-right radio button */
	private JRadioButton m_WipeRightRB;
	
	
	//------------------------------------------------
	/** Default constructor */
	//------------------------------------------------
	public SetTransitionOptionsDlg(Frame owner, boolean modal)
	{
		super(owner, modal); // Call the super class constructor
		// Create the dialog frame
		this.setSize(340, 240);
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
                
                // Fade-in radio button
                m_FadeInRB = new JRadioButton("Fade-In");
		m_FadeInRB.setSize(120, 20);
		m_FadeInRB.setLocation(100, 25);
		m_FadeInRB.setSelected(false);
		m_FadeInRB.setBackground(Color.lightGray);
		this.add(m_FadeInRB);
                
                // Fade-out radio button
                m_FadeOutRB = new JRadioButton("Fade-Out");
		m_FadeOutRB.setSize(120, 20);
		m_FadeOutRB.setLocation(100, 45);
		m_FadeOutRB.setSelected(false);
		m_FadeOutRB.setBackground(Color.lightGray);
		this.add(m_FadeOutRB);
		
                // Wipe-up radio button
                m_WipeUpRB = new JRadioButton("Wipe-Up");
		m_WipeUpRB.setSize(120, 20);
		m_WipeUpRB.setLocation(100, 65);
		m_WipeUpRB.setSelected(false);
		m_WipeUpRB.setBackground(Color.lightGray);
		this.add(m_WipeUpRB);
                
                // Wipe-down radio button
                m_WipeDownRB = new JRadioButton("Wipe-Down");
		m_WipeDownRB.setSize(120, 20);
		m_WipeDownRB.setLocation(100, 85);
		m_WipeDownRB.setSelected(false);
		m_WipeDownRB.setBackground(Color.lightGray);
		this.add(m_WipeDownRB);
                
                // Wipe-left radio button
                m_WipeLeftRB = new JRadioButton("Wipe-Left");
		m_WipeLeftRB.setSize(120, 20);
		m_WipeLeftRB.setLocation(100, 105);
		m_WipeLeftRB.setSelected(false);
		m_WipeLeftRB.setBackground(Color.lightGray);
		this.add(m_WipeLeftRB);
                
                // Wipe-right radio button
                m_WipeRightRB = new JRadioButton("Wipe-Right");
		m_WipeRightRB.setSize(120, 20);
		m_WipeRightRB.setLocation(100, 125);
		m_WipeRightRB.setSelected(false);
		m_WipeRightRB.setBackground(Color.lightGray);
		this.add(m_WipeRightRB);	
		
		
		
		// Create the OK button
		m_OKBtn = new JButton("OK");
		m_OKBtn.setSize(50, 20);
		m_OKBtn.setLocation(75, 170);
		m_OKBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
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
		m_CancelBtn.setLocation(200, 170);
		m_CancelBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
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
		if(m_FadeInRB.isSelected()) return 1;
		else if(m_FadeOutRB.isSelected()) return 2;
                else if(m_WipeUpRB.isSelected()) return 3;
                else if(m_WipeDownRB.isSelected()) return 4;
                else if(m_WipeLeftRB.isSelected()) return 5;
                else if(m_WipeRightRB.isSelected()) return 6;
		else return 7;
	}
	
	
}
