package com.progmob.android.friendkeeper.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
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

/**
 * ContactListAdapter
 * <p>
 * Adaptador para gerenciar a exibição de uma lista de contatos em um RecyclerView.
 * Esta classe é responsável por vincular os dados de cada contato aos componentes
 * visuais na interface do usuário.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private static final String TAG = "ContactListAdapter";


    /**
     * ContactViewHolder
     * <p>
     * ViewHolder que contém as referências para os componentes visuais de cada item de contato.
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView contactAvatar; // Imagem do avatar do contato
        private final TextView contactName; // Nome do contato
        private final TextView contactPhone; // Número de telefone do contato

        private ContactViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            contactAvatar = itemView.findViewById(R.id.contact_avatar);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    private final LayoutInflater mInflater; // Inflater para criar views
    private List<Contact> mContacts = new ArrayList<>(); // Cache dos contatos
    private OnItemClickListener listener;

    /**
     * Construtor do ContactListAdapter
     *
     * @param context O contexto da aplicação ou atividade onde o adaptador está sendo utilizado.
     */
    public ContactListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Método onCreateViewHolder
     * <p>
     * Infla a view do item do RecyclerView e cria um novo ViewHolder.
     *
     * @param parent O ViewGroup ao qual o novo View será anexado após ser inflado.
     * @param viewType O tipo de nova View.
     * @return Um novo ContactViewHolder que mantém a View para cada item.
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ContactViewHolder(itemView, listener);
    }

    /**
     * Método onBindViewHolder
     * <p>
     * Vincula os dados de um contato ao ViewHolder.
     *
     * @param holder O ViewHolder que deve ser atualizado para representar o conteúdo do item na posição especificada.
     * @param position A posição do item no conjunto de dados do adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        try {
            if (mContacts != null && !mContacts.isEmpty()) {
                Contact current = mContacts.get(position);
                holder.contactName.setText(current.getName());
                holder.contactPhone.setText(current.getPhoneNumber());

                // TODO: Foto não está sendo definida no avatar
                if (current.getPhotoUri() != null && !current.getPhotoUri().isRelative()) {
                    Glide.with(holder.itemView.getContext())
                            .load(current.getPhotoUri())
                            .placeholder(R.drawable.ic_person)
                            .into(holder.contactAvatar);
                } else {
                    holder.contactAvatar.setImageResource(R.drawable.ic_person);
                }
            } else {
                holder.contactName.setText(R.string.dados_nao_prontos);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao vincular dados do contato na posição " + position, e);
        }
    }

    /**
     * Atualiza a lista de contatos exibida pelo adaptador.
     *
     * @param contacts A nova lista de contatos.
     */
    public void setContacts(List<Contact> contacts) {
        try {
            mContacts = contacts;
            notifyItemRangeInserted(0, contacts.size());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar a lista de contatos", e);
        }
    }

    /**
     * Retorna o número total de itens no conjunto de dados mantido pelo adaptador.
     *
     * @return O número de itens no conjunto de dados.
     */
    @Override
    public int getItemCount() {
        try {
            if (mContacts != null) {
                return mContacts.size();
            } else {
                return 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter o número de itens", e);
            return 0;
        }
    }
    public Contact getContact(int position) {
        return mContacts.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
