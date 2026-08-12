package com.iromoratoys.family_portal.growth;

import java.time.LocalDate;
import java.time.Period;

public class ChildResponse {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private int ageInMonths;

    public ChildResponse(Child child) {
        this.id = child.getId();
        this.name = child.getName();
        this.birthDate = child.getBirthDate();
        if (child.getBirthDate() != null) {
            Period period = Period.between(child.getBirthDate(), LocalDate.now());
            this.ageInMonths = period.getYears() * 12 + period.getMonths();
        } else {
            this.ageInMonths = 0;
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public int getAgeInMonths() { return ageInMonths; }
}
