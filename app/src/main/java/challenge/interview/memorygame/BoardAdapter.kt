package challenge.interview.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import challenge.interview.memorygame.Models.BoardSize
import java.util.Collections.min
import kotlin.math.min

class BoardAdapter(private val context: Context, private val boardSize: BoardSize):
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    // its like singleton where we define const
    companion object {
        private const val MARGIN_SIZE=10
        private const val TAG = "BoardAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false)
        val cardWidth = parent.width/boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height/boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth,cardHeight)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun getItemCount() = boardSize.numCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton2)

        fun bind(position: Int){
            imageButton.setOnClickListener{
                Log.i(TAG,"Clicked on position $position")
            }
            }


        }
}


