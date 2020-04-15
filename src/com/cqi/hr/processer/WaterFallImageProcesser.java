package com.cqi.hr.processer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

import com.cqi.hr.util.ImageUtils;

public class WaterFallImageProcesser extends OriginalImageProcesser{
	private int scaleWidth;
	private int scaleHeight;
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public WaterFallImageProcesser(int scaleWidth, int scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;
	}
	
	@Override
	public BufferedImage beforeProcess(BufferedImage image) {
		//double rateWH = image.getWidth()/image.getHeight();
		//double rateScaleWH = scaleWidth/scaleHeight;
		//Width is better than height.
		if(image.getWidth()<image.getHeight()) {
			logger.info("image.getWidth()>image.getHeight()");
			image = ImageUtils.scaleImageFixedHeight(image, scaleHeight);
		//Height is better than width.
		} else {
			logger.info("image.getWidth()<=image.getHeight()");
			image = ImageUtils.scaleImageFixedWidth(image, scaleWidth);
		}
		logger.info("imageWidth: " + image.getWidth() + " imageHeight: " + image.getHeight());
		BufferedImage cropedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics g = cropedImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		return cropedImage;
	}
}