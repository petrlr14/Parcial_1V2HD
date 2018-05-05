package com.pdm00057616.contactsmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.model.Contacts;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Serializable {

    private ArrayList<Contacts> contacts;
    public RecyclerViewAdapter(ArrayList<Contacts> contacts) {
        this.contacts=contacts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_contact_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.position=position;
        Contacts contact=this.contacts.get(position);
        if(contact.getPhoto()!=null){
            holder.imageViewPhoto.setImageBitmap(contact.getPhoto());
        }else{
            holder.imageViewPhoto.setImageResource(R.drawable.user);
        }

        holder.textViewName.setText(contact.getName());
        if(contact.getFav()){
            holder.imageButtonFav.setImageResource(R.drawable.star_selected);
        }else{
            holder.imageButtonFav.setImageResource(R.drawable.star_deselected);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewPhoto;
        ImageButton imageButtonFav;
        TextView textViewName;
        int position;
        public ViewHolder(View itemView) {
            super(itemView);
            bindView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnClickCardViewAction(v, position);
                }
            });
        }
        private void bindView(View view){
            imageViewPhoto=view.findViewById(R.id.image_view_photo);
            imageButtonFav=view.findViewById(R.id.image_button_fav);
            textViewName=view.findViewById(R.id.textview_name_contact);
            imageButtonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnClickButtonAction(v, position);
                }
            });
        }
    }

    public abstract void setOnClickCardViewAction(View view, int position);
    public abstract void setOnClickButtonAction(View view, int position);

}
