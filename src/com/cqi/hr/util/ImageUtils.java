package com.cqi.hr.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.cqi.hr.processer.ImageProcesser;
import com.cqi.hr.processer.OriginalImageProcesser;
import com.cqi.hr.processer.WaterFallImageProcesser;

@SuppressWarnings("unused")
public class ImageUtils {
	public static BufferedImage scaleImageFixedWidth(BufferedImage originalImage, int scaleWidth) {
		double rate = 1D*scaleWidth/originalImage.getWidth();
		int scaleHeight = (int)Math.round(originalImage.getHeight()*rate);
		return scaleImage(originalImage, scaleWidth, scaleHeight);
	}
	public static BufferedImage scaleImageFixedHeight(BufferedImage originalImage, int scaleHeight) {
		double rate = 1D*scaleHeight/originalImage.getHeight();
		int scaleWidth = (int)Math.round(originalImage.getWidth()*rate);
		return scaleImage(originalImage, scaleWidth, scaleHeight);
	}
	public static BufferedImage scaleImage(BufferedImage originalImage, int scaleWidth, int scaleHeight) {
		int width = originalImage.getWidth(),
			height = originalImage.getHeight();
		AffineTransform tx = new AffineTransform();
		tx.scale(1.0*scaleWidth/width, 1.0*scaleHeight/height);
		AffineTransformOp txOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return txOp.filter(originalImage, new BufferedImage(scaleWidth, scaleHeight, originalImage.getType()==BufferedImage.TYPE_CUSTOM?BufferedImage.TYPE_BYTE_INDEXED:originalImage.getType()));
	}
	public static final int ROTATION_DIRECTION_CLOCKWISE = 100;
	public static final int ROTATION_DIRECTION_ANTICLOCKWISE = 200;

	private static double getDegree90SinValue(double degree) {
		return degree==90?1D:degree==270?-1D:0D;
	}
	
	private static double getDegree90CosValue(double degree) {
		return degree==90?-1D:degree==270?1D:0D;
	}
	
	
	public static AffineTransform getRotateAffineTransform(int rotationDirection, double degree, int cx, int cy) {
		AffineTransform tx = null;
		if(rotationDirection==ROTATION_DIRECTION_CLOCKWISE) {
			tx = new AffineTransform(0D, 1D, -1D, 0D, cy, 0D);
		} else if(rotationDirection==ROTATION_DIRECTION_ANTICLOCKWISE) {
			tx = new AffineTransform(0D, -1D, 1D, 0D, 0D, cx);
		} else {}
		return tx;
	}
	
