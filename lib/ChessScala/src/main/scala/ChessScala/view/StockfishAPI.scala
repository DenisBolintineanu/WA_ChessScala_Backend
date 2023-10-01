package ChessScala.view

import ChessScala.controller.IController
import ChessScala.model.gameState.stateImplementation.AIState
import ChessScala.util.Observer

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.sys.process.{Process, ProcessIO}

class StockfishAPI(controller: IController) extends Observer{

  controller.add(this)
  private val stockfishPath = "/opt/homebrew/bin/stockfish"

  def stockfishIO(time: Int, UCI: String, trigger: String => Boolean, expression: String => String): Option[String] = {
    var result: Option[String] = None
    val processIO = new ProcessIO(
      in => {
        val writer = new PrintWriter(in)
        writer.println("setoption name Skill Level value 0")
        writer.flush()
        writer.println("uci")
        writer.flush()
        writer.println(s"position startpos moves $UCI")
        writer.flush()
        writer.println(s"go movetime $time")
        writer.flush()
        Thread.sleep((200).toInt)
        writer.close()
      },
      out => {
        val reader = new BufferedReader(new InputStreamReader(out))
        var line: String = ""
        Thread.sleep((200).toInt)
        while ({ line = reader.readLine(); line != null }) {
          if (trigger(line))
            result = Some(expression(line))
        }
        reader.close()
      },
      _.close()
    )
    println(UCI)
    val process = Process(stockfishPath).run(processIO)
    process.exitValue()
    println(result)
    result
  }

  def getBestMove(input: String): Option[String] = {
    val trigger: String => Boolean = k => k.startsWith("bestmove")
    val expression: String => String = k => k.split(" ")(1)
    val time: Int = 1
    Thread.sleep(300)
    stockfishIO(time, input, trigger, expression)
  }

  def evaluatePosition(input: String): Option[String] = {
    val team = if (input.split(" ").length % 2 == 0) 1 else -1
    val mateTrigger: String => Boolean = line => line.startsWith("info") && line.contains("mate")
    val trigger: String => Boolean = line => line.startsWith("info") && line.contains("score cp") || mateTrigger(line)
    val expression: String => String = line => {
      val parts = line.split(" ")
      if (mateTrigger(line)) s"#${team * parts(parts.indexOf("mate") + 1).toInt}"
      else s"${team * parts(parts.indexOf("score") + 2).toInt}"
    }
    val time: Int = 700
    stockfishIO(time, input, trigger, expression)
  }

  override def update(): Unit =
    controller.state match
      case state: AIState => {
        if (state.team != state.selectedTeam)
        Thread(()=> {
          val moves = (controller.returnMoveList().reverse.mkString(" "))
          Thread.sleep(100)
          getBestMove(moves) match
            case Some(value) => controller.computeInput(value)
            case None => {}
        }).start()
      }
      case _ => return
}
