import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TME2 {

	public byte[] hash_concat(byte[] hash1, byte[] hash2) {
		byte[] hash = new byte[hash1.length+hash2.length];
		for(int i = 0;i<hash1.length;i++) {
			hash[i] = hash1[i];
		}
		for(int i = hash1.length;i<hash1.length+hash2.length;i++) {
			hash[i] = hash2[i-hash1.length];
		}
		return hash;
	}
	
	public byte[] concatenate(byte[] a, byte[] b) {
        byte[] hash = new byte[a.length + b.length];
        System.arraycopy(a, 0, hash, 0, a.length);
        System.arraycopy(b, 0, hash, a.length, b.length);
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            result = digest.digest(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public boolean isMerkle(Noeud root) {
		if(root.getRight() != null && root.getLeft() != null) {//si on as deux fils, alors on teste la concatenation des deux hash
			byte[] hash_concat = concatenate(root.getLeft().getValue(),root.getRight().getValue());
			if(hash_concat.equals(root.getValue())) {
				return isMerkle(root.getLeft()) && isMerkle(root.getRight());
			}else {
				return false;
			}
		}else {//si on est une feuille, alors on as qu'un hash
			return true;
		}
	}
	
	public Noeud makeMerkle(ArrayList<byte[]> values){
		ArrayList<Noeud> hash_values = new ArrayList<Noeud>();
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		for(byte[] b : values) {
			hash_values.add(new Noeud(digest.digest(b),null,null));			
		}
		return makeTree(hash_values);
	}
	
	public Noeud makeTree(ArrayList<Noeud> nodes){
		@SuppressWarnings("unchecked")
		ArrayList<Noeud> nodes_copy = (ArrayList<Noeud>) nodes.clone();
		while(nodes_copy.size() > 1) {
			ArrayList<Noeud> temp = new ArrayList<Noeud>();
			for(int i = 0;i<nodes.size();i+=2) {
				nodes_copy.get(i).setPos(0);
				nodes_copy.get(i+1).setPos(1);
				Noeud pere = new Noeud(concatenate(nodes_copy.get(i).getValue(),nodes_copy.get(i+1).getValue()),nodes_copy.get(i),nodes_copy.get(i+1));
				temp.add(pere);
				nodes_copy.get(i).setPere(pere);
				nodes_copy.get(i+1).setPere(pere);
			}
			nodes_copy = temp;			
		}
		return nodes_copy.get(0);
	}
	
	public Noeud recherche(byte[] hash, Noeud root) {
		if(root.getValue().equals(hash)) {
			return root;
		}else {
			Noeud rech_left = recherche(hash,root.getLeft());
			Noeud rech_right = recherche(hash, root.getRight());
			if(rech_left != null && rech_right != null) return rech_left;
			if(rech_left != null && rech_right == null) return rech_left;
			if(rech_left == null && rech_right != null) return rech_right;
			if(rech_left == null && rech_right == null) return null;
		}
		return null;
	}
	
	public void witnessCalcul(Noeud element, ArrayList<Noeud> witness){
		if(element.getPere() != null) {
			Noeud left = element.getPere().getLeft();
			Noeud right = element.getPere().getRight();
			witness.add(left);
			witness.add(right);			
			witnessCalcul(element.getPere(),witness);
		}else {
			witness.add(element);
		}
	}
	
	public ArrayList<Noeud> getwitness(byte[] element, Noeud root){
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			hash = digest.digest(element);
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Noeud element_node = recherche(hash, root);
		ArrayList<Noeud> witness = new ArrayList<Noeud>();
		witnessCalcul(element_node,witness);
		return witness;
	}
}
