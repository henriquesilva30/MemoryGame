package challenge.interview.memorygame

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.interview.memorygame.Models.BoardSize
import challenge.interview.memorygame.Models.MemoryCard
import challenge.interview.memorygame.Models.MemoryGame
import challenge.interview.memorygame.Utils.DEFAULT_ICONS
import challenge.interview.memorygame.Utils.EXTRA_BOARD_SIZE
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE = 248


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

        val intent = Intent(this,CreateActivity::class.java)
        intent.putExtra(EXTRA_BOARD_SIZE,BoardSize.Medium)
        startActivity(intent)

        recyclerBoard = findViewById(R.id.rvCard)
        moves = findViewById(R.id.movestv)
        pairs = findViewById(R.id.pairstv)
        clRoot = findViewById(R.id.clRoot)

     setupBoard()
    }

    private fun setupBoard() {
        when (boardSize){
            BoardSize.Easy -> {
                moves.text = "Easy: 4x2"
                pairs.text = "Pairs: 0/4"
            }
            BoardSize.Medium -> {
                moves.text = "Medium: 6x3"
                pairs.text = "Pairs: 0/9"
            }
            BoardSize.Hard -> {
                moves.text = "Hard: 7x4"
                pairs.text = "Pairs: 0/13"
            }
        }
        pairs.setTextColor(ContextCompat.getColor(this,R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
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

    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?",null, View.OnClickListener {
                        setupBoard()
                    })
                } else {
                setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
            R.id.mi_custom -> {
                showCreationDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreationDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialog("Create your own memory board", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.Easy
                R.id.rbMedium -> BoardSize.Medium
                else -> BoardSize.Hard
            }
            val intent = Intent(this,CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desiredBoardSize)
            startActivityForResult(intent,CREATE_REQUEST_CODE)
        })
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when (boardSize) {
            BoardSize.Easy -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.Medium -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.Hard -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.Easy
                R.id.rbMedium -> BoardSize.Medium
                else -> BoardSize.Hard
            }

            setupBoard()
        })
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
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