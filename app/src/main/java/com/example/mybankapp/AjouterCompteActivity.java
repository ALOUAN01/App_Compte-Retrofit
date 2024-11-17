package com.example.mybankapp;

import android.os.Bundle;
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

public class AjouterCompteActivity extends AppCompatActivity {

    private EditText editSolde, editDateCreation, editType;
    private Button btnAjouter;
    private BanqueApi banqueApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_compte);

        // Initialiser les vues
        editSolde = findViewById(R.id.editSolde);
        editDateCreation = findViewById(R.id.editDateCreation);
        editType = findViewById(R.id.editType);
        btnAjouter = findViewById(R.id.btnAjouter);

        // Initialisation de Retrofit
        banqueApi = RetrofitInstance.getRetrofitInstance().create(BanqueApi.class);

        // Ajouter un compte lorsque le bouton est cliqué
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double solde = Double.parseDouble(editSolde.getText().toString());
                String dateCreation = editDateCreation.getText().toString();
                String type = editType.getText().toString();

                // Créer un objet Compte
                Compte compte = new Compte(null, solde, dateCreation, type);

                // Appeler l'API pour ajouter le compte
                banqueApi.createCompte(compte).enqueue(new Callback<Compte>() {
                    @Override
                    public void onResponse(Call<Compte> call, Response<Compte> response) {
                        if (response.isSuccessful()) {
                            // Afficher un message de succès
                            Toast.makeText(AjouterCompteActivity.this, "Compte ajouté avec succès", Toast.LENGTH_SHORT).show();
                            MainActivity mainActivity =new MainActivity();
                            mainActivity.getComptes();
                            finish();  // Retourner à la MainActivity
                        }
                    }

                    @Override
                    public void onFailure(Call<Compte> call, Throwable t) {
                        // Afficher un message d'erreur
                        Toast.makeText(AjouterCompteActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
