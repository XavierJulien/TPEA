
public class Noeud {
	private byte[] value;
	private Noeud left,right,pere;
	private int pos;
	
	public Noeud(byte[] value ,Noeud l,Noeud r) {
		this.value = value;
		this.left = l;
		this.right = r;
		this.pere = null;
	}
	
	public byte[] getValue() {return value;}
	
	public void setValue(byte[] value) {this.value = value;}
	public Noeud getLeft() {return left;}
	public void setLeft(Noeud left) {this.left = left;}
	public Noeud getRight() {return right;}
	public void setRight(Noeud right) {this.right = right;}
	public int getPos() {return pos;}
	public void setPos(int pos) {this.pos = pos;}
	public Noeud getPere() {return pere;}
	public void setPere(Noeud pere) {this.pere = pere;}
}
