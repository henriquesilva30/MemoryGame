package challenge.interview.memorygame.Models

enum class BoardSize(val numCards:Int){
    Easy(8),
    Medium(18),
    Hard(28);

    fun getWidth():Int{
        return when (this){
            Easy -> 2
            Medium -> 3
            Hard -> 4
        }
    }

    fun getHeight():Int{
        return numCards/getWidth()
    }

    fun getNumPairs():Int{
        return numCards/2
    }

}