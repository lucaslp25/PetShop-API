package com.lucasdev.petshop_api.services.payment;

public record PreferenceCreationResultDTO(
        boolean preferenceCreated,
        String paymentUrl,
        String preferenceId
) {

    public static PreferenceCreationResultDTO error(String errorMessage) {
        return new PreferenceCreationResultDTO(false, null, errorMessage);
    }
}

//making this class for don´t a lot of responsibility in paymentResultDTO!