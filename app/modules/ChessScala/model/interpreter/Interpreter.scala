package ChessScala.model.interpreter

trait Interpreter {

  val actions: Map[String, String => (String, Boolean)]

  val descriptor: String

  final def selectRegEx(input: String): String => (String, Boolean) =
    actions.filter(k => input.matches(k._1)).lastOption match
      case Some(value) => value._2
      case None => k => ("Wrong input! Please try again!", false)

  final def processInputLine(input: String): (String, Boolean) = selectRegEx(input)(input)
}
