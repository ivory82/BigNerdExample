package gs.im

data class Coordinate( val x: Int, val y: Int){
    val isInBounds = x >= 0 && y >= 0
}