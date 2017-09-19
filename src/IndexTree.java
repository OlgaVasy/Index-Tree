import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class IndexTree {

   protected static class Node {

      protected String data;
      protected int count; // count of occurrences
      protected ArrayList<String> lineNumbers;
      protected Node left;
      protected Node right;

      // node constructor
      public Node(String data,int count,ArrayList<String> lineNumbers) {
         this.data = data;
         this.count=count;
         this.lineNumbers=lineNumbers;
      }
   }
   protected Node root; //root of the tree

   //no-parameter constructor
   public IndexTree(){
      root = null;
   }

   // recursive add method
   public Node add(Node localRoot, String data,int count,ArrayList<String>lineNumbers) {

      if (localRoot == null) {
         return new Node(data, count, lineNumbers);
      }
       if (data.compareTo(localRoot.data) < 0) {
         if (localRoot.left != null) {
            add(localRoot.left, data, count, lineNumbers);
         } else {
            localRoot.left = new Node(data, count, lineNumbers);
         }      }
      else if (data.compareTo(localRoot.data) > 0) {
         if (localRoot.right != null) {
            add(localRoot.right, data, count, lineNumbers);
         } else {
            localRoot.right = new Node(data, count, lineNumbers);
         }
      }
      return localRoot;
      }

   public void run() {

      // choose a file
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
      chooser.setFileFilter(filter);
      chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
      int result = chooser.showOpenDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
         File file = chooser.getSelectedFile();
         System.out.println("The selected file: " + file.getAbsolutePath());
         System.out.println();

      try{    //open the file
         Scanner scan = new Scanner(new FileInputStream(file)).useDelimiter(",|-|\\s");

         Map<String, Integer> index = new TreeMap<>(); //the word indexes
         Map<String, Integer> occurrences = new TreeMap<>(); //the word occurrences
         ArrayList<ArrayList<String>> lineNumbers = new ArrayList<>(); // multidimensional arraylist of the line numbers
         int lineNumber=1;  //the line number
         int tokenN=0;   //the token number

         while (scan.hasNext()) {

            String nextToken = scan.next().toLowerCase(); // read the next token

           // remove the punctuation marks from the token
            String token="";
            for (Character c : nextToken.toCharArray()) {
               if(Character.isLetterOrDigit(c))
                  token += c;}

                  if(token!=""){      //determine if the token contains characters after removing the punctuation marks
                     if (!occurrences.containsKey(token)) {
                        occurrences.put(token, 1); //the first occurrence of the token
                        lineNumbers.add(new ArrayList<>());
                        lineNumbers.get(tokenN).add("" + lineNumber); //add the line number to the arrayList
                        index.put(token, tokenN);  //add new index
                        tokenN++;
                   } else {
                        occurrences.put(token, occurrences.get(token) + 1);
                        int number = index.get(token);  //get the index of the existing token
                        lineNumbers.get(number).add(" " + lineNumber); //add the line number to the arrayList
            }}

              if (nextToken.contains(".")) { // indication of end-of-line
                  lineNumber++;
              }
            }
            ArrayList<String> myList = new ArrayList<>();
            for (String word : occurrences.keySet()) {
               int count = occurrences.get(word); //count of occurrences of the word
               int tokenNumber = index.get(word); //the index of the word
               myList = lineNumbers.get(tokenNumber); //the line numbers for the word
               //add data for the word
               if (root == null) {
                  root = new Node(word, count, myList);
               } else {
                  add(root, word, count, myList);
               }
            }
            print(root);
          }catch(Exception e){}
   }}

   public void print(Node root) {
      if (root != null) {
         print(root.left);
         System.out.println("'" + root.data + "', count of occurrences: " + root.count +", line numbers: " +root.lineNumbers);
         print(root.right);
      }
   }
   public static void main(String[] args) {
      IndexTree myTree=new IndexTree();
      myTree.run();
   }


}