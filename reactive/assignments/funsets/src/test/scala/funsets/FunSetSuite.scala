package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains intersected elements") {
    new TestSets {
      val s1Us2 = union(s1, s2)
      val s2Us3 = union(s2, s3)
      val s2I = intersect(s1Us2, s2Us3)
      assert(!contains(s2I, 1), "Intersect 1")
      assert(contains(s2I, 2), "Intersect 2")
      assert(!contains(s2I, 3), "Intersect 3")
    }
  }

  test("diff contains diffed elements") {
    new TestSets {
      val s1Us2 = union(s1, s2)
      val s2Us3 = union(s2, s3)
      val s2D = diff(s1Us2, s2Us3)
      assert(contains(s2D, 1), "Diff 1")
      assert(!contains(s2D, 2), "Diff 2")
      assert(!contains(s2D, 3), "Diff 3")
    }
  }

  test("filter contains diffed elements") {
    new TestSets {
      val s1Us2Us3 = union(union(s1, s2), s3)
      val sF = filter(s1Us2Us3, x => x <= 2)

      assert(contains(sF, 1), "Filter 1")
      assert(contains(sF, 2), "Filter 2")
      assert(!contains(sF, 3), "Filter 3")
    }
  }

  test("forall") {
    new TestSets {
      val u = union(union(s1, s2), s3)

      assert(forall(u, x => x < 2000), "all < 2000")
      assert(forall(u, x => x < 1000), "all < 1000")
      assert(!forall(u, x => x < 3), "all < 3")
      assert(!forall(u, x => x < 2), "all < 2")
      assert(!forall(u, x => x < 1), "all < 2")
      assert(forall(u, x => x >= 1), "all >= 1")
    }
  }

  test("exists") {
    new TestSets {
      val u = union(union(s1, s2), s3)

      assert(exists(u, x => x == 1), "exists 1")
      assert(exists(u, x => x == 2), "exists 2")
      assert(exists(u, x => x == 3), "exists 3")
      assert(!exists(u, x => x == 4), "exists 4")
    }
  }


  test("map") {
    new TestSets {
      val u = union(union(s1, s2), s3)

      assert(forall(map(u, x => 1), x => x == 1), "map x => 1")
      assert(forall(map(u, x => x * x), x => x == 1 || x == 4 || x == 9), "map x => x * x")
      assert(forall(map(u, x => x + 10), x=> x <= 13), "map x + 10")
    }
  }
}
