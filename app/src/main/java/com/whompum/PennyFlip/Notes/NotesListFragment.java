package com.whompum.PennyFlip.Notes;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Notes.Adapter.NotesListAdapter;
import com.whompum.PennyFlip.Notes.Adapter.NotesListAdapterClient;
import com.whompum.PennyFlip.Notes.Model.Note;
import com.whompum.PennyFlip.Notes.Model.NoteWrapper;
import com.whompum.PennyFlip.Notes.Persistence.NotesController;
import com.whompum.PennyFlip.OnItemSelected;
import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotesListFragment extends Fragment implements NotesListAdapterClient, Handler.Callback{

    public static final int LAYOUT = R.layout.notes_list;

    @BindView(R.id.id_global_list) protected RecyclerView container;

    private OnItemSelected<Note> client;

    private NotesListAdapter adapter;

    private Unbinder unbinder;

    private int transactionType;
    private String sourceId;

    private Handler resultReceiver = new Handler(this);

    public static Fragment obtain(@NonNull OnItemSelected<Note> client,
                                  int transactionType,
                                  @NonNull final String sourceId){

        final NotesListFragment fragment = new NotesListFragment();
        fragment.setOnItemSelectedListener(client);
        fragment.transactionType = transactionType;
        fragment.sourceId = sourceId;

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int highlight = 0;

        if(Build.VERSION.SDK_INT >= 23)
            if(transactionType == TransactionType.ADD)
                highlight = getContext().getColor(R.color.light_green);
            else if(transactionType == TransactionType.SPEND)
                highlight = getContext().getColor(R.color.light_red);

        if(Build.VERSION.SDK_INT < 23)
            if(transactionType == TransactionType.ADD)
                highlight = getContext().getResources().getColor(R.color.light_green);
            else if(transactionType == TransactionType.SPEND)
                highlight = getContext().getResources().getColor(R.color.light_red);

        adapter = new NotesListAdapter(this, this, highlight);

        populate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, root, false);

        this.unbinder = ButterKnife.bind(this, layout);

        container.setLayoutManager(new LinearLayoutManager(getContext()));
        container.setAdapter(adapter);

    return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public View getChildAtAdapterPosition(int pos) {
        return container.getLayoutManager().findViewByPosition(pos);
    }

    @Override
    public void onItemSelected(Note note) {
        client.onItemSelected(note);
    }

    @Override
    public boolean handleMessage(Message msg) {

        if( !(msg.obj instanceof List) )
            return true;

        final List<NoteWrapper> wrappers = new ArrayList<>(((List) msg.obj).size());

        for(Note n: ((List<Note>)msg.obj) )
            wrappers.add(new NoteWrapper(n));

        adapter.swapDataSet(wrappers);

        return true;
    }

    private void populate(){
        NotesController.obtain(getContext())
                .fetch(resultReceiver, sourceId);
    }

    public void setOnItemSelectedListener(@NonNull final OnItemSelected<Note> l){
        this.client = l;
    }
}