	/**
	 * 目前只支援90、180度
	 * @param originalImage
	 * @param degree
	 * @return
	 */
	public static BufferedImage rotationImage(int rotationDirection, BufferedImage originalImage, double degree) {
		BufferedImage result = null;
		AffineTransform tx = null;
		if(rotationDirection==ROTATION_DIRECTION_CLOCKWISE) {
			tx = new AffineTransform(0D, 1D, -1D, 0D, originalImage.getHeight(), 0D);
		} else if(rotationDirection==ROTATION_DIRECTION_ANTICLOCKWISE) {
			tx = new AffineTransform(0D, -1D, 1D, 0D, 0D, originalImage.getWidth());
		} else {}
		if(tx!=null) {
			AffineTransformOp txOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			result = txOp.filter(originalImage, null);
		} else {
			result = originalImage;
		}
		return result;
	}
	public static BufferedImage rotationTextImage(BufferedImage originalImage, int degree) {
		BufferedImage result = null;
		AffineTransform tx = null;
		if(degree==90 || degree==-270) {
			tx = new AffineTransform(0D, 1D, -1D, 0D, originalImage.getHeight(), 0D);
		} else if(degree==270 || degree==-90) {
			tx = new AffineTransform(0D, -1D, 1D, 0D, 0D, originalImage.getWidth());
		} else if(degree==180 || degree==-180) {
			tx = new AffineTransform(-1D, 0D, 0D, -1D, originalImage.getWidth(), originalImage.getHeight());
		} else {}
		if(tx!=null) {
			AffineTransformOp txOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			result = txOp.filter(originalImage, null);
		} else {
			result = originalImage;
		}
		return result;
	}
	public static BufferedImage convertByteArrayToBufferedImage(byte[] b) {
		BufferedImage img = null;
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	public static byte[] convertBufferedImageToByteArray(RenderedImage image, String format) {
		byte[] b = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, format, out);
			b = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public static String imageUpload(MultipartFile file,String imageName, String directory, String picPath) {
		String downloadFilePath = null;
		//縮圖
		byte[] datas = null;
		OriginalImageProcesser imageProcesser = new WaterFallImageProcesser(200, 200);
		
		if(file!=null && file.getSize()>0) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = file.getInputStream();
				//取得使用者專用目錄
				File uploadFileFolder = new File(directory + "/");
				if(!uploadFileFolder.exists()) uploadFileFolder.mkdirs();
				if(uploadFileFolder.exists()) {
					
					//如果做刪除動作, 在上傳失敗時, 使用者連之前的照片也看不見了, 所以不刪除
					//delUserIcon(userId);
					int dotIndex = file.getOriginalFilename().lastIndexOf(".");
					String extFileName = file.getOriginalFilename().substring(dotIndex+1).toLowerCase();
					String fileName = imageName + "."+ extFileName;
					out = new FileOutputStream(new File(uploadFileFolder, fileName));
					downloadFilePath =  picPath + "/" + directory + "/" + fileName;
					int len;
					byte[] b = file.getBytes();
					while ((len=in.read(b))!=-1) {
						//不存原始圖檔
						//out.write(b, 0, len);
						//out.flush();
						datas = b;
					}
					//儲存壓縮的圖
					saveImageToServer(datas, file.getContentType(), uploadFileFolder.getPath(), fileName, imageProcesser);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return downloadFilePath;
	}
	
	private static boolean saveImageToServer(byte[] imageDatas, String mimeType, String newFilePath, String newFileName, ImageProcesser imageProcesser) {
		//MIME-TYPE
		if(mimeType==null) return false;
        if("image/pjpeg".equals(mimeType) || "image/jpg".equals(mimeType)) {
        	mimeType = "image/jpeg";
        } else if("image/x-png".equals(mimeType)) {
        	mimeType = "image/png";
        } else {
        	//Nothing, you know.
        }

        
        //Find image reader/writer
        ImageReader reader = null;
		ImageWriter writer = null;
		{			
			Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType(mimeType);
			if(it.hasNext()) {
				reader = it.next();
			}
		}
		{
			Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(mimeType);
			if(it.hasNext()) {
				writer = it.next();
			}			
		}
		if(reader==null || writer==null) return false;
		
		
		//I/O
		ImageInputStream  imgIn  = null;
		ImageOutputStream imgOut = null;
		try {
			imgIn = ImageIO.createImageInputStream(new ByteArrayInputStream(imageDatas));
			reader.setInput(imgIn, false, false);
			BufferedImage srcImg = reader.read(0);
			IIOMetadata imageMetadata = reader.getImageMetadata(0);
			if(imageProcesser!=null) {
				srcImg = imageProcesser.process(srcImg);
			}
			
			imgOut = new MemoryCacheImageOutputStream(new FileOutputStream(newFilePath + "/" + newFileName));
			ImageWriteParam iwp;
			if("image/jpeg".equals(mimeType)) {
				JPEGImageWriteParam writerParam = new JPEGImageWriteParam(null);
				writerParam.setOptimizeHuffmanTables(true);
				writerParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				writerParam.setCompressionQuality(1F);
				iwp = writerParam;
			} else {
				iwp = writer.getDefaultWriteParam();
			}
			
			writer.setOutput(imgOut);
			writer.write(imageMetadata, new IIOImage(srcImg, null, imageMetadata), iwp);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			reader.dispose();
			writer.dispose();
			try {
				imgIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				imgOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return false;
	}
}
