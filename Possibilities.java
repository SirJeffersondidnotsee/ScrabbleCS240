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
        System.out.println("What is the filename for your dictionary?('example.txt')");
        String input = in.nextLine();
        
        
        File file = new File(input);            
        
        // creates an empty array the size of the dictionary
        String[] Dict = new String[fileSize(input)];
        
        //Arraylist for storing words pre-scoring
        List<String> words = new ArrayList<>();
        
        //Arraylist for storing scored words for sorting
         List<Score> scoredWords = new ArrayList<>();
                    
        // Adds the words from the file to the initial dictionary array
        populate(Dict, input);
       
        // Root Node of Trie
        TrieNode root = new TrieNode();
       
        // insert all words of dictionary array into trie
        createDictionary(Dict, root);
           
        System.out.println("Enter the letters in your Scrabble hand:");
        System.out.println("**all input besides upper or lowercase letters will be ignored");

        String input2 = in.nextLine();
        
        char Hand[] = createHand(input2);
        int z = Hand.length;
        
        System.out.println("Thank you!");
        System.out.println("Here are the words you can make with those letters in descending order:");

                  
        beginSearchWords(Hand, root, z, scoredWords, words);
        convertToScore(words,scoredWords);
        printScoredWords(scoredWords);
        
        
    }
    
    static void createDictionary(String[] Dict, TrieNode root) {
        int n = Dict.length;
        for (int i=0; i<n; i++) {
            insert(root, Dict[i]);
        }
    }
    
    // Fill scoredWords arraylist from words, then sorts the arraylist by score
    static void convertToScore(List<String> words, List<Score> scoredWords) {
        
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);
            scoredWords.add(new Score(word, i, scrabbleScore(word)));
        }
        
        Collections.sort(scoredWords);
    }
    
    //simple method for printing the scored words at the end of the program
    static void printScoredWords(List<Score> scoredWords) {
    
        for (Score scoredWord : scoredWords) { 
               
            System.out.println(scoredWord.toString());
        }
    }
    
    //creates the char array which holds the letters for the hand of scrabble so that
    //it can be created with user input.   
    static char[] createHand(String userLetters) {
    
        userLetters = userLetters.replaceAll("[^A-Za-z]+", "");
        
        String[] preLetters = userLetters.toLowerCase().split("\\W+");
       
        String postLetters = new String();
            
        for(int i = 0; i < preLetters.length;i++){
            postLetters = postLetters + preLetters[i];
        }

        char newHand[] = postLetters.toCharArray();    
        return newHand;
    }        
      
    //method which starts searching for words based off letters in the given hand, then
    //passes off to a recursive function which finishes the search and prints the word
    static void beginSearchWords(char hand[], TrieNode root,int handSize, List<Score> scoredWords, List<String> words) {
        
        //added an int array to represent the count vs the boolean array to eliminate
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
        for (int i = 0 ; i < SIZE ; i++) {
        
           //if the letter is in our hand, and in the dictionary
            if (count[i] > 0 && current.Child[i] != null ) {
                str = str+(char)(i + 'a');
                count[i]--;
                searchWord(hand, current.Child[i], str, count, scoredWords, words);
                str = "";
            }
        }
    }
    
    // A recursive function to print all possible valid
    // words present in array
    static void searchWord(char hand[], TrieNode current,String str, int counter[], List<Score> scoredWords, List<String> words) {
    

        // base case which outputs a word when we have reached the end of a word
        if (current.endOfWord == true) {
            
            //replacing old simple print function with a storing function to try to add scoring
            words.add(str);  
        }
        
        // checks the children of the current node
        for (int K = 0; K < SIZE; K++) {
            //if the letter is in the hand & part of a word in the dictionary
            if (counter[K] > 0 && current.Child[K] != null) {
                // add current character
                char c = (char) (K + 'a');
                
                counter[K]--;
                
                // Recursive search for remaining characters in word
                searchWord(hand, current.Child[K], str + c, counter, scoredWords, words);
                
                //adds the letters back into the count array as it recurses back
                //through itself to maintain the proper count while searching thoroughly
                counter[K]++;
    
            }
        }
        
    }
    
    
    //Small method for returning a word's scrabble score.
   static int scrabbleScore(String word) {
        int score = 0;
        int scrabbleScoreTable[] = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z') {
                score += scrabbleScoreTable[word.charAt(i) - 'a'];
            }
            else {
             System.out.println("error in word input recursed from dictionary trie.");   // error in input 
            }
        }return score;
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
            System.out.println("An error occurred. That file wasn't found in the project folder.");
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

//score class with variables for efficiently storing a string word, an int score, and an int for ordering.
class Score implements Comparable<Score> {
    public Score(String word, int order, int score) {
        this.word = word;        
        this.order = order;
        this.score = score;
    }
    
    public Score(String word, int score) {
        this.word = word;
        this.score = score;
    }    

    public String   getWord() { return word; }    
    public int      getScore() { return score; }
    public int      getOrder() { return order; } 

    @Override   // Overridden sorting rule for sorting words by score in descending order
    public int      compareTo(Score other) {
        int result = other.getScore() - score;
        return result == 0 ? order - other.getOrder() : result;
    }

    @Override  //overridden toString() method for printing out the score with the word
    public String   toString() {
        return word + " -score:" + score;
    }
    
    private String word;    // the string for the word
    private int order;      // the word's order in the final word list
    private int score;      // the word's Scrabble score
}