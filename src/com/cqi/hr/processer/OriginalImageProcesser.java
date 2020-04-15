package com.cqi.hr.processer;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

public class OriginalImageProcesser implements ImageProcesser{
	private Logger logger = Logger.getLogger(getClass());
	private int imageWidth;
	private int imageHeight;
	
	protected void initImageInfo(BufferedImage image) {
		imageWidth  = image.getWidth();
		imageHeight = image.getHeight();
		logger.info("imageWidth:"+imageWidth+",imageHeight:"+imageHeight);
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		image = beforeProcess(image);
		initImageInfo(image);
		return image;
	}
	
	protected BufferedImage beforeProcess(BufferedImage image) {
		return image;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
}