package com.whompum.PennyFlip.Notes.Adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whompum.PennyFlip.Notes.Model.Note;
import com.whompum.PennyFlip.Notes.Model.NoteWrapper;
import com.whompum.PennyFlip.OnItemSelected;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Ts;

import java.util.ArrayList;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.Holder> implements HolderProvider{

    public static final int LAYOUT = R.layout.notes_list_item;

    private List<NoteWrapper> data = new ArrayList<>();

    private LayoutInflater inflater;

    private Provider provider;
    private OnItemSelected<Note> selectionListener;

    private int highlight;

    public NotesListAdapter() {

    }

    public NotesListAdapter(@NonNull final Provider provider,
                            @NonNull final OnItemSelected<Note> selectionListener,
                            /*Resolved Color*/ final int highlight) {

        this(null, provider, selectionListener, highlight);
    }

    public NotesListAdapter(@Nullable final List<NoteWrapper> data,
                            @NonNull final Provider provider,
                            @NonNull final OnItemSelected<Note> selectionListener,
                            /*Resolved Color*/ final int highlight) {
        if(data != null)
            this.data = data;

        this.provider = provider;
        this.selectionListener = selectionListener;
        this.highlight = highlight;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(inflater == null)
            inflater = LayoutInflater.from(parent.getContext());

        return new Holder(inflater.inflate(LAYOUT, parent, false), this, highlight);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void swapDataSet(@NonNull final List<NoteWrapper> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getChildAtAdapterPosition(int pos) {
        return provider.getChildAtAdapterPosition(pos);
    }

    @Override
    @Nullable
    public NoteWrapper getDataAt(int position) {
        if(data == null || data.size() == 0 || position > data.size() -1)
            return null;

        return data.get(position);
    }

    @Override
    public void onItemClick(int posClicked) {
        selectionListener.onItemSelected(data.get(posClicked).getNote());
    }



    public static class Holder extends RecyclerView.ViewHolder{

        public Holder(@NonNull final View itemView,
                      @NonNull final HolderProvider provider,
                      final int highlight) {
            super(itemView);

            itemView.findViewById(R.id.note_list_expand)
                    .setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            final View content = provider.getChildAtAdapterPosition(getAdapterPosition());

                            final View view = content.findViewById(R.id.note_list_content);

                            final ImageButton toggle = content.findViewById(R.id.note_list_expand);

                            final NoteWrapper wrapper = provider.getDataAt(getAdapterPosition());

                            if(wrapper.isExpanded()) {
                                view.setVisibility(View.GONE);
                                toggle.setImageResource(R.drawable.icon_arrow_down);
                            }
                            else {
                                view.setVisibility(View.VISIBLE);
                                toggle.setImageResource(R.drawable.icon_arrow_up);
                            }
                            wrapper.toggle();

                        }
                    });

            itemView.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            provider.onItemClick(getAdapterPosition());
                        }
                    });

            ((TextView)itemView.findViewById(R.id.note_list_header))
                    .setTextColor(highlight);
        }

        void bind(@NonNull final NoteWrapper wrapper){

            final View content = itemView.findViewById(R.id.note_list_content);

            final Note n = wrapper.getNote();

            final Ts ts = Ts.from(n.getLastUpdate());

            final String timestamp = ts.simpleDate() + " " + ts.simpleTime();

            if(n.getNoteTitle() != null)
            ((TextView)itemView.findViewById(R.id.note_list_header))
                    .setText(n.getNoteTitle());

            ((TextView)itemView.findViewById(R.id.note_list_stamp))
                    .setText(timestamp);

            if(n.getContent() != null)
            ((TextView)itemView.findViewById(R.id.note_list_content))
                    .setText(n.getContent());

            //If the content is expanded, but we don't need it to, then close.
            if(!wrapper.isExpanded() && content.getVisibility() == View.VISIBLE) {
                ((ImageButton)itemView.findViewById(R.id.note_list_expand))
                        .setImageResource(R.drawable.icon_arrow_down);

                content.setVisibility(View.GONE);
            }

        }

    }

}
