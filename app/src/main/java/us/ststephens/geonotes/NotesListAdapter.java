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

public class NotesListAdapter extends RecyclerView.Adapter<NoteListViewHolder> implements NoteListViewHolder.OnNoteClickedListener{
    private CircularArray<Note> notes;
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
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_list_item, parent, false);
        return new NoteListViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(NoteListViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.setTitle(note.getTitle());
        holder.setDescription(note.getDescription());
        holder.toggleDescription(note.isExpanded());
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    @Override
    public void onNoteClicked(int position) {
        Note note = notes.get(position);
        note.setExpanded(!note.isExpanded()); //toggle expanded flag
        notifyItemChanged(position);
    }
}
