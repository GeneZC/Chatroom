package chatroom;

import java.awt.Graphics;  
import java.awt.GraphicsConfiguration;  
import java.awt.GraphicsDevice;  
import java.awt.GraphicsEnvironment;  
import java.awt.HeadlessException;  
import java.awt.Image;  
import java.awt.Transparency;  
import java.awt.image.BufferedImage;  

  
import javax.swing.ImageIcon;  
import javax.swing.JPanel;
import javax.swing.JFrame;
  
public  class ImagePanel extends JPanel
{
	//private static final long serialVersionUID=-7411632419492480055L;
	private Image image;


	public ImagePanel(Image image) {  
		this.image=image;
		this.setOpaque(false);
	}



	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		if (image != null) {
			int width=getWidth();
			int height=getHeight();
			g.drawImage(image, 0,0,width,height,this);
		}
	}

    //test passes!
    public static void main(String arg[])
    {
    	Image image = new ImageIcon("Resource/test.jpg").getImage();
    	ImagePanel panel = new ImagePanel(image);
    	panel.setLayout(null);
    	JFrame frame = new JFrame();
    	frame.add(panel);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
    	frame.setLocation(350, 120);   
    	frame.setSize(750, 560);   
    	frame.setVisible(true);
    }
}  
