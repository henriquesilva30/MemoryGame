package challenge.interview.memorygame

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.interview.memorygame.Models.BoardSize
import challenge.interview.memorygame.Models.MemoryCard
import challenge.interview.memorygame.Models.MemoryGame
import challenge.interview.memorygame.Utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: BoardAdapter
    private lateinit var memoryGame: MemoryGame
    private lateinit var recyclerBoard: RecyclerView
    private lateinit var moves: TextView
    private lateinit var pairs: TextView
    private lateinit var clRoot: ConstraintLayout

    private var boardSize: BoardSize = BoardSize.Easy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerBoard = findViewById(R.id.rvCard)
        moves = findViewById(R.id.movestv)
        pairs = findViewById(R.id.pairstv)
        clRoot = findViewById(R.id.clRoot)

        memoryGame = MemoryGame(boardSize)
        pairs.setTextColor(ContextCompat.getColor(this,R.color.color_progress_none))
        adapter = BoardAdapter(this,boardSize,memoryGame.cards,object: BoardAdapter.CardClickListenner{
            override fun onCardClickListenner(position: Int) {
                //Log.i(TAG,"Card clicked $position")
                updateGameWithFlip(position)
            }

        })
        recyclerBoard.adapter = adapter
            recyclerBoard.setHasFixedSize(true)
       // adjust later for different sizes
        recyclerBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())



    }

    private fun updateGameWithFlip(position: Int) {
        //error checking
        if(memoryGame.haveWonGame()){
            Snackbar.make(clRoot,"You already won!",Snackbar.LENGTH_SHORT).show()
            return
        }
        //error checking
        if (memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot,"Invalid move!",Snackbar.LENGTH_SHORT).show()
            return
        }
        //Acttualy flip over the card
        if(memoryGame.flipCard(position)){
            Log.i(TAG,"Found the match! Num pairs found :${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat()/boardSize.getNumPairs(),
                ContextCompat.getColor(this,R.color.color_progress_none),
                ContextCompat.getColor(this,R.color.color_progress_full)
            ) as Int
            pairs.setTextColor(color)
            pairs.text = "Pairs: ${memoryGame.numPairsFound}/${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()){
                Snackbar.make(clRoot,"You won! Congratulations!",Snackbar.LENGTH_SHORT).show()
            }
        }
        moves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}