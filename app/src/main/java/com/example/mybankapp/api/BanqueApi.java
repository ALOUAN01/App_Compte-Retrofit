package com.example.mybankapp.api;

import com.example.mybankapp.Beans.Compte;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BanqueApi {

    // Récupérer tous les comptes
    @GET("banque/comptes")
    Call<List<Compte>> getAllComptes();

    // Créer un nouveau compte
    @POST("banque/comptes")
    Call<Compte> createCompte(@Body Compte compte);

    // Mettre à jour un compte existant
    @GET("banque/comptes/{id}")
    Call<Compte> getCompteById(@Path("id") Long id);
    @PUT("banque/comptes/{id}")
    Call<Void> updateCompte(@Path("id") Long id, @Body Compte compte);
    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);
}
