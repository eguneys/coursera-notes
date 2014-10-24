package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("min2") = forAll { (a: Int, b: Int) =>
    val h = insert(b, insert(a, empty))

    findMin(h) == (if (a < b) a else b)
  }

  property("del1") = forAll { (a: Int) =>
    val h = deleteMin(insert(a, empty))

    h == empty
  }

  property("meld1") = forAll { (h1: H, h2: H) =>
    val m = meld(h1, h2)

    val m1 = findMin(h1)
    val m2 = findMin(h2)
    val min = if (m1 < m2) m1 else m2

    min == findMin(m)
  }

  def isSorted(h: H): Boolean = {
    if (empty == h) true
    else {
      val min = findMin(h)
      val hs = deleteMin(h)

      if (empty == hs) true
      else {
        val min2 = findMin(hs)

        if (min <= min2) isSorted(hs)
        else {
          false
        }
      }
    }
  }

  property("sort1") = forAll { (h: H) =>
    isSorted(h)
  }


  def heapEqual(h1: H, h2: H): Boolean = {
    if (h1 == empty) h2 == empty
    else if (h2 == empty || findMin(h1) != findMin(h2)) false
    else
      heapEqual(deleteMin(h1), deleteMin(h2))
  }


  property("break1") = forAll { (h1: H, h2: H) =>
    val m = meld(h1, h2)

    val min1 = findMin(h1)
    val h1s = deleteMin(h1)

    val h2p = insert(min1, h2)

    val m2 = meld(h1s, h2p)

    heapEqual(m, m2)
  }

  lazy val genHeap: Gen[H] = for {
    k <- arbitrary[Int]
    h <- Gen.frequency((1, value(empty)), (2, genHeap))
  } yield insert(k, h)


  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

}
