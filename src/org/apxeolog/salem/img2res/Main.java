package org.apxeolog.salem.img2res;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

public class Main {

	/**
	 * @param args
	 */
	
	private static final String havenSignature = "Haven Resource 1";
	private static final String imageSignature = "image";
	
	public static byte[] int2byte(int arg) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(arg).array();
	}
	
	public static byte[] img2res(File image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(new byte[] { 0, 0 });					// Z = 0
		bos.write(new byte[] { 0, 0 });					// Sub Z = 0
		bos.write(0);									// Flag 1 = false
		bos.write(new byte[] { 0, 0 });					// ID = 0
		bos.write(new byte[] { 0, 0, 0, 0 });			// Offset Coord = (0, 0)
		ImageIO.write(ImageIO.read(image), "PNG", bos);
		return bos.toByteArray();
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("No input file!");
			return;
		}
		File input = new File((String) args[0]);
		if (!input.exists() || !input.canRead()){
			System.out.println("No such file!");
			return;
		}
		File output = new File(input.getName().substring(0, input.getName().lastIndexOf('.')) + ".res");
		try {
			FileOutputStream fos = new FileOutputStream(output);
			fos.write(havenSignature.getBytes()); 	// Signature
			fos.write(new byte[]{ 1, 0 });			// Version
			fos.write(imageSignature.getBytes());	// Image layer signature
			fos.write(0);							// End of image layer signature
			byte[] image = img2res(input);
			fos.write(int2byte(image.length));
			fos.write(image);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
