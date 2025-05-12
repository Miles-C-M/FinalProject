// Adapter for search tracks
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.TrackData

class TrackAdapter(
    private val tracks: List<TrackData>,
    private val onLongClick: (TrackData) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track, onLongClick)
    }

    override fun getItemCount(): Int = tracks.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.song_name)
        private val artist: TextView = itemView.findViewById(R.id.artist_name)
        private val trackImage: ImageView = itemView.findViewById(R.id.album_art)

        fun bind(track: TrackData, onLongClick: (TrackData) -> Unit) {
            trackName.text = track.name
            artist.text = track.artist

            Glide.with(itemView.context)
                .load(track.artwork)
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(trackImage)

            itemView.setOnLongClickListener {
                onLongClick(track)
                true
            }
        }
    }
}
