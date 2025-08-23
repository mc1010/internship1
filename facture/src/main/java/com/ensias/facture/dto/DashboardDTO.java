package com.ensias.facture.dto;


import java.util.List;

public record DashboardDTO(
        long totalClients,
        long nouveauxClients, // par ex. ajoutés dans le dernier mois
        long totalProduits,
        long totalFactures,
        long facturesPayees,
        long facturesPartiellementPayees,
        long facturesImpayees,
        double montantTotalPaye,
        double montantRestant,
        List<RecentPaiementDTO> derniersPaiements,
        List<TopClientDTO> topClients
) {}

