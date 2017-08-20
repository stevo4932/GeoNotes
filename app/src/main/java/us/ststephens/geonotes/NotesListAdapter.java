package us.ststephens.geonotes;

import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import us.ststephens.geonotes.models.Note;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    private CircularArray<Note> notes;
    private OnNoteSelectedListener listener;
    private RecyclerView recyclerView;

    public NotesListAdapter() {
        this.notes = new CircularArray<>();
    }

    public void setNotes(Note[] notes) {
        for (Note note : notes) {
            if (note != null) {
                this.notes.addLast(note);
            }
        }
        notifyDataSetChanged();
    }

    public void addNote(@NonNull Note note) {
        notes.addFirst(note);
        notifyItemInserted(0);
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public void setListener(OnNoteSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewHolderClicked(viewHolder);
            }
        });
        return viewHolder;
    }

    private void onViewHolderClicked(ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        viewHolder.setExpanded(!viewHolder.isExpanded());
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.setTitle(note.getTitle());
        holder.setDescription(note.getDescription());
        holder.toggleDescription();
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;
        private boolean isExpanded;

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

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public void toggleDescription() {
            if (isExpanded()) {
                description.setMaxLines(Integer.MAX_VALUE);
                description.setEllipsize(null);
            } else {
                description.setMaxLines(2);
                description.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }

    public interface OnNoteSelectedListener{
        void onNoteSelected(View view, Note note);
    }
}
