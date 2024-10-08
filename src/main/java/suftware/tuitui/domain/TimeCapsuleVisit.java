package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visit_counter")
public class TimeCapsuleVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_counter_id", nullable = false, unique = true)
    Integer visitCountId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", referencedColumnName = "capsule_id", unique = true)
    TimeCapsule timeCapsule;

    @Column(name = "visit_count", nullable = false)
    Integer visitCount;
}
