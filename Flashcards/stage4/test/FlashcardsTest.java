import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Arrays;
import java.util.List;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;

public class FlashcardsTest extends StageTest<String> {

  /*
  * Yes, I've read about @DynamicTestingMethod annotation but I wanted to execute my tests while building using Gradle
  * and with usage of @DynamicTestingMethod my tests didn't 'attach' to the project. I don't know the reason why.
  * */
  @Override
  public List<TestCase<String>> generate() {
    return Arrays.asList(new TestCase<String>().setDynamicTesting(this::testNumberOfCardsNumericalInputExpectNormalOutput),
        new TestCase<String>().setDynamicTesting(this::testNumberOfCardsNotNumericalInputExpectExceptionThrowOrExceptionHandling),
        new TestCase<String>().setDynamicTesting(this::testNumberOfCardsEmptyInputExpectExceptionThrowOrExceptionHandling),
        new TestCase<String>().setDynamicTesting(this::testNewTermInputExpectNormalOutput),
        new TestCase<String>().setDynamicTesting(this::testOldTermInputExpectRepeatedInputRequest),
        new TestCase<String>().setDynamicTesting(this::testNewDefinitionInputExpectNormalOutput),
        new TestCase<String>().setDynamicTesting(this::testNewDefinitionAsLastCardInputExpectDefinitionAsking),
        new TestCase<String>().setDynamicTesting(this::testOldDefinitionInputExpectRepeatedInputRequest),
        new TestCase<String>().setDynamicTesting(this::testCorrectAnswerOnTermAskingExpectUserAnswerAcceptance),
        new TestCase<String>().setDynamicTesting(this::testWrongAnswerDefinedInCardsOnTermAskingExpectCorrectAnswerAndReminder),
        new TestCase<String>().setDynamicTesting(this::testWrongAnswerNotDefinedInCardsOnTermAskingExpectWrongAnswerOutput));
  }

  /*
  * Check normal number of cards input
  * */
  CheckResult testNumberOfCardsNumericalInputExpectNormalOutput() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    String output = main.execute("3");
    expect(output.toLowerCase()).toContain().regex("card");
    return CheckResult.correct();
  }

  /*
   * Check wrong number of cards input
   *
   * Note: I've read about 'user is only learning, and maybe he/she hasn't handle exceptions'.
   * But the current situation is a crash situation.
   * So we have to check it.
   * */
  CheckResult testNumberOfCardsNotNumericalInputExpectExceptionThrowOrExceptionHandling() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    try {
      main.execute("fm32");
    } catch (NumberFormatException e) {
      return CheckResult.wrong("You've entered characters in numerical-only input.");
    }
    return CheckResult.correct();
  }

  /*
   * Check empty input
   *
   * Note: I've read about 'user is only learning, and maybe he/she hasn't handle exceptions'.
   * But the current situation is a crash situation.
   * So we have to check it.
   * */
  CheckResult testNumberOfCardsEmptyInputExpectExceptionThrowOrExceptionHandling() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    try {
      main.execute("");
    } catch (NumberFormatException e) {
      return CheckResult.wrong("You've entered nothing.");
    }
    return CheckResult.correct();
  }

  /*
   * Check normal brand-new card's term input
   * */
  CheckResult testNewTermInputExpectNormalOutput() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    String output = main.execute("second term");
    expect(output.toLowerCase()).toContain().regex("definition");
    return CheckResult.correct();
  }

  /*
   * Check card's term input already contained in cards
   * */
  CheckResult testOldTermInputExpectRepeatedInputRequest() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    String output = main.execute("first term");
    expect(output.toLowerCase()).toContain().regex("try again");
    return CheckResult.correct();
  }

  /*
   * Check normal brand-new card's definition input
   * */
  CheckResult testNewDefinitionInputExpectNormalOutput() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("3");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    String output = main.execute("second definition");
    expect(output.toLowerCase()).toContain().regex("card");
    return CheckResult.correct();
  }

  /*
   * Check normal brand-new card's definition input as last card's definition (the difference with the test above in expect output)
   * */
  CheckResult testNewDefinitionAsLastCardInputExpectDefinitionAsking() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    String output = main.execute("second definition");
    expect(output.toLowerCase()).toContain().regex("definition");
    return CheckResult.correct();
  }

  /*
   * Check card's definition input already contained in cards
   * */
  CheckResult testOldDefinitionInputExpectRepeatedInputRequest() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    String output = main.execute("first definition");
    expect(output.toLowerCase()).toContain().regex("try again");
    return CheckResult.correct();
  }

  /*
  * Check correct answer
  * */
  CheckResult testCorrectAnswerOnTermAskingExpectUserAnswerAcceptance() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    main.execute("second definition");
    String output = main.execute("first definition");
    expect(output.toLowerCase()).toContain().regex("correct answer");
    return CheckResult.correct();
  }

  /*
  * Check incorrect answer which contains in other cards
  * */
  CheckResult testWrongAnswerDefinedInCardsOnTermAskingExpectCorrectAnswerAndReminder() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    main.execute("second definition");
    String output = main.execute("second definition");
    expect(output.toLowerCase()).toContain().regex("first definition");
    expect(output.toLowerCase()).toContain().regex("second term");
    return CheckResult.correct();
  }

  /*
   * Check incorrect answer which doesn't contain in other cards
   * */
  CheckResult testWrongAnswerNotDefinedInCardsOnTermAskingExpectWrongAnswerOutput() {
    TestedProgram main = new TestedProgram(flashcards.Main.class);
    main.start();
    main.execute("2");
    main.execute("first term");
    main.execute("first definition");
    main.execute("second term");
    main.execute("second definition");
    String output = main.execute("third definition");
    expect(output.toLowerCase()).toContain().regex("wrong answer");
    return CheckResult.correct();
  }
}
