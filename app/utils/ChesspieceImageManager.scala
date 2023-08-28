package utils
import ChessScala.model.board.{Board, Coordinate}

case class ChesspieceImageManager(board: Board) {

  private val images:Map[Int, String] = Map(
    (1, "blackRook.png"),
    (2, "blackKnight.png"),
    (3, "blackBishop.png"),
    (4, "blackQueen.png"),
    (5, "blackKing.png"),
    (6, "blackPawn.png"),
    (7, "whiteRook.png"),
    (8, "whiteKnight.png"),
    (9, "whiteBishop.png"),
    (10, "whiteQueen.png"),
    (11, "whiteKing.png"),
    (12, "whitePawn.png"),
    (13, "blackPawn.png"),
    (14, "whitePawn.png"),
    (15, "blackPawn.png"),
    (16, "whitePawn.png"),
    (17, "blackRook.png"),
    (18, "whiteRook.png"),
  )

  def getImage(x: Int, y: Int): String ={
    if (board.get(Coordinate(x,y)).isEmpty) return ""
    val id: Int = board.get(Coordinate(x,y)).get.id
    "images/chesspieces/"+ images(id)
  }


}
