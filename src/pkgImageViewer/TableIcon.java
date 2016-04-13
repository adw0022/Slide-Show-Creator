/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgImageViewer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.table.*;

/**
 *
 * @author sydney
 */

    
  
public class TableIcon
{
    ImageIcon[] icons;
  
    public TableIcon(BufferedImage[] source)
    {
        icons = getScaledIcons(source);
    }
  
    public JTable getTable()
    {
        
        int i =0;
        int setBit = 0;
        //int rows = 4;
        int cols = 4;
        int rows =(int)((icons.length/cols)+1);
        
        String[] colNames = { " ", " ", " ", " " };
        
        Object[][] data = new Object[rows][cols];
                 
        
        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < cols; col++)
            {
                if(i<icons.length)
                {

                    int count = (row*cols +col);
                    data[row][col] = icons[count];
                    i++;
  
                }
                
                if (i==icons.length)
                    break;
            }
        if (i==icons.length)
                    break;}
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        
        final JTable table = new JTable(model)
        {
            public boolean isCellEditable(int row, int col) //{ return col != 2; }
            {return false;}
         
        };
        table.setRowHeight(100);
        table.setCellSelectionEnabled(true);

        
table.addMouseListener(new MouseAdapter() {
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      JTable target = (JTable)e.getSource();
      int row = target.getSelectedRow();
      int column = target.getSelectedColumn();
      // do some action if appropriate column
    }
  }
});      


        for(int n=0 ;n<rows ;n++)
            for(int m=0; m<cols ;m++)
        {
            TableColumn col = table.getColumnModel().getColumn(m);
            col.setCellRenderer(new CustomRenderer());
        }
        
        return table;
    }
  
    private ImageIcon[] getScaledIcons(BufferedImage[] in)
    {
        int w = 128;
        int h = 72;
        ImageIcon[] icons = new ImageIcon[in.length];
        for(int j = 0; j < in.length; j++)
        {
           
            BufferedImage bi = new BufferedImage(w, h, in[j].getType());
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setPaint(UIManager.getColor("Table.background"));
            g2.fillRect(0,0,w,h);
            double xScale = (double)w / in[j].getWidth();
            double yScale = (double)h / in[j].getHeight();
            double scale = Math.min(xScale, yScale);    // scale to fit
            double x = (w - scale*in[j].getWidth())/2;
            double y = (h - scale*in[j].getHeight())/2;
            AffineTransform at = AffineTransform.getTranslateInstance(x,y);
            at.scale(scale, scale);
            g2.drawRenderedImage(in[j], at);
            g2.dispose();
            icons[j] = new ImageIcon(bi);
        }
        return icons;
    }
  
  
}
  
class CustomRenderer extends DefaultTableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column)
    {
        JLabel label = (JLabel)super.getTableCellRendererComponent(table,
                                   value, isSelected, hasFocus, row, column);
        ImageIcon icon = (ImageIcon)value;
        label.setIcon(icon);
        label.setText("");
        return label;
    }
}

