package pkgImageViewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

//============================================================================
/** This class implements the panel on which all images are drawn */
//===========================================================================
public class ImagePanel extends JPanel
{
	/** the parent JFrame */
	private JFrame m_Parent;

	/** The image to draw*/
	private Image theImage = null;
	
	//--------------------------------------------------------
	/** Default constructor */
	//--------------------------------------------------------
	public ImagePanel(JFrame parent)
	{            
		m_Parent = parent; 
                m_Parent.setExtendedState(m_Parent.MAXIMIZED_BOTH); 
                m_Parent.setVisible(true);
		this.setSize(m_Parent.getSize().width-50, 
				m_Parent.getSize().height-100);   // Set the size 
		this.setLocation(20, 15);       // Set the location in the window
		this.setBackground(Color.lightGray); // Set the panel color
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Give it a border
		this.setLayout(null); // No layout manager.  We�ll place everything                
	}
	
	//--------------------------------------------------------
	/** Set the reference to the image to be drawn. */
	//--------------------------------------------------------
	public void setImage(Image img)
	{
		theImage = img;
	}
	
	//--------------------------------------------------------
	/** Override the paint function to draw the image. */
	//--------------------------------------------------------
	public void paint(Graphics g)
	{
		this.paintComponent(g);
		this.paintBorder(g);
		// Prepare to draw the image
		if(theImage == null) return; // Don't try to draw if we don't have one

        // Note: If the image is too large we try to scale it
        int imgWidth = theImage.getWidth(this);
        int imgHeight = theImage.getHeight(this);
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        int posX, posY; // Position to place upper-left corner of image
        int imgScaleWidth, imgScaleHeight; // In case we need to scale
        // Set the upper left position of the image
        if(imgWidth <  panelWidth)
        	posX = (panelWidth - imgWidth) / 2;
        else
        	posX = 0;
        if(imgHeight < panelHeight)
        	posY = (panelHeight - imgHeight) / 2;
        else
        	posY = 0;
        // See if we need to scale it
        if((posX == 0) || (posY == 0))
        {
        	if(imgWidth > imgHeight) // Scale by width
        	{
        		imgScaleWidth = panelWidth;
        		imgScaleHeight = (int)Math.round(((double)panelWidth / (double)imgWidth) * 
        				(double)imgHeight);
        	}
        	else // Scale by height
        	{
        		imgScaleHeight = panelHeight;
        		imgScaleWidth = (int)Math.round(((double)panelHeight / (double)imgHeight) * 
        				(double)imgWidth);
        	}
        }
        else // Draw it normal size
        {
    		imgScaleWidth = imgWidth;
    		imgScaleHeight = imgHeight;
        }
        // Draw the scaled image
		g.drawImage(theImage, posX, posY, imgScaleWidth, imgScaleHeight, this);
	}
}
