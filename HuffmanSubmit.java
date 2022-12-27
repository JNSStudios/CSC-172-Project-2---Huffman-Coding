import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

// Import any package as required


public class HuffmanSubmit implements Huffman {
  
	// Feel free to add more methods and variables as required. 

	// USED TO MAKE THE TREE, USING NODES THAT CONTAIN THE NECESSARY DATA OR FREQUENCIES
	public class HuffmanNode {
		private HuffmanNode left;
		private HuffmanNode right;
		private Integer frequency;
		private Character data;

		// blank constructor
		public HuffmanNode(){
			this.left = null;
			this.right = null;
			this.frequency = null;
			this.data = null;
		}
		// leaf constructor
		public HuffmanNode(Character d, Integer f){
			this.left = null;
			this.right = null;
			this.frequency = f;
			this.data = d;
		}
		// vertex constructor
		public HuffmanNode(Integer f, HuffmanNode l, HuffmanNode r){
			this.left = l;
			this.right = r;
			this.frequency = f;
			this.data = null;
		}
		
		// getters
		public HuffmanNode getLeft(){return this.left;}
		public HuffmanNode getRight(){return this.right;}
		public Integer getFrequency(){return this.frequency;}
		public Character getData(){return this.data;}

		// setters
		public void setLeft(HuffmanNode l){this.left = l;}
		public void setRight(HuffmanNode r){this.right = r;}
		public void setFrequency(Integer f){this.frequency = f;}
		public void setData(Character d){this.data = d;}
		
		// check methods
		public boolean isLeaf(){ return (this.left == null && this.right == null);}

		// debug print methods
		public void printSubtree(){
			System.out.println(this.toString());
			if(this.getLeft() != null){
				System.out.println("Left: ");
				this.getLeft().printSubtree();
			}
			if(this.getRight() != null){
				System.out.println("Right: ");
				this.getRight().printSubtree();
			}
		}

		public void debugMessage(){
			String str = "I ";
			if(this.getData() == null)
				str += "am a VERTEX node with ";
			else 
				str += "contain the character \'" + this.getData() + "\', and I have ";
			str += "a frequency of " + this.getFrequency() + ".\n";
			if(this.isLeaf())
				str += "I am a leaf node.\n";
			else {
				str += "My left child ";
				if(this.getLeft().getData() == null)
					str += "is a VERTEX node with ";
				else 
					str += "contains the character \'" + this.getLeft().getData() + "\', and has ";
				str += "a frequency of " + this.getLeft().getFrequency() + ". ";
				str += "My right child ";
				if(this.getRight().getData() == null)
					str += "is a VERTEX node with ";
				else 
					str += "contains the character \'" + this.getRight().getData() + "\', and has ";
				str += "a frequency of " + this.getRight().getFrequency() + ". ";
			}
			System.out.println(str +"\n");
		}

		@Override
		public String toString(){			
			return "(\'" + this.getData() + "\', " + this.getFrequency() + ")";
		}
	}
	// done with assistance from Zach (aka thechonkypenguin#4934) on the CSUG Tutoring Discord server
	public class HuffmanNodeComparator implements Comparator<HuffmanNode> {
		public int compare(HuffmanNode a, HuffmanNode b){
			if(a.getFrequency() == b.getFrequency()){
				if(a.getData() != null && b.getData() != null){
					return a.getData() - b.getData();
				} else {
					return 0;
				}
			} else {
				return a.getFrequency() - b.getFrequency();
			}
		}	
	}

	// this will construct a root node that contains a Huffman tree sorted from treeNodes.
	public HuffmanNode constructTree(PriorityQueue<HuffmanNode> treeNodes){
		while(true){
			// Pulls the first node from the head of the priority queue
			HuffmanNode first = treeNodes.poll();
			// If the queue is empty now, return that node, since it is the root of the constructed tree
			if(treeNodes.isEmpty())
				return first;
			// pull the second one
			HuffmanNode second = treeNodes.poll();
			// make a new node with the two pulled nodes as children.
			HuffmanNode parent = new HuffmanNode(first.getFrequency() + second.getFrequency(), first, second);
			// parent.debugMessage();
			// add that summed node back into the queue
			treeNodes.add(parent);
		}
	}
	// This will construct a hashtable that contains characters in the tree as keys, and Strings that contain binary denoting the path necessary to reach the character in the key.
	// Ex: Character "c" is reached with binary path "001010"
	// NOTE: This method is recursive and returns a table containing all character paths
	public Hashtable<Character, String>  binaryPath(HuffmanNode node, String path, Hashtable<Character, String> table ){
		// left fork, 0. right fork, 1.
		if(node.isLeaf()){
			table.put(node.getData(), path);
			// System.out.println("Character found! " + node.getData() + " at path " + path + ". saving...\nTable is now " +table.toString() );
			return table;
		} else {
			// System.out.println("At a vertex.");
		}
		// System.out.println("Checking left subtree...");
		table = binaryPath(node.getLeft(), path+"0", table);
		// System.out.println("Left subtree checked. Checking right...");
		
		table = binaryPath(node.getRight(), path+"1", table);
		// System.out.println("Right subtree checked. Returning up...\n");
		return table;
	}


