import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class GameProcesses {
    private Node head;
    private Node tail;
    private int size;


    public GameProcesses(String filename) {
        this.head = null;
        this.tail = null;
        this.size = 0;

        fillLinkedList(filename);
    }


    public void fillLinkedList(String filename) {
        Scanner userInput = new Scanner(System.in);

        // Count the number of lines (challengers) in the file
        int totalChallengers = countLinesInFile(filename);

        // Ask the user for the number of challengers
        System.out.println("How many challengers do you want? (2-" + totalChallengers + "): ");
        int num_of_challengers = userInput.nextInt();

        // Validate user input: ensure the number of challengers is within range
        while (num_of_challengers <= 0 || num_of_challengers > totalChallengers) {
            System.out.println("Invalid number of challengers! Please enter a number between 1 and " + totalChallengers + ": ");
            num_of_challengers = userInput.nextInt();
        }

        // Create an array to store the challengers from the file
        String[] challengers = new String[totalChallengers];

        // Read challengers from the file
        try {
            FileReader file = new FileReader(filename);
            Scanner input = new Scanner(file);
            int index = 0;
            while (input.hasNextLine()) {
                challengers[index++] = input.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }

        Random rand = new Random();
        int count = 0;

        // Randomly select challengers until we have the required number
        while (count < num_of_challengers) {
            int randomIndex = rand.nextInt(totalChallengers); // Select a random challenger
            String selectedChallenger = challengers[randomIndex];

            // Check for duplicate challengers
            if (!isDuplicate(selectedChallenger)) {
                if (!selectedChallenger.contains(",")) {
                    System.out.println("Invalid challenger format, skipping: " + selectedChallenger);
                    continue;
                }

                String[] split = selectedChallenger.split(",", 2);
                if (split.length != 2) {
                    System.out.println("Invalid challenger format, skipping: " + selectedChallenger);
                    continue;
                }

                String name = split[0];  // Challenger's name
                String type = split[1];  // Challenger's type

                // Create a new node for the challenger and append it to the list
                Node newNode = new Node(name, type);
                append(newNode, size + 1); // Add node to the list
                count++;
            }
        }

        printLinkedList();
    }

    private int countLinesInFile(String filename) {// counts all the lines in the file
        int lineCount = 0;
        try {
            FileReader file = new FileReader(filename);
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                lineCount++;
                input.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
        return lineCount;
    }

    //checks to see if the challenger is a dupe
    private boolean isDuplicate(String challenger) {
        if (head == null) {
            return false; // No challengers in the list
        }
        Node current = head;
        do {
            // Compare challenger details with the current node
            if ((current.getName() + "," + current.getType()).equals(challenger)) {
                return true;
            }
            current = current.getNext();
        } while (current != head);
        return false; //No duplicates found
    }

    //Method to append a new node to the linked list
    public void append(Node newNode, int spot) {
        // Case 1: Linked list is empty
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
            size++;
        }
        // Case 2: Insert at the start (new head)
        else if(spot == 1) {
            head.setPrev(newNode);
            newNode.setNext(head);
            head = newNode;
            size++;
        }
        // Case 3: Insert at the end (new tail)
        else if(spot == size + 1) {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
            size++;
        }
        // Case 4: Insert in the middle of the list
        else {
            Node current = head;
            // Traverse to the desired spot
            for(int i = 0; i < spot - 1; i++) {
                current = current.getNext();
            }
            Node prevNode = current.getPrev();
            prevNode.setNext(newNode);
            newNode.setPrev(prevNode);
            newNode.setNext(current);
            current.setPrev(newNode);
        }
        // Reattach head and tail to maintain the circular structure
        head.setPrev(tail);
        tail.setNext(head);
    }

    // Method to print the entire list
    public void printLinkedList() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node current = head;
        System.out.println("Challengers remaining ");
        System.out.println("+-------------------+");
        do {
            // Print each challenger's name and type
            System.out.println(current.getName() + " | Class: " + current.getType());
            System.out.println("");

            current = current.getNext(); // Move to the next node
        } while (current != head); // Continue until loops back to the head

        System.out.println("+--------------------+");
        System.out.println("Would you like to continue with the following challengers?(y/n): ");

        Scanner userInput = new Scanner(System.in);
        String answer = userInput.nextLine().toLowerCase(); //scans for user input
        if (answer.equals("y")) { //if the user says yes, then it will simulate another round, if anything else is entered the program will end
            simulateOneRound();
        }
        else{
            System.out.println("Good bye!");
            System.exit(0);
        }

    }

    private void simulateOneRound() {
        Random rand = new Random();
        int j, k;

        // Pick the first random index
        j = rand.nextInt(size) + 1;

        // Pick the second index, ensuring it's different from j
        do {
            k = rand.nextInt(size) + 1;
        } while (k == j);

        Node first = head;
        for (int i = 1; i < j; i++) { // picks the first node out of the list
            first = first.getNext();
        }

        Node second = tail;
        for (int i = 1; i < k; i++) { // picks the second item out of the list
            second = second.getPrev();
        }

        System.out.println("j: " + j + ", k: " + k);
        System.out.println("+---------------------+");
        System.out.println("Challengers selected:");
        System.out.println(first.getName() + ", class: " + first.getType());
        System.out.println("VS.");
        System.out.println(second.getName() + ", class: " + second.getType());
        System.out.println("+---------------------+");

        Node winner, loser;

        if ((first.getType().equals("Warrior") && second.getType().equals("Mage")) || // checks to see if the first node picked won anything
                (first.getType().equals("Mage") && second.getType().equals("Rogue")) ||
                (first.getType().equals("Rogue") && second.getType().equals("Warrior"))) {
            winner = first;
            loser = second;
        } else if (first.getType().equals(second.getType())) {
            int tiebreaker = rand.nextInt(2); //handles ties
            if (tiebreaker == 0) {
                winner = first;
                loser = second;
            } else {
                winner = second;
                loser = first;
            }
        } else { // if first doesn't win anything, and it's not a tie-breaker than the second node picked wins
            winner = second;
            loser = first;
        }

        System.out.println(winner.getName() + " is the winner!");
        System.out.println(loser.getName() + " is eliminated!");

        removeFromList(loser);

        if (size < 2) { // checks to see if there is only one node in the list
            System.out.println("+---------------------+");
            System.out.println("Only one challenger remains. The battle is over!");
            System.out.println(head.getName() + " Wins it all!!!!!!");
            return;
        } else { // if there's more nodes it runs the print method restarting the game loop
            printLinkedList();
        }
        System.out.println("+--------------+");
    }


    public void removeFromList(Node loser){
        if (loser == null || size == 0) return;

        // Case 1: Only one node in the list
        if (size == 1) {
            head = null;
            tail = null;
        }
        // Case 2: Removing the head
        else if (loser == head) {
            head = head.getNext();
            tail.setNext(head);
            head.setPrev(tail);
        }
        // Case 3: Removing the tail
        else if (loser == tail) {
            tail = tail.getPrev();
            tail.setNext(head);
            head.setPrev(tail);
        }
        // Case 4: Removing a middle node
        else {
            loser.getPrev().setNext(loser.getNext());
            loser.getNext().setPrev(loser.getPrev());
        }
        size--;
    }
}