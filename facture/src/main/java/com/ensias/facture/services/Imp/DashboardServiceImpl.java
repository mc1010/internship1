package com.ensias.facture.services.Imp;

import com.ensias.facture.dto.DashboardDTO;
import com.ensias.facture.dto.RecentPaiementDTO;
import com.ensias.facture.dto.TopClientDTO;
import com.ensias.facture.models.Client;
import com.ensias.facture.models.Facture;
import com.ensias.facture.models.Paiement;
import com.ensias.facture.models.StatutPaiement;
import com.ensias.facture.repositories.ClientRepository;
import com.ensias.facture.repositories.FactureRepository;
import com.ensias.facture.repositories.PaiementRepository;
import com.ensias.facture.repositories.ProduitRepository;
import com.ensias.facture.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ClientRepository clientRepository;
    private final FactureRepository factureRepository;
    private final PaiementRepository paiementRepository;
    private final ProduitRepository produitRepository;

    @Override
    public DashboardDTO getDashboardStats() {

        // --- Clients ---
        long totalClients = clientRepository.count();
        LocalDate unMois = LocalDate.now().minusMonths(1);
        long nouveauxClients = clientRepository.findAll()
                .stream()
                .filter(c -> c.getFactures().stream()
                        .anyMatch(f -> f.getDateEmission().isAfter(unMois)))
                .count();

        // --- Produits ---
        long totalProduits = produitRepository.count();

        // --- Factures ---
        List<Facture> toutesFactures = factureRepository.findAll();
        long totalFactures = toutesFactures.size();
        long facturesPayees = toutesFactures.stream()
                .filter(f -> f.getStatutPaiement() == StatutPaiement.PAYEE)
                .count();
        long facturesPartiellementPayees = toutesFactures.stream()
                .filter(f -> f.getStatutPaiement() == StatutPaiement.PARTIELLE)
                .count();
        long facturesImpayees = toutesFactures.stream()
                .filter(f -> f.getStatutPaiement() == StatutPaiement.IMPAYEE)
                .count();

        // --- Paiements ---
        List<Paiement> tousPaiements = paiementRepository.findAll();
        double montantTotalPaye = tousPaiements.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
        double montantRestant = toutesFactures.stream()
                .mapToDouble(Facture::getTotalTTC)
                .sum() - montantTotalPaye;

        List<RecentPaiementDTO> derniersPaiements = tousPaiements.stream()
                .sorted(Comparator.comparing(Paiement::getDatePaiement).reversed())
                .limit(5)
                .map(p -> new RecentPaiementDTO(
                        p.getId(),
                        p.getMontant(),
                        p.getDatePaiement(),
                        p.getReferenceTransaction(),
                        p.getFacture().getId()
                ))
                .toList();

        // --- Top Clients ---
        Map<Client, Double> totalParClient = tousPaiements.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFacture().getClient(),
                        Collectors.summingDouble(Paiement::getMontant)
                ));
        List<TopClientDTO> topClients = totalParClient.entrySet().stream()
                .sorted(Map.Entry.<Client, Double>comparingByValue().reversed())
                .limit(5)
                .map(e -> new TopClientDTO(
                        e.getKey().getId(),
                        e.getKey().getRaisonSociale(),
                        e.getValue()
                ))
                .toList();

        return new DashboardDTO(
                totalClients,
                nouveauxClients,
                totalProduits,
                totalFactures,
                facturesPayees,
                facturesPartiellementPayees,
                facturesImpayees,
                montantTotalPaye,
                montantRestant,
                derniersPaiements,
                topClients
        );
    }
}