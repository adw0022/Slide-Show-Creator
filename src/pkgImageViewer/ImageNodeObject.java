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

    public ImageNodeObject(String name, int code) {
      this.fileName = name;
      this.transitionNumber = code;
   }
}
