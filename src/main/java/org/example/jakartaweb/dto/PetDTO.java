package org.example.jakartaweb.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonbPropertyOrder({"id", "name", "species", "hungerLevel", "happiness"})
public class PetDTO {

    private Long id;

    @NotBlank(message = "Pet name cannot be empty")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Pet species must be specified")
    private String species;

    @NotNull(message = "Hunger level must be specified")
    @Min(value = 0, message = "Hunger level cannot be lower than 0")
    @Max(value = 100, message = "Hunger level cannot be higher than 100")
    private Integer hungerLevel;

    @NotNull(message = "Happiness level must be specified")
    @Min(value = 0, message = "Happiness level cannot be lower than 0")
    @Max(value = 100, message = "Happiness level cannot be higher than 100")
    private Integer happiness;

    public PetDTO() {}

    public PetDTO(Long id, String name, String species, Integer hungerLevel, Integer happiness) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.hungerLevel = hungerLevel;
        this.happiness = happiness;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public Integer getHungerLevel() { return hungerLevel; }
    public void setHungerLevel(Integer hungerLevel) { this.hungerLevel = hungerLevel; }

    public Integer getHappiness() { return happiness; }
    public void setHappiness(Integer happiness) { this.happiness = happiness; }
}

