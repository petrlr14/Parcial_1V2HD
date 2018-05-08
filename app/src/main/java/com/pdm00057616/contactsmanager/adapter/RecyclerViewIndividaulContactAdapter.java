package com.pdm00057616.contactsmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.model.Address;
import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.model.Email;
import com.pdm00057616.contactsmanager.model.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewIndividaulContactAdapter extends RecyclerView.Adapter<RecyclerViewIndividaulContactAdapter.ViewHolder> {

    List<String> arrayList;
    List<Integer> arrayListType;
    String field;

    public RecyclerViewIndividaulContactAdapter(Contacts contacts, int type) {
        List<String> aux = new ArrayList<>();
        List<Integer> auxType = new ArrayList<>();
        switch (type) {
            case 1:
                List<PhoneNumber> auxNumbers = contacts.getNumbers();
                for (int i = 0; i < contacts.getNumbers().size(); i++) {
                    aux.add(auxNumbers.get(i).getNumber());
                    auxType.add(auxNumbers.get(i).getType());
                }
                field = "number";
                break;
            case 2:
                List<Email> auxEmail = contacts.getEmail();
                for (int i = 0; i < contacts.getEmail().size(); i++) {
                    aux.add(auxEmail.get(i).getEmail());
                    auxType.add(auxEmail.get(i).getType());
                }
                field = "email";
                break;
            case 3:
                List<Address> auxAddress = contacts.getAddress();
                if (auxAddress == null) {
                    auxAddress=new ArrayList<>();
                }else {
                    for (int i = 0; i < contacts.getAddress().size(); i++) {
                        auxType.add(auxAddress.get(i).getType());
                    }
                }
                field = "address";
                break;
        }
        if (aux.size() == 0) {
            aux.add("No information found");
        }
        this.arrayListType = auxType;
        this.arrayList = aux;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_info_individaul_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.position = holder.getAdapterPosition();
        holder.textView.setText(arrayList.get(position));
        holder.imageViewIcon.setImageResource(getIcon(field, position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageViewIcon;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
            imageViewIcon = itemView.findViewById(R.id.image_view_type);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnclickPhone(v, position);
                }
            });
        }
    }

    public abstract void setOnclickPhone(View v, int position);

    private int setIconTypeNumber(int type) {
        switch (type) {
            case 1:
                return R.drawable.home_icon;
            case 2:
                return R.drawable.mobile_icon;
            case 3:
                return R.drawable.work_icon;
            case 4:
            case 5:
                return R.drawable.fax_icon;
            default:
                return R.drawable.phone_icon;
        }
    }

    private int setIconTypeEmail(int type) {
        switch (type) {
            case 1:
                return R.drawable.home_icon;
            case 2:
                return R.drawable.work_icon;
            case 4:
                return R.drawable.mobile_icon;
            default:
                return R.drawable.email_icon;
        }
    }

    private int setIconTypeAddress(int type) {
        switch (type) {
            case 1:
                return R.drawable.home_icon;
            default:
                return R.drawable.work_icon;
        }
    }

    private int getIcon(String infoType, int position) {
        if (arrayListType.size() > 0) {
            if (infoType.equals("number")) {
                return setIconTypeNumber(arrayListType.get(position));
            } else if (infoType.equals("email")) {
                return setIconTypeEmail(arrayListType.get(position));
            } else {
                return setIconTypeAddress(arrayListType.get(position));
            }
        } else {
            return R.drawable.nothing_icon;
        }
    }
}
