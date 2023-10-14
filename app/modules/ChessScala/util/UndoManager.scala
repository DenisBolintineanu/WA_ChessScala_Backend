package ChessScala.util

class UndoManager {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil
  
  def doStep(command: Command) = {
    undoStack = command::undoStack
    command.doStep()
  }
  
  def undoStep(): Unit = {
    undoStack match {
      case  Nil =>
      case head::stack => {
        head.undoStep()
        undoStack=stack
        redoStack= head::redoStack
      }
    }
  }

  def deleteStep(): Unit = {
    undoStack match {
      case  Nil =>
      case head::stack => {
        head.undoStep()
        undoStack=stack
      }
    }
  }

  def redoStep(): Unit = {
    redoStack match {
      case Nil =>
      case head::stack => {
        head.redoStep()
        redoStack=stack
        undoStack=head::undoStack
      }
    }
  }
  def getMoveList: List[String] = {
    undoStack.map(k => k.inputString)
  }
}