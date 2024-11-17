package com.example.mybankapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybankapp.Beans.Compte;
import com.example.mybankapp.adapters.CompteAdapter;
import com.example.mybankapp.api.BanqueApi;
import com.example.mybankapp.api.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;
    private BanqueApi banqueApi;
    private Button btnAjouterCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de Retrofit
        banqueApi = RetrofitInstance.getRetrofitInstance().create(BanqueApi.class);

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        compteAdapter = new CompteAdapter();
        recyclerView.setAdapter(compteAdapter);

        // Configurer les listeners pour les clics sur "Modifier" et "Supprimer"
        setupCompteItemClickListeners();

        // Initialiser le bouton d'ajout de compte
        btnAjouterCompte = findViewById(R.id.btnAjouterCompte);
        btnAjouterCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers l'activité pour ajouter un compte
                Intent intent = new Intent(MainActivity.this, AjouterCompteActivity.class);
                startActivity(intent);
            }
        });

        // Récupérer les comptes depuis l'API
        getComptes();
    }

    // Configurer les listeners pour les boutons Modifier/Supprimer dans l'adapter
    private void setupCompteItemClickListeners() {
        compteAdapter.setCompteItemClickListener(new CompteAdapter.CompteItemClickListener() {
            @Override
            public void onEditClick(Compte compte) {
                // Lancer une activité ou dialog pour modifier le compte
                Intent intent = new Intent(MainActivity.this, ModifierCompteActivity.class);
                intent.putExtra("compteId", compte.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Compte compte) {
                // Supprimer un compte après confirmation
                supprimerCompte(compte);
            }
        });
    }

    // Récupérer les comptes depuis l'API
    public void getComptes() {
        Call<List<Compte>> call = banqueApi.getAllComptes();
        call.enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    compteAdapter.setComptes(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la récupération des comptes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour supprimer un compte via l'API
    private void supprimerCompte(Compte compte) {
        Call<Void> call = banqueApi.deleteCompte(compte.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                    // Rafraîchir la liste après suppression
                    getComptes();
                } else {
                    Toast.makeText(MainActivity.this, "Échec de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
