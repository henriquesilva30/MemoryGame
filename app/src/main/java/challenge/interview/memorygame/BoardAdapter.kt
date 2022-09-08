package challenge.interview.memorygame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private val context: Context, private val numberCards:Int):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = numberCards


        class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            fun bind(position: Int){
                //txt
            }


        }
}


