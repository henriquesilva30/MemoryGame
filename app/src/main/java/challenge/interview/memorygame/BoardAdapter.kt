package challenge.interview.memorygame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private val context: Context, private val numberCards:Int):
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = numberCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bind(position: Int){
                //txt
            }


        }
}


