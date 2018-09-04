import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DifBin {
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(int i) {
	    char[] hexChars = new char[2];
        int v = i & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
	    return new String(hexChars);
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("need 2 files as parameter");
			System.exit(1);
		}
		
		File f1 = new File(args[0]);
		
		if (!f1.exists()) {
			System.err.println("file doesn't exist: " + f1);
			System.exit(1);
		}
		
		File f2 = new File(args[1]);
		
		if (!f2.exists()) {
			System.err.println("file doesn't exist: " + f2);
			System.exit(1);
		}
		
		long pos = 0;
		try (InputStream is1 = new BufferedInputStream(new FileInputStream(f1)); InputStream is2 = new BufferedInputStream(new FileInputStream(f2))) {
			int r1 = is1.read();
			int r2 = is2.read();
			
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			
			while (r1 != -1 && r2 != -1) {
				if (r1 != r2) {
					if (sb1.length() == 0) {
						System.out.print("@" + pos + "-");
					}
					sb1.append(bytesToHex(r1)).append(' ');
					sb2.append(bytesToHex(r2)).append(' ');
				}
				
				if (sb1.length() > 0 && (r1 == r2 || sb1.length() >= 30)) {
					System.out.println(pos + ": " + sb1.toString() + "| " + sb2.toString());
					sb1.setLength(0);
					sb2.setLength(0);
				}
				r1 = is1.read();
				r2 = is2.read();
				pos++;
			}
			if (r1 != r2) {
				if (r1 < r2) {
					System.out.println("left file end before right file at " + pos);
				} else {
					System.out.println("right file end before left file at " + pos);
				}
			}
		}
	}

}
