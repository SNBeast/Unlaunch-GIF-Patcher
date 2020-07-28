import java.io.*;
import java.nio.file.*;

public class UnlaunchPatcher {
	public static final int gifLocation = 0x48f0;
	public static final int maxGifSize = 0x3c70;
	public UnlaunchPatcher (String image, String original, String output) {
		byte[] gif = null;
		byte[] unlaunch = null;
		try {
			gif = Files.readAllBytes(Paths.get(image));
		} catch (IOException e) {
			System.out.println("Error reading image file:");
			e.printStackTrace();
			System.exit(0);
		}
		
		if (!(gif[0] == (byte)'G' && gif[1] == (byte)'I' && gif[2] == (byte)'F' && gif[6] == 0 && gif[7] == 1 && gif[8] == (byte)0xC0 && gif[9] == 0)) {
			System.out.println("Input GIF improperly formatted.");
			System.exit(0);
		}
		if (gif.length > maxGifSize) {
			System.out.println("Input GIF too large.");
			System.exit(0);
		}
		
		try {
			unlaunch = Files.readAllBytes(Paths.get(original));
		} catch (IOException e) {
			System.out.println("Error reading Unlaunch: ");
			e.printStackTrace();
			System.exit(0);
		}
		
		if (unlaunch.length < gifLocation + maxGifSize) {
			System.out.println("'Unlaunch' file improper");
			System.exit(0);
		}
		
		System.arraycopy(gif, 0, unlaunch, gifLocation, gif.length);
		
		File f = new File(output);
		f.delete();
		try {
			f.createNewFile();
			Files.write(Paths.get(output), unlaunch);
		} catch (IOException e) {
			System.out.println("Error writing patched Unlaunch: ");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Patching successful!");
	}
	public static void main (String[] args) {
		if (args.length == 3) {
			new UnlaunchPatcher(args[0], args[1], args[2]);
		}
		else {
			System.out.println("Usage: java -jar UnlaunchGIFPatcher.jar image.gif unlaunch.dsi output.dsi");
		}
	}
}
