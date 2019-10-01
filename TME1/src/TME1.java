import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TME1 {
	
	//===========================UTIL================================
    private static String bytesToHex1(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashInBytes.length; i++) {
            sb.append(Integer.toString((hashInBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }
    
    public static String toBitString(final byte[] b) {
        final char[] bits = new char[8 * b.length];
        for(int i = 0; i < b.length; i++) {
            final byte byteval = b[i];
            int bytei = i << 3;
            int mask = 0x1;
            for(int j = 7; j >= 0; j--) {
                final int bitval = byteval & mask;
                if(bitval == 0) {
                    bits[bytei + j] = '0';
                } else {
                    bits[bytei + j] = '1';
                }
                mask <<= 1;
            }
        }
        return String.valueOf(bits);
      }
	
    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }
    
    
    //===========================TP================================
	public static byte[] hash_id(String nom, String prenom) {
		byte[] hash = new byte[256];
		byte[] input = (nom+":"+prenom).getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hash_prefix = new byte[8];
		for(int i = 0 ;i < 8 ; i++) {
			hash_prefix[i] = hash[i];
		}
		return hash_prefix;
	}

	public static byte[] hash_value(byte[] huit_octet,int entier) {
		byte[] hash = new byte[256];
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String entToBits = Integer.toBinaryString(entier);
			System.out.println("entier : "+entToBits);
			String huitoctetBits = toBitString(huit_octet);
			System.out.println("huit octet : "+bytesToHex1(huit_octet));
			System.out.println("huit octet en binaire : "+huitoctetBits);
			System.out.println(""+huitoctetBits+entToBits);
			hash = md.digest((huitoctetBits+entToBits).getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public static int count_zero_prefix(String input) {
		int cpt = 0;
		for(char c : input.toCharArray()) {
			if(c == '0') {
				cpt++;
			}else {
				break;
			}
		}
		return cpt;
	}
	
	public static boolean is_valid(String nom,String prenom,int nounce,int difficulte) {
		return count_zero_prefix(hexToBinary(bytesToHex1(hash_value(hash_id(nom,prenom),nounce)))) >= difficulte;
	}
	
	public static int mine(String nom,String prenom,int difficulte) {
		int nounce = 0;
		while (!is_valid(nom,prenom,nounce,difficulte)) {
			System.out.println(nounce);
			nounce++;
		}
		return nounce;
	}
	
	public static void main(String[] args) {	
		String s = bytesToHex1(hash_id("koff", "jean-pierre"));
		System.out.println(s);
		String s2 = bytesToHex1(hash_value(hash_id("koff", "jean-pierre"),32));
		System.out.println(s2);
		String s3 = hexToBinary(bytesToHex1(hash_value(hash_id("koff", "jean-pierre"),32)));
		System.out.println(s3);
		//System.out.println(mine("koff", "jean-pierre",15));
	}
}
