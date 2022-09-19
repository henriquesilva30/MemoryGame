package challenge.interview.memorygame.Models

data class MemoryCard(
    val identifier:Int,
    val imageUrl: String? = null,
    var isFaceUp:Boolean = false,
    var isMatch:Boolean = false
)