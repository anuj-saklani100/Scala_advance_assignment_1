package jsonSerializer

//  problem statement 2 of assignment
import jsonSerializer.jsonserializer.jsonToString

import java.util.Date
object jsonserializer  extends App{
  // lets make a real time API generator (Json) from the given information
  case class User(name:String,age:Int,email:String)
  case class Post(content:String,posted_on:Date)
  case class Feed(user:User,post:List[Post])

  // Now lets build a trait
  sealed trait API{
    def stringify:String
  }
  // conversion classes

  final case class jsonToString(value:String) extends API{
    def stringify:String = "\"" + value + "\""
  }
  final case class jsonToInt(value:Int) extends API{
    def stringify:String= value.toString
  }
  final case class jsonArray(value:List[API]) extends API{
    def stringify:String={
      value.map(_.stringify).mkString("[",",","]")
    }
  }
  final case class jsonObject(value:Map[String,API]) extends API{
    def stringify:String= {
      value.map {
        case (key, valu) => "\"" + key + "\"" + " : " + valu.stringify
      }.mkString("{",",","}")
    }

  }
  // lets test it
  val test1=jsonObject(
    Map(
      "name"-> jsonToString("Anuj Saklani"),
      "age"-> jsonToInt(22),
      "email"-> jsonToString("anuj.saklani@tellius.com"),
      "hobbies"-> jsonArray(List(
        jsonToString("cricket"),
        jsonToString("travelling"),
        jsonToString("coding"),
        jsonToString("teaching")
      )
      )

    ))
  println(test1.stringify)    //testing of above conversion
// working perfectly
  // OUTPUT:   {"name" : "Anuj Saklani","age" : 22,"email" : "anuj.saklani@tellius.com","hobbies" : ["cricket","travelling","coding","teaching"]}

  // now using type class lets make a conversions (using implicits)

  trait API_conv[T]{     // convertor trait
    def convertor(value:T):API
  }

  implicit object intConv extends API_conv[Int]{
    def convertor(value:Int):API= jsonToInt(value)
  }

  implicit object stringConv extends API_conv[String]{
    def convertor(value:String):API= jsonToString(value)
  }

  // user convertor
  implicit object user_conv extends API_conv[User]{
    def convertor(user:User):API={
      jsonObject(
        Map(
          "name"-> jsonToString(user.name),
          "age"-> jsonToInt(user.age),
          "email"-> jsonToString(user.email)
        )
      )
    }
  }

  // post convertor
  implicit object post_conv extends API_conv[Post] {
def convertor(post:Post):API={
  jsonObject(
    Map(
      "content" -> jsonToString(post.content),
      "posted-on"-> jsonToString(post.posted_on.toString)
    )
  )
  }
  }

  // feed convertor
  implicit object feed_conv extends API_conv[Feed]{
    def convertor(feed:Feed):API={
      jsonObject(
        Map(
          "users"-> user_conv.convertor(feed.user),
          "posts"-> jsonArray(feed.post.map(post_conv.convertor))
        )
      )
    }
  }

  // now make a implicit class call with generics
  implicit class caller[T](value:T){
    def generate(implicit convert:API_conv[T]):API= convert.convertor(value)
  }

  // Now come to actual testing
  val newdate=new Date(System.currentTimeMillis())
  val anuj=User("Anuj Saklani",22,"anuj.saklani@tellius.com")
  val feed=Feed(
   anuj,List(
      Post("Hey finally I completed this assignment problem",newdate),
      Post("The weather is Rainy today",newdate),
      Post("I got a new Friend!",newdate),
      Post("I bought an i phone",newdate),
      Post("I like animals!",newdate)
    )
  )

  //  final testing
  println(feed.generate.stringify)

  /*
  OUPUT:
  {"users" : {"name" : "Anuj Saklani","age" : 22,"email" : "anuj.saklani@tellius.com"},
  "posts" : [{"content" : "Hey finally I completed this assignment problem","posted-on" : "Wed May 03 20:57:29 IST 2023"},
  {"content" : "The weather is Rainy today","posted-on" : "Wed May 03 20:57:29 IST 2023"},
  {"content" : "I got a new Friend!","posted-on" : "Wed May 03 20:57:29 IST 2023"},
  {"content" : "I bought an i phone","posted-on" : "Wed May 03 20:57:29 IST 2023"},
  {"content" : "I like animals!","posted-on" : "Wed May 03 20:57:29 IST 2023"}]}

   */

}
