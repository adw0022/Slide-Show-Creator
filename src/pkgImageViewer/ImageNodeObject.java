/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgImageViewer;

/**
 *
 * @author Aaron
 */
public class ImageNodeObject {
    
    private String fileName;
    private int transitionNumber;

    public ImageNodeObject(String name, int code)
    {
      this.fileName = name;
      this.transitionNumber = code;
    }
    public String getName()
        {
            return fileName;
        }
    public int getTransition()
        {
            return transitionNumber;
        }

    public void setName(String name)
        {
            this.fileName = name;
        }
    public void setCode(int code)
        {
            this.transitionNumber = code;
        }
}
