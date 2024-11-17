package com.example.mybankapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybankapp.Beans.Compte;
import com.example.mybankapp.R;

import java.util.ArrayList;
import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes = new ArrayList<>();
    private CompteItemClickListener listener; // Interface pour gérer les clics sur les boutons

    // Interface pour les clics sur "Modifier" et "Supprimer"
    public interface CompteItemClickListener {
        void onEditClick(Compte compte);
        void onDeleteClick(Compte compte);
    }

    public void setCompteItemClickListener(CompteItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);

        // Afficher les informations du compte
        holder.soldeTextView.setText(String.format("Solde: %.2f", compte.getSolde()));
        holder.typeTextView.setText(compte.getType());
        holder.dateCView.setText(String.format("Date: %s", compte.getDateCreation()));

        // Bouton Modifier
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(compte); // Passer l'objet compte au callback
            }
        });

        // Bouton Supprimer
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(compte); // Passer l'objet compte au callback
            }
        });
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    // Méthode pour mettre à jour la liste des comptes
    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
        notifyDataSetChanged();
    }

    // ViewHolder pour chaque élément
    public static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView soldeTextView;
        TextView typeTextView;
        TextView dateCView;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Lier les vues du layout
            soldeTextView = itemView.findViewById(R.id.soldeTextView);
            typeTextView = itemView.findViewById(R.id.typeCompte);
            dateCView = itemView.findViewById(R.id.dateCreation);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
