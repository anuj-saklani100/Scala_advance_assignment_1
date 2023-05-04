package problem_statement_3

object prob extends App{
// normal way to display a list via method of a class
class temp[A](list:List[A]){
  def display:Unit= list.foreach(println)
}
  val a=List(1,2,3,4,5,6)
  val b=new temp(a)
  val c=List("anuj","prateek")
  val d=new temp(c)
  b.display
  d.display

  // implicit way to do this (Assignment problem)
  
  implicit class ListOps[T](list:List[T]){
    def convert:Unit= println(list.mkString("\"",",","\""))
  }
  val lists=List(1,2,3,4,5,6,7,8,9,10)
   lists.convert

  // major difference is we are not initiating a class, the implicit is taking care of that behind the scenes
}
