import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
 
// Java program to print all valid words that can be made from a scrabble hand
public class Possibilities {
       
    // Constant for alphabet size
    static final int SIZE = 26;
       
    // trie Node class
    static class TrieNode {
        TrieNode[] Child = new TrieNode[SIZE];
       
        //Boolean for storing whether or not the node is the end of a word
        boolean endOfWord;
          
        // Constructor
        public TrieNode() {
            endOfWord = false;
            for (int i =0 ; i< SIZE ; i++)
                    Child[i] = null;
        }
    }
    
    //main method for testing
    public static void main(String args[]) {
    
        Scanner in = new Scanner(System.in);
        System.out.println("What is the filename?");
        String input = in.nextLine();
        File file = new File(input);      
       
        
        // creates an empty array the size of the dictionary
        String[] Dict = new String[fileSize(input)];
        
        // Adds the words from the file to the array
        populate(Dict, input);
       
        // Root Node of Trie
        TrieNode root = new TrieNode();
       
        // insert all words of dictionary into trie
        int n = Dict.length;
        for (int i=0; i<n; i++)
            insert(root, Dict[i]);
       
        char Hand[] = {'a', 'i', 's', 'n', 'h', 'm', 'n'} ;
        int N = Hand.length;
               
        PrintAllWords(Hand, root, N);
    }
    
    // method which starts searching for words based off letters in the given hand, then
    //passes off to a recursive function which finishes the search and prints the word
    static void PrintAllWords(char hand[], TrieNode root,int handSize) {
    
        // create a 'has' boolean array that will store all
        // present characters in Hand[]
        boolean[] Has = new boolean[SIZE];
       
        for (int i = 0 ; i < handSize; i++) {
            Has[hand[i] - 'a'] = true;
        }
        //starting to add in an int array to represent the count vs the boolean array to eliminate
        //reused letters
        int[] count = new int[SIZE];
        
        for (int j = 0; j < handSize; j++) {
            count[hand[j] - 'a']++;
        }

        TrieNode current = root ;
       
        // string to hold output words
        String str = "";
       
        // Checks every letter, if any is in hand and the dictionary, adds letter
        //to the string, then calls recursive funtion to finish search and print, then resets string
        for (int i = 0 ; i < SIZE ; i++)
        {
           \//if the letter is in our hand, and in the dictionary
            if (Has[i] == true && current.Child[i] != null )
            {
                str = str+(char)(i + 'a');
                //System.out.println("2"); was using for testing

                searchWord(current.Child[i], Has, str, count);
                str = "";
            }
        }
    }
    
        // A recursive function to print all possible valid
    // words present in array
    static void searchWord(TrieNode current, boolean Has[],String str, int counter[]) {

        // base case which outputs a word when we have reached the end of a word
        if (current.endOfWord == true) {
            System.out.println(str);
            //System.out.println("1");
        }
        // checks the children of the current node assigned as 'root'
        for (int K = 0; K < SIZE; K++) {

            if (Has[K] == true && current.Child[K] != null) {
                // add current character
                char c = (char) (K + 'a');

                // Recursively search for remaining characters in word
                searchWord(current.Child[K], Has, str + c, counter);
            }
        }
    }
    
    //simple method for getting size of dictionary so we know what size the array
    //that will store all the words before they are moved into the trie will need to be.
    static int fileSize(String filename) { 
    
    char ch='"';
    int dictSize = 0;
  
     try {
      File name = new File(filename);
      Scanner myReader = new Scanner(name);  
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        dictSize++;
        }
      myReader.close();
      } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }return dictSize;   
    }
    
    //method which scans a text file and creates a string array of all the words in seperate
    //elements which will be taken in during the creation of our trie dictionary.
    static void populate(String[] array, String filename) {

        int j = 0;

        try {
        File name = new File(filename);
        Scanner myReader = new Scanner(name);  
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            data = data.toLowerCase();
            array[j] = data;
            j++;
            //add "data" to the next array element
            }
        myReader.close();
        } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }   
    }
    
    // If prefix is new, inserts a new trie node
    //when letter from word(key) is present, moves to the next child and rescans
    //when word(key) is finished, marks the last letter
    static void insert(TrieNode root, String Key) {
        int n = Key.length();
        TrieNode current = root;
       
        for (int i=0; i<n; i++) {
            int index = Key.charAt(i) - 'a';
       
            if (current.Child[index] == null) {
                current.Child[index] = new TrieNode();
                }
            current = current.Child[index];
        }
        // mark last node as end of word
        current.endOfWord = true;
    }
   
}