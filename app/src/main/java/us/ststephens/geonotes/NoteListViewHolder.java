package us.ststephens.geonotes;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView title;
    private TextView description;
    private OnNoteClickedListener listener;

    public NoteListViewHolder(View itemView, OnNoteClickedListener listener) {
        this(itemView);
        this.listener = listener;
    }

    public NoteListViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        itemView.setOnClickListener(this);
    }

    public void setListener(OnNoteClickedListener listener) {
        this.listener = listener;
    }

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setDescription(CharSequence description) {
        this.description.setText(description);
    }

    public void toggleDescription(boolean isExpanded) {
        if (isExpanded) {
            description.setMaxLines(Integer.MAX_VALUE);
            description.setEllipsize(null);
        } else {
            description.setMaxLines(2);
            description.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onNoteClicked(getAdapterPosition());
        }
    }

    public interface OnNoteClickedListener{
        void onNoteClicked(int position);
    }
}
