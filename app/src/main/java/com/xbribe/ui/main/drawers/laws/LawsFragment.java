package com.xbribe.ui.main.drawers.laws;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xbribe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LawsFragment extends Fragment

{
    @BindView(R.id.recycler_laws)
    RecyclerView recyclerView;

    LawsAdapter lawsAdapter;

    List<LawsModel> llist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View parent= inflater.inflate(R.layout.fragment_bribelaws,container,false);
        ButterKnife.bind(this,parent);
        initrecycleradapter();
        return parent;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            godisplay(position);
        }
    };

    private void godisplay(int position)
    {
        showMessage("AS STATED",llist.get(position).getDesc());
    }

    private void initrecycleradapter()
    {
        lawsAdapter=new LawsAdapter(getContext(),uploadlist());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(lawsAdapter);
        lawsAdapter.setOnItemClickListener(onClickListener);
    }

    private List<LawsModel> uploadlist()
    {
        llist=new ArrayList<>();
        llist.add(new LawsModel("SECTION 403-INDIAN PENAL CODE","who ever dishonestly misappropriates or converts to his own use any movable property,shall be punished with imprisonment of either description for a term which may extend to two years,or with fine,or withboth."));
        llist.add(new LawsModel("SECTION 405-INDIAN PENAL CODE","Whoever,being in any manner entrusted with property,or with any dominion overproperty,dishonestly misappropriates or converts to his own use that property,or dishonestly uses or disposes of that property inviolation of any direction of law prescribing the mode in which such trust is to be discharged,or of any legal contract,expressor implied,which he has made touching the discharge of such trust,or willfully suffers any other person so to do,commits criminal breach of trust"));
        llist.add(new LawsModel("SECTION 406-INDIAN PENAL CODE","Punishment for criminal breach of trust Whoever commits criminal breach of trust shall be punished with imprisonment of either description for a term which may extend to three years,or with fine,or with both."));
        llist.add(new LawsModel("SECTION-408-INDIAN PENAL CODE","Whoever,being a clerk or servant or employ edasa clerk or servant,and being in any manner entrusted in such capacity with property,or with any dominion over property,commits criminal breach of trust in respect of that property,shall be punished with imprisonment of either description for a term which may extend to seven years,and shall also be liable to fine.")) ;
        llist.add(new LawsModel("SECTION 409-INDIAN PENAL CODE","Whoever,being in any manner entrusted with property,or with any dominion over property in his capacity of a public servant or in the way of his business as a banker,merchant,factor,broker,at torney or agent,commits breach of trust in respect of that property,shall be punished with imprisonment for life,or with imprisonment of either description for a term which may extend to ten years,and shall also be liable to fine."));
        llist.add(new LawsModel("SECTION 412-INDIAN PENAL CODE","property,the possession where of he knows or has reason to believe to have been transferred by the commission of dacoity,or dishonestly receives from a person,whom he knows or has reason to believe to belong or to have belonged to agang of dacoity,property which he knows or has reason to believe to have beens tolen,shall be punished with 1)imprisonmentforlife,or with rigorous imprisonment for a term which may extend to ten years,and shall also be liable to fine"));
        llist.add(new LawsModel("SECTION 415-INDIAN PENAL CODE","Whoever,by deceiving any person,fraudulently or dishonestly induces the person so deceived to deliver any property to any person,or to consent that any person shall retain any property,or intentionally induces the person so deceived to door omit to  do any thing which he would not do omit if he were not so deceived,and which act or omission causes or is likely to cause damage or harm to that person in body,mind,reputation or property,is said to cheat"));
        llist.add(new LawsModel("SECTION 417-INDIAN PPENAL CODE","Whoever cheats shall be punished with imprisonment of either description for a term which may extend to one year, or with fine, or with both."));
        llist.add(new LawsModel("SECTION 2(C)-The Prevention of Corruption Act,1988","Any person in the service or pay of' the Government or remunerated by the Government by fees or commission for the performance of any public duty"));

       return  llist;

    }
    private void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
