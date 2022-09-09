package challenge.interview.memorygame

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.interview.memorygame.Models.BoardSize
import challenge.interview.memorygame.Models.MemoryCard
import challenge.interview.memorygame.Models.MemoryGame
import challenge.interview.memorygame.Utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var recyclerBoard: RecyclerView
    private lateinit var moves: TextView
    private lateinit var pairs: TextView

    private var boardSize: BoardSize = BoardSize.Hard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerBoard = findViewById(R.id.rvCard)
        moves = findViewById(R.id.movestv)
        pairs = findViewById(R.id.pairstv)

        val memoryGame = MemoryGame(boardSize)

        recyclerBoard.adapter = BoardAdapter(this,boardSize,memoryGame.cards,object: BoardAdapter.CardClickListenner{
            override fun onCardClickListenner(position: Int) {
                Log.i(TAG,"Card clicked $position")
            }

        })
        recyclerBoard.setHasFixedSize(true)
       // adjust later for different sizes
        recyclerBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())



    }
}