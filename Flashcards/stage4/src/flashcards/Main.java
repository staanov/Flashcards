package flashcards;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Map<String, String> cards = new LinkedHashMap<>(); //LinkedHashMap usage for order guarantee
    int numberOfCards;
    String userInputTerm;
    String userInputDefinition;

    System.out.println("Input the number of cards: ");
    Scanner scanner = new Scanner(System.in);

    /*
     * Exception Handling if user entered characters as 'number of cards' inout
     * */
    while (true) {
      try {
        numberOfCards = Integer.parseInt(scanner.nextLine());
        break;
      } catch (NumberFormatException e) {
        System.out.println("You've entered not numerical input. Try again:");
      }
    }

    /*
    * Adding new cards
    * */
    for (int i = 1; i <= numberOfCards; i++) {
      System.out.println(String.format("The card #%s: ", i));
      do {
        userInputTerm = scanner.nextLine();
        if (cards.containsKey(userInputTerm)) {
          System.out.println(String.format("The card \"%s\" already exists. Try again: ", userInputTerm));
        }
      } while (cards.containsKey(userInputTerm));
      System.out.println(String.format("The definition of the card #%s:", i));
      do {
        userInputDefinition = scanner.nextLine();
        if (cards.containsValue(userInputDefinition)) {
          System.out.println(String.format("The definition \"%s\" already exists. Try again: ", userInputDefinition));
        }
      } while (cards.containsValue(userInputDefinition));
      cards.put(userInputTerm, userInputDefinition);
    }

    /*
    * Checking user's knowledge by cards definitions
    * */
    for (Map.Entry<String, String> entry : cards.entrySet()) {
      System.out.println(String.format("Print the definition of \"%s\":", entry.getKey()));
      String userInputAnswer = scanner.nextLine();
      if (userInputAnswer.equals(entry.getValue())) {
        System.out.println("Correct answer.");
      } else if (cards.containsValue(userInputAnswer)) {
        System.out.println(String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".",
            entry.getValue(), cards.entrySet().stream().filter(e -> userInputAnswer.equals(e.getValue())).map(Map.Entry::getKey)));
      } else {
        System.out.println(String.format("Wrong answer. The correct one is \"%s\".", entry.getValue()));
      }
    }
  }
}
