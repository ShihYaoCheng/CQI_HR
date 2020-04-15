package com.cqi.hr.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;


public class LineChart {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private BufferedImage image;
	private int[] Y;
	
	public LineChart(int[] point) {
		this.Y = point;
	}
	
	private Image loadImage(InputStream in) throws IOException {
		try {
			if(in == null){
				logger.info("image in is null");
			}
			return ImageIO.read(in);
		} catch(Exception e){
			logger.error("loadImage error: ", e);
			return null;
		}finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public void create(String userId, int width, int height) throws IOException {
		create(userId,width,height,null,null);
	}
	
	public void create(String userId, int width, int height, String fileUrl,String fileName) throws IOException {
		int X=50;
		int[] convertY = new int[Y.length];
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		Image bgImg = loadImage(this.getClass().getResourceAsStream("/tw/com/ebti/web/util/resources/Line.gif"));
		
		if(bgImg != null){
			boolean b = g.drawImage(bgImg, 0, 0, width, height, null);
			//System.out.println(b);
			//g.setColor(Color.YELLOW);
			//g.fillRect(0, 0, image.getWidth(), image.getHeight());
			
			for(int i=0; i<Y.length; i++){
				convertY[i] = convertMathY(Y[i]);
			}
			
			for(int i=0; i<Y.length-1; i++){
				g.setColor(Color.RED);
				g.setStroke(new BasicStroke(1.5F));
				g.drawLine(X, convertY[i], (X+50), convertY[i+1]);
				X+=50;
			}
			
			g.dispose();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(fileUrl==null){
				File f = new File("C:/temp/"+userId+"_"+sdf.format(new Date())+".jpg");
				ImageIO.write(getImage(), "JPEG", f);
			}
			else{
				File f = new File(fileUrl+"/"+fileName+".jpg");
				ImageIO.write(getImage(), "JPEG", f);
			}
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	private int convertMathY(int y) {
		return image.getHeight()-y;
	}
}
