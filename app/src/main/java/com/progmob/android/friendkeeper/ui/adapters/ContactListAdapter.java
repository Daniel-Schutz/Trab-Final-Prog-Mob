package com.progmob.android.friendkeeper.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView contactAvatar;
        private final TextView contactName;
        private final TextView contactPhone;

        private ContactViewHolder(View itemView) {
            super(itemView);
            contactAvatar = itemView.findViewById(R.id.contact_avatar);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> mContacts = new ArrayList<>(); // Cached copy of contacts

    public ContactListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (mContacts != null && !mContacts.isEmpty()) {
            Contact current = mContacts.get(position);
            holder.contactName.setText(current.getName());
            holder.contactPhone.setText(current.getPhoneNumber());

            if (current.getPhotoUri() != null && !current.getPhotoUri().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(current.getPhotoUri())
                        .placeholder(R.drawable.ic_person)
                        .into(holder.contactAvatar);
            } else {
                holder.contactAvatar.setImageResource(R.drawable.ic_person);
            }

            // Set the operator logo based on your logic
            // holder.contactOperator.setImageResource(current.getOperatorResource());
        } else {
            holder.contactName.setText(R.string.dados_nao_prontos);
        }
    }

    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        notifyItemRangeInserted(0, contacts.size());
    }

    @Override
    public int getItemCount() {
        if (mContacts != null)
            return mContacts.size();
        else return 0;
    }
}
