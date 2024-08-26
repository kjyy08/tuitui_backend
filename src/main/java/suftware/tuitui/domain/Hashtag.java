package suftware.tuitui.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hashtag_label")
public class Hashtag {
    @Id
    @Column(name = "hashtag_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer hashtagId;

    @Column(name = "hashtag", length = 100)
    String tag;
}
