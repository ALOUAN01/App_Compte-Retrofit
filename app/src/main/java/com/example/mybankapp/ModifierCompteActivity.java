package com.example.mybankapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybankapp.Beans.Compte;
import com.example.mybankapp.api.BanqueApi;
import com.example.mybankapp.api.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifierCompteActivity extends AppCompatActivity {

    private EditText editTextSolde;
    private EditText editTextType;
    private EditText editTextDateCreation;
    private Button btnModifier;
    private Long compteId;

    private BanqueApi banqueApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_compte);

        // Initialiser les vues
        editTextSolde = findViewById(R.id.editTextSolde);
        editTextType = findViewById(R.id.editTextType);
        editTextDateCreation = findViewById(R.id.editTextDateCreation);
        btnModifier = findViewById(R.id.btnModifier);

        // Initialiser l'API Retrofit
        banqueApi = RetrofitInstance.getRetrofitInstance().create(BanqueApi.class);

        // Récupérer l'ID du compte passé via l'Intent
        compteId = getIntent().getLongExtra("compteId", -1);

        if (compteId != -1) {
            // Charger les données du compte
            getCompteDetails();
        } else {
            Toast.makeText(this, "Erreur : ID du compte non valide", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurer le bouton pour mettre à jour le compte
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCompte();
            }
        });
    }

    // Méthode pour charger les détails du compte via l'API
    private void getCompteDetails() {
        Call<Compte> call = banqueApi.getCompteById(compteId);
        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Compte compte = response.body();
                    // Remplir les champs avec les données existantes
                    editTextSolde.setText(String.valueOf(compte.getSolde()));
                    editTextType.setText(compte.getType());
                    editTextDateCreation.setText(compte.getDateCreation());
                } else {
                    Toast.makeText(ModifierCompteActivity.this, "Erreur lors de la récupération des détails", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(ModifierCompteActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Méthode pour mettre à jour le compte via l'API
    private void updateCompte() {
        // Récupérer les nouvelles valeurs des champs
        double solde = Double.parseDouble(editTextSolde.getText().toString());
        String type = editTextType.getText().toString();
        String dateCreation = editTextDateCreation.getText().toString();

        // Créer un objet Compte avec les nouvelles données
        Compte compte = new Compte(compteId, solde, type, dateCreation);

        // Effectuer un appel PUT à l'API
        Call<Void> call = banqueApi.updateCompte(compteId, compte);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModifierCompteActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("ModifierCompte", "Erreur HTTP: " + response.code());
                    Toast.makeText(ModifierCompteActivity.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ModifierCompteActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