	public void encode(String inputFile, String outputFile, String freqFile){
		BinaryIn in = new BinaryIn(inputFile);
		ArrayList<Character> inputChars = new ArrayList<Character>();
		// create an arraylist to store all the binaryin data as a character series.
		while(!in.isEmpty())
			inputChars.add((Character) in.readChar());

		// now we have all the character divvied up into each index.
		// using this, we now insert a hash table where we add up the values of each
		Hashtable<Character, Integer> freqList = new Hashtable<Character, Integer>();

		for(Character c : inputChars){
			if(!freqList.containsKey(c))
				freqList.put(c,1);
			else
				freqList.replace(c, freqList.get(c), freqList.get(c)+1);
			// System.out.println("ENCODE PASS\n"+freqList);
		}
				
		// System.out.println("FINAL ENCODE PASS ON CHARS:\n"+freqList);
		// System.out.println(freqList.toString());

		// now we have each character with their frequencies next to them!
		// now sort em, and also make the frequency file while you're at it.
		PriorityQueue<HuffmanNode> treeNodes = new PriorityQueue<HuffmanNode>(freqList.keySet().size()+1, new HuffmanNodeComparator());
		String freqFileText = "";
		
		for(Character c : freqList.keySet()){
			// System.out.println("ADDING TO TREE: " + c);
			treeNodes.add(new HuffmanNode(c, freqList.get(c)));
		}

		for(HuffmanNode n : treeNodes){
			// System.out.println("READING FROM TREE: " + n);
			String bin = Integer.toBinaryString(n.getData());
			while(bin.length() != 8)
				bin = "0" + bin;
			freqFileText += bin + ":" + freqList.get(n.getData()) + "\n";
		}
		
		// we will write the frequency file later, after the tree is made and the encoding is done.
	
		// System.out.println("During encoding: " + treeNodes.toString());
		
		HuffmanNode huffman = constructTree(treeNodes);
		// WE NOW HAVE THE HUFFMAN TREE!!
		// huffman.printSubtree();

		Hashtable<Character, String> conv = binaryPath(huffman, "", new Hashtable<Character, String>());

		// System.out.println(conv.toString());

		BinaryOut out = new BinaryOut(outputFile);
		int bitsWritten = 0;
		for(Character c : inputChars){
			String bin = conv.get(c);
			for(Character b : bin.toCharArray()){
				if(b == '0'){
					out.write(false);
				} else if(b == '1'){
					out.write(true);
				}
				bitsWritten++;
			}
		}
		freqFileText += bitsWritten;
		// System.out.println(debugPrint);
		
		out.close();
		
		// this will automatically write the binary data to the encoded file.
		
		// now save the frequency file!
		try{
			FileWriter frFi = new FileWriter(freqFile);
			frFi.write(freqFileText);
			frFi.close();
		}catch(Exception E){
			System.out.println("Unable to save frequency file " + freqFile + ". :(");
		}
   }


   public void decode(String inputFile, String outputFile, String freqFile){
		// start by reconstructing the huffman tree from the frequency file
		Scanner scanner;
		try{
			scanner = new Scanner(new File(freqFile));
		}catch(Exception e){
			System.out.println("Cannot open file " + freqFile + ". :(");
			return;
		}
		
		Hashtable<Character, Integer> freqList = new Hashtable<Character, Integer>();
		int bitsWritten = 0;
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			// System.out.println(line);
			if(line.indexOf(":") == -1){
				bitsWritten = Integer.parseInt(line);
			} else {
				Character ch = (char) Integer.parseInt(line.substring(0, line.indexOf(":")), 2);
				Integer frq = Integer.parseInt(line.substring(line.indexOf(":")+1, line.length()));
				// System.out.println("\'" + ch + "\'   " + frq);
				freqList.put(ch, frq);
				// System.out.println("DECODE PASS\n"+freqList);
			}
		}
		scanner.close();
		// we've reassembled the frequency list!
		// System.out.println("FINAL ENCODE PASS ON CHARS:\n"+freqList);

		PriorityQueue<HuffmanNode> treeNodes = new PriorityQueue<HuffmanNode>(freqList.keySet().size(), new HuffmanNodeComparator());
		
		// System.out.println("\n" + freqList.toString());
		for(Character c : freqList.keySet()){
			// System.out.println(c);
			treeNodes.add(new HuffmanNode(c, freqList.get(c)));
		}
		// System.out.println("\nDuring decoding: " + treeNodes.toString());

		HuffmanNode root = constructTree(treeNodes);
		// huffman tree reassembled
		
		// root.printSubtree();

		

		BinaryIn in = new BinaryIn(inputFile);
		BinaryOut out = new BinaryOut(outputFile);
		HuffmanNode curr = root;

		for(int i = 0; i <= bitsWritten; i++){
			if(in.isEmpty())
				break;
			// System.out.println(i + "   " + bitsWritten);
			if(curr.isLeaf()){ 	
				// System.out.print("LEAF!!  \'" + curr.getData() + "\'\n");
				out.write(curr.getData());
				curr = root;
				i--;
				continue;
			}
			boolean isOne = in.readBoolean();
			// System.out.print("Currently reading a " +  (isOne ? 1 :0) + "   ");
			if(!isOne){
				// System.out.print("Going left.\n");
				curr = curr.getLeft();
			} else {
				// System.out.print("Going right.\n");
				curr = curr.getRight();
			}
		}
		out.close();
		// done!

   }



   public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();

		huffman.encode("ur.jpg", "ur.enc", "freqUR.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freqUR.txt");

		
		huffman.encode("alice30.txt", "alice30.enc", "freqAlice.txt");
		huffman.decode("alice30.enc", "alice30dec.txt", "freqAlice.txt");

		// used for debugging the program on the short text sample "Back 2 Back"
		// huffman.encode("shorttest.txt", "shorttest.enc", "freqTEST.txt");
		// huffman.decode("shorttest.enc", "shorttestDEC.txt", "freqTEST.txt");

		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
