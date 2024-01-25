package LE7Q1;

import java.util.Scanner;

public class TylerDemoHashingWithLinearProbing
{
    // Fields
    public static int items ;
    public static Scanner input;
    public static double lf; // load factor
    public static int tableCapacity;
    public static TylerValueEntry[] hashTable;
    // public static TylerValueEntry[] workingWithHashTable; didn't end up using this?

    // Rehash the table with new size double the old size, rounded up to the nearest prime
    public static void reHashingWithLinearProbe()
    {
        // Temp references
        int oldCapacity = tableCapacity;
        TylerValueEntry[] oldTable = hashTable;
        int oldItems = items;

        // New hash table capacity is the next prime after double the current capacity
        tableCapacity = checkPrime(2 * tableCapacity);
        hashTable = new TylerValueEntry[tableCapacity];
        items = 0; // Since addValueLinearProbe increments items, we need to reset this, so we don't rehash again

        // Rehashing from temp copy
        for (int i = 0; i < oldCapacity; i++)
        {
            // Skip null and available values
            if (oldTable[i] != null && oldTable[i].getKey() != -111)
            {
                addValueLinearProbe(oldTable[i].getKey());
            }
        }

        // Set the number of items back to what it was before rehashing (rehashing doesn't change this)
        items = oldItems;

        // User info
        System.out.println("Rehashing the table......");
        System.out.println("The rehashed capacity is: " + tableCapacity);
        System.out.println("The Current Hash-Table: "); printHashTable();
    }

    // Locate and remove n, by marking it available (key: -111)
    // Return value is if the value was actually in the hash table
    public static void removeValueLinearProbe(Integer n)
    {
        // Calculated index
        int i = n % tableCapacity;

        // Java operation % has output i : -n < i < n
        // n < 0 implies i < 0 implies that we need to correct this to i > 0 to get a valid table index
        if (i < 0) {i += tableCapacity;}

        // Counter will ensure we don't look out more than the size of the table
        int c = 0;

        // Flag for when we find the value
        boolean found = false;

        // We only need to search until the next index that has never been populated
        // Working (mod tableCapacity) so that we wrap around
        while (hashTable[i] != null && c < tableCapacity)
        {
            // Stop checking if we find the target
            if (hashTable[i].getKey() == n)
            {
                found = true;
                break;
            }

            i = (i + 1) % tableCapacity;
            c++;
        }

        // Mark the location available and update the number of items if we found the value
        // Provide user info in either case
        if (found)
        {
            items--;
            hashTable[i] = new TylerValueEntry(-111);
            System.out.println(n + " is found and removed! The Current Hash-Table: ");
            printHashTable();
        }
        else
        {
            System.out.println(n + " is not found! The Current Hash-Table: ");
            printHashTable();
        }
    }

    // Print the string representation of the hash table
    public static void printHashTable()
    {
        // Build array from here
        StringBuilder s = new StringBuilder("[");

        // Iterate over the hashtable
        for (int i = 0; i < tableCapacity; i++)
        {
            // Catch null and available slots
            if (hashTable[i] == null) {s.append("null");}
            else if(hashTable[i].getKey() == -111) {s.append("available");}
            else {s.append(hashTable[i].getKey());}

            // Formatting
            s.append(", ");
        }

        // Print formatted hash table
        s.append("\b\b]");
        System.out.println(s);
    }

    // Header
    public static void myHeader(int labE_number, int q_number)
    {
        System.out.println("=======================================================");
        System.out.printf("Lab Exercise: %d-Q%d%n", labE_number, q_number);
        System.out.println("Prepared by: Tyler Liquornik");
        System.out.println("Student Number: 251271244");
        System.out.printf("Goal of this exercise: %s%n", "Open Addressing Collision Handling in Hashing");
        System.out.println("=======================================================");
    }

    // Get prime p : 2 < n <= p
    public static int checkPrime(int n)
    {
        // We just need to check half of the n factors
        int m = n / 2;

        // Check factors
        for (int i = 3; i <= m; i++)
        {
            // If n is not a prime number
            if (n % i == 0)
            {
                i = 2; // reset i to 2 so that it is incremented back to 3 before next step
                n++; // next n value
                m = n / 2; // reset m for next n
            }
        }

        return n;
    }

    // Add values to the hash table with linear probing collision handling
    public static void addValueLinearProbe(Integer n)
    {
        items++;

        // Update number of items and rehash if necessary
        if (items >= tableCapacity * lf) {reHashingWithLinearProbe();}

        // Calculated index
        int i = n % tableCapacity;

        // Java operation % has output i : -n < i < n
        // n < 0 implies i < 0 implies that we need to correct this to i > 0 to get a valid table index
        if (i < 0) {i += tableCapacity;}

        // Handle Collision by finding next null or available spot
        while (hashTable[i] != null && hashTable[i].getKey() != -111) {i = (i + 1) % tableCapacity;}

        // Insert value into hash table
        hashTable[i] = new TylerValueEntry(n);
    }

    // Footer
    public static void myFooter(int labE_number, int q_number)
    {
        System.out.println("=======================================================");
        System.out.printf("Completion of Lab Exercise %d-Q%d is successful!%n", labE_number, q_number);
        System.out.println("Signing off - Tyler");
        System.out.println("=======================================================");
    }

    // Driver method
    public static void main(String[] args)
    {
        myHeader(7, 1);

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

        // Get data from user, populating hashtable with it
        for (int i = 1; i <= n; i++)
        {
            System.out.print("Enter item " + i + ": ");
            addValueLinearProbe(input.nextInt());
            input.nextLine();
        }

        System.out.println("The Current Hash-Table: "); printHashTable();

        // Demo for removing two values, then adding one value
        System.out.println("Let’s remove two values from the table and then add one.......");

        System.out.print("\nEnter a value you want to remove: ");
        removeValueLinearProbe(input.nextInt());
        input.nextLine();

        System.out.print("\nEnter a value you want to remove: ");
        removeValueLinearProbe(input.nextInt());
        input.nextLine();

        System.out.print("\nEnter a value to add to the table: ");
        addValueLinearProbe(input.nextInt());
        input.nextLine();
        System.out.println("The Current Hash-Table: "); printHashTable();

        // Demo for rehashing
        System.out.println();
        reHashingWithLinearProbe();

        myFooter(7, 1);
    }
}
