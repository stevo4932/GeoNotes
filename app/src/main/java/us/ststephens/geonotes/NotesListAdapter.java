package us.ststephens.geonotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    private Note[] notes;

    public NotesListAdapter(Note[] notes) {
        this.notes = notes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = notes[position];
        holder.setTitle(note.getTitle());
        holder.setDescription(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.length : 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        public void setTitle(CharSequence title) {
            this.title.setText(title);
        }

        public void setDescription(CharSequence description) {
            this.description.setText(description);
        }
    }
}
