package LE7Q2;

import LE7Q1.*;
import java.util.Scanner;

import static LE7Q1.TylerDemoHashingWithLinearProbing.*;

public class TylerDemoHashingWithAllOpenAddressingTechniques
{
    public static void addValueDoubleHashing(Integer n)
    {
        items++;

        // We won't deal with rehashing with double hashing
        // if (items >= tableCapacity * lf) {reHashingWithLinearProbe();}

        // Calculated index with an extra reference
        int i = n % tableCapacity;

        // Java operation % has output i : -n < i < n
        // n < 0 implies i < 0 implies that we need to correct this to i > 0 to get a valid table index
        if (i < 0) {i += tableCapacity;}

        //Extra reference to i
        int oldI = i;

        // Second hash
        int q = thePrimeNumberForSecondHashFunction(tableCapacity);
        int probe = q - (n % q); // ensures the probe isn't zero
        int j = 0;

        // Handle Collision by finding next null or available spot according to the double hashing technique
        while (hashTable[i] != null && hashTable[i].getKey() != -111)
        {
            i = (oldI + probe * j) % tableCapacity;
            if (i < 0) {i += tableCapacity;} // same adjustment as above
            j++;
        }

        // Insert value into hash table
        hashTable[i] = new TylerValueEntry(n);
    }

    // Add a value to the hash table using the quadratic probing technique
    public static void addValueQuadraticProbing(Integer n)
    {
        items++;

        // We won't deal with rehashing with quadratic probing
        // if (items >= tableCapacity * lf) {reHashingWithLinearProbe();}

        // Calculated index with an extra reference
        int i = n % tableCapacity;
        int oldI = i;

        // Java operation % has output i : -n < i < n
        // n < 0 implies i < 0 implies that we need to correct this to i > 0 to get a valid table index
        if (i < 0) {i += tableCapacity;}

        // The amount to increment the index by during a collision
        int m = 1;

        // We need to count how many iterations we probe since with quadratic probing
        // When lf > 0.5 we cannot guarantee finding a spot
        int c = 0;
        boolean found = true;

        // Handle Collision by finding next null or available spot quadratically
        while (hashTable[i] != null && hashTable[i].getKey() != -111)
        {
            i = (oldI + m * m) % tableCapacity;
            m++; c++;

            // If we have tried probing too many times there's no spot
            if (c >= tableCapacity)
            {
                found = false;
                break;
            }
        }

        // Insert value into hash table if a spot was found (we'll only fail possibly if lf > 0.5)
        if (found)
            hashTable[i] = new TylerValueEntry(n);
        else
            System.out.println("Probing failed! Use a load factor of 0.5 or less. Goodbye!");
    }

    // Print the string representation of an Integer[]
    public static void printArray(Integer[] arr)
    {
        // Build array from here
        StringBuilder s = new StringBuilder("[");

        // Iterate over the hashtable
        for (Integer integer : arr)
        {
            s.append(integer);

            // Formatting
            s.append(", ");
        }

        // Print formatted hash table
        s.append("\b\b]");
        System.out.println(s);
    }

    // Reset the hashtable so that all values are null
    public static void emptyHashTable()
    {
        items = 0;

        for (int i = 0; i < tableCapacity; i++)
        {
            hashTable[i] = null;
        }
    }

    public static int thePrimeNumberForSecondHashFunction(int n)
    {
        n -= 2; // next least n value (can skip even numbers, input n is tableCapacity which is always prime)

        // We just need to check half of the n factors
        int m = n / 2;

        // Check factors
        for (int i = 3; i <= m; i++)
        {
            // If n is not a prime number
            if (n % i == 0)
            {
                i = 2; // reset i to 2 so that it is incremented back to 3 before next step
                n -= 2; // next n to check
                m = n / 2; // reset m for next n
            }
        }

        return n;
    }

    public static void main(String[] args)
    {
        myHeader(7, 2);

        // Instantiate the scanner
        input = new Scanner(System.in);

        // User Input for items and lf
        System.out.println("Let's decide on the initial table capacity based on the load factor and dataset size");

        System.out.print("How many data items: ");
        int n = input.nextInt();
        tableCapacity = n; // At first, we'll naively set table capacity to the number of items
        input.nextLine();

        System.out.print("What is the load factor (Recommended: <= 0.5): ");
        lf = input.nextDouble();
        input.nextLine();

        // Get the initial table capacity as the smallest prime such that we aren't over the load factor
        while (n >= tableCapacity * lf)
        {
            tableCapacity = checkPrime(tableCapacity + 1);
        }
        System.out.println("The minimum required table capacity would be: " + tableCapacity);

        // Instantiate the hash table
        hashTable = new TylerValueEntry[tableCapacity];

        // Instantiate and show the data set
        Integer[] numbers = {7, 14, -21, -28, 35};
        System.out.println("The given dataset is:"); printArray(numbers);
        System.out.println("Let's enter each data item from the above to the hash table:\n");

        // Demo Linear probing
        System.out.println("Adding data - linear probing resolves collision");
        for (Integer k : numbers)
        {
            addValueLinearProbe(k);
        }
        System.out.println("The Current Hash-Table:"); printHashTable();
        emptyHashTable();

        // Demo quadratic probing
        System.out.println("\nAdding data - quadratic probing resolves collision");
        for (Integer k : numbers)
        {
            addValueQuadraticProbing(k);
        }
        System.out.println("The Current Hash-Table:"); printHashTable();
        emptyHashTable();

        // Demo double-hashing
        System.out.println("\nAdding data - double hashing resolves collision");
        for (Integer k : numbers)
        {
            addValueDoubleHashing(k);
        }
        System.out.println("The q-value for double hashing is: " + thePrimeNumberForSecondHashFunction(tableCapacity));
        System.out.println("The Current Hash-Table:"); printHashTable();
        emptyHashTable();

        myFooter(7, 2);
    }
}
