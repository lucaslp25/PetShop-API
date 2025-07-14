package com.lucasdev.petshop_api.model.enums;

public enum ServiceType {

    BATH_AND_GROOMING("Bath_and_Grooming"),
    HYDRATION("Hydration"),
    VETERINARY_CONSULTATION("Veterinary_Consultation"),
    ACCOMMODATION("Accommodation"),
    TRAINING("Training"),
    VACCINATION("Vaccination");

    String serviceType;

    ServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }
}
