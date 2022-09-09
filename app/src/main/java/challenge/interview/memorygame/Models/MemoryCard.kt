package challenge.interview.memorygame.Models

data class MemoryCard(
    val identifier:Int,
    var isFaceUp:Boolean = false,
    var isMatch:Boolean = false
)