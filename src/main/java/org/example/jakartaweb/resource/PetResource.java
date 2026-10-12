package org.example.jakartaweb.resource;

import org.example.jakartaweb.dto.PetDTO;
import org.example.jakartaweb.service.PetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JAX-RS Resource class exposing RESTful API endpoints for Pet Management.
 * Manually transforms payloads into LinkedHashMaps to hide IDs and guarantee correct field sorting.
 */
@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {

    @Inject
    private PetService petService;

    // Helper method to enforce exact property order and completely HIDE the ID field
    private Map<String, Object> convertToSortedMapWithoutId(PetDTO pet) {
        Map<String, Object> sortedMap = new LinkedHashMap<>();
        sortedMap.put("name", pet.getName());
        sortedMap.put("species", pet.getSpecies());
        sortedMap.put("hungerLevel", pet.getHungerLevel());
        sortedMap.put("happiness", pet.getHappiness());
        return sortedMap;
    }

    /**
     * POST /pets -> Adopt a new pet.
     */
    @POST
    public Response adoptPet(@Valid PetDTO petDTO) {
        PetDTO createdPet = petService.adoptPet(petDTO);
        return Response.status(Response.Status.CREATED)
                .entity(convertToSortedMapWithoutId(createdPet))
                .build();
    }

    /**
     * GET /pets -> List all pets with high-grade features: Filtering, Sorting, and Pagination.
     */
    @GET
    public Response listAllPets(@Context UriInfo uriInfo) {
        // Extract query parameters
        String species = uriInfo.getQueryParameters().getFirst("species");
        String sortBy = uriInfo.getQueryParameters().getFirst("sortBy");
        String order = uriInfo.getQueryParameters().getFirst("order");
        String offsetParam = uriInfo.getQueryParameters().getFirst("offset");
        String limitParam = uriInfo.getQueryParameters().getFirst("limit");

        // 1. Fetch master collection from service
        List<PetDTO> result = petService.getAllPets();

        // 2. FILTERING: Filter by species
        if (species != null && !species.trim().isEmpty()) {
            String cleanedFilter = species.trim();
            result = result.stream()
                    .filter(pet -> pet.getSpecies() != null && pet.getSpecies().equalsIgnoreCase(cleanedFilter))
                    .collect(Collectors.toList());
        }

        // 3. SORTING: Sort dynamically based on parameters
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Comparator<PetDTO> comparator = null;

            if (sortBy.equalsIgnoreCase("happiness")) {
                comparator = Comparator.comparingInt(PetDTO::getHappiness);
            } else if (sortBy.equalsIgnoreCase("hungerLevel")) {
                comparator = Comparator.comparingInt(PetDTO::getHungerLevel);
            } else if (sortBy.equalsIgnoreCase("name")) {
                comparator = Comparator.comparing(PetDTO::getName);
            } else if (sortBy.equalsIgnoreCase("species")) {
                comparator = Comparator.comparing(PetDTO::getSpecies);
            }

            if (comparator != null) {
                if (order != null && order.equalsIgnoreCase("desc")) {
                    comparator = comparator.reversed();
                }
                result.sort(comparator);
            }
        }

        // 4. PAGINATION: Slice dataset bounds
        int offset = 0;
        int limit = 10;

        try {
            if (offsetParam != null) offset = Integer.parseInt(offsetParam);
            if (limitParam != null) limit = Integer.parseInt(limitParam);
        } catch (NumberFormatException e) {
            // Fallback to defaults
        }

        if (offset < 0) offset = 0;
        if (limit <= 0) limit = 10;

        int totalSize = result.size();
        List<PetDTO> pagedList = new ArrayList<>();
        if (offset < totalSize) {
            int toIndex = Math.min(offset + limit, totalSize);
            pagedList = result.subList(offset, toIndex);
        }

        // 5. TRANSFORM: Hide IDs and apply perfect field sorting
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (PetDTO pet : pagedList) {
            responseList.add(convertToSortedMapWithoutId(pet));
        }

        return Response.ok(responseList).build();
    }

    /**
     * GET /pets/{id} -> View specific pet status.
     */
    @GET
    @Path("/{id}")
    public Response viewPetStatus(@PathParam("id") Long id) {
        PetDTO pet = petService.getPetById(id);
        return Response.ok(convertToSortedMapWithoutId(pet)).build();
    }

    /**
     * PUT /pets/{id}/feed -> Feed the pet.
     */
    @PUT
    @Path("/{id}/feed")
    public Response feedPet(@PathParam("id") Long id) {
        PetDTO updatedPet = petService.feedPet(id);
        return Response.ok(convertToSortedMapWithoutId(updatedPet)).build();
    }

    /**
     * PUT /pets/{id}/play -> Play with the pet.
     */
    @PUT
    @Path("/{id}/play")
    public Response playWithPet(@PathParam("id") Long id) {
        PetDTO updatedPet = petService.playWithPet(id);
        return Response.ok(convertToSortedMapWithoutId(updatedPet)).build();
    }

    /**
     * DELETE /pets/{id} -> Release the pet.
     */
    @DELETE
    @Path("/{id}")
    public Response releasePet(@PathParam("id") Long id) {
        petService.releasePet(id);
        return Response.noContent().build();
    }
}
