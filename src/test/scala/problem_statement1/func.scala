package problem_statement1
// problem statement 1 of assignment
object func extends App{
  /* In this problem statement the function is taking an implicit parameter and using it inside it for the calculation*/

  // lets make a simple program that takes two parameters 1 is normal and 1 is implicit
class Shopping{
    def calculate(price:Int)(implicit discount:Int):Int={
      val res= price-discount
      res
    }
  }
  // normal way to call the function (curried function)
  val offer1=new Shopping
  println(offer1.calculate(8000)(2000))    // Output: 6000

  // implicit way
  implicit val dis=3000
  val offer2=new Shopping
  println(offer2.calculate(9000))         // Output: 6000

}


