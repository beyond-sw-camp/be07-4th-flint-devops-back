package com.hotel.flint.dining.domain;

import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.common.enumdir.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Dining {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiningName diningName;

    @Column(length = 255, nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Option breakfastYn;

    @Column(nullable = false, unique = true, length = 13)
    private String callNum;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;
    
    @OneToMany(mappedBy = "diningId", cascade = CascadeType.ALL )
    private List<DiningReservation> diningReservations;

    @OneToMany(mappedBy = "dining", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Menu> menu;

}