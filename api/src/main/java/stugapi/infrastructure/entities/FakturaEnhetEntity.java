package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "faktura_enhet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FakturaEnhetEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String quantity;
    
    @Column(nullable = false)
    private String price;
    
    @Column(nullable = false)
    private String total;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faktura_id")
    private FakturaEntity faktura;
}