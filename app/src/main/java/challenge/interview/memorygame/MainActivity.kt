package challenge.interview.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerBoard: RecyclerView
    private lateinit var moves: TextView
    private lateinit var pairs: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerBoard = findViewById(R.id.rvCard)
        moves = findViewById(R.id.movestv)
        pairs = findViewById(R.id.pairstv)

        recyclerBoard.adapter = BoardAdapter(this,8)
        recyclerBoard.setHasFixedSize(true)
       // adjust later for different sizes
        recyclerBoard.layoutManager = GridLayoutManager(this,2)



    }
}