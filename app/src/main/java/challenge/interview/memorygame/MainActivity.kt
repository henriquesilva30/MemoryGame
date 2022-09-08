package challenge.interview.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var moves: RecyclerView
    private lateinit var pairs: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.rvCard)
        moves = findViewById(R.id.movestv)
        pairs = findViewById(R.id.pairstv)

        recycler.adapter = BoardAdapter(this,8)
        recycler.setHasFixedSize(true)
        //adjust later for different sizes
        recycler.layoutManager = GridLayoutManager(this,2)



    }
}