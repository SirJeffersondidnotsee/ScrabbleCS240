import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Test {

//small basic program just for testing and debugging small things from the main program
//just now I was using it to figure out how to eliminate bad input errors from what the user types.
public static void main(String args[]) {

String str = "HA RT4$e";
str = str.toLowerCase().replaceAll("[^A-Za-z]+", "");
System.out.println(str);

}

}