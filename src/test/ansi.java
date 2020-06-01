package test;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class ansi {
      
    public static void main(String[] args) throws IOException, URISyntaxException {
       
        BufferedImage img = ImageIO.read(new FileInputStream(new File("src/assets/img/sprites/006_Méga_X.png")));     
        
        int X=img.getWidth();
        int Y=img.getHeight();
        Color[][] pxls = new Color[X][Y];
        
        for(int x=0;x<X;x++) {
            for(int y=0;y<Y;y++) {
                pxls[x][y]=new Color(img.getRGB(x,y), true);
            }
        }
        for(Color[] row:pxls) {
            for(Color pxl:row) {
                //(pxl>>24)&0xff)
                System.out.print("\033[39;2;"+(pxl.getRed())+";"+(pxl.getGreen())+";"+(pxl.getBlue())+"m██\033[0m");
            }
            System.out.println("");
        }
        Color clr = pxls[20][14];
        System.out.println(clr.getTransparency()+":"+clr.getRed()+":"+clr.getGreen()+":"+clr.getBlue());
    }
    
}
