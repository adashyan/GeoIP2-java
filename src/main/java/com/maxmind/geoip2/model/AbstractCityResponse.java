package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCityResponse extends AbstractCountryResponse {

    private final City city;
    private final Location location;
    private final Postal postal;
    private final List<Subdivision> subdivisions;

    AbstractCityResponse() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    AbstractCityResponse(
            City city,
            Continent continent,
            Country country,
            Location location,
            MaxMind maxmind,
            Postal postal,
            Country registeredCountry,
            RepresentedCountry representedCountry,
            List<Subdivision> subdivisions,
            Traits traits
    ) {
        super(continent, country, maxmind, registeredCountry, representedCountry, traits);
        this.city = city != null ? city : new City();
        this.location = location != null ? location : new Location();
        this.postal = postal != null ? postal : new Postal();
        this.subdivisions = subdivisions != null ? subdivisions : new ArrayList<>();
    }

    AbstractCityResponse(
            AbstractCityResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        this(
            response.getCity() != null ? new City(response.getCity(), locales) : null,
            response.getContinent() != null ? new Continent(response.getContinent(), locales) : null,
            response.getCountry() != null ? new Country(response.getCountry(), locales) : null,
            response.getLocation(),
            response.getMaxMind(),
            response.getPostal(),
            response.getRegisteredCountry() != null ? new Country(response.getRegisteredCountry(), locales) : null,
            response.getRepresentedCountry() != null ? new RepresentedCountry(response.getRepresentedCountry(), locales) : null,
            mapSubdivisions(locales, response.getSubdivisions()),
            response.getTraits() != null ? new Traits(response.getTraits(), ipAddress, network) : null
        );
    }

    private static ArrayList<Subdivision> mapSubdivisions(
            List<String> locales,
            List<Subdivision> subdivisions
    ) {
        if (subdivisions == null) {
            return null;
        }

        ArrayList<Subdivision> subdivisions2 = new ArrayList<>(subdivisions.size());
        for (Subdivision subdivision : subdivisions) {
            subdivisions2.add(new Subdivision(subdivision, locales));
        }
        return subdivisions2;
    }

    /**
     * @return City record for the requested IP address.
     */
    public City getCity() {
        return this.city;
    }

    /**
     * @return Location record for the requested IP address.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * @return the postal
     */
    public Postal getPostal() {
        return this.postal;
    }

    /**
     * @return An {@link List} of {@link Subdivision} objects representing the
     * country subdivisions for the requested IP address. The number and
     * type of subdivisions varies by country, but a subdivision is
     * typically a state, province, county, etc. Subdivisions are
     * ordered from most general (largest) to most specific (smallest).
     * If the response did not contain any subdivisions, this method
     * returns an empty array.
     */
    public List<Subdivision> getSubdivisions() {
        return new ArrayList<>(this.subdivisions);
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     */
    @JsonIgnore
    public Subdivision getMostSpecificSubdivision() {
        if (this.subdivisions.isEmpty()) {
            return new Subdivision();
        }
        return this.subdivisions.get(this.subdivisions.size() - 1);
    }

    /**
     * @return An object representing the least specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     */
    @JsonIgnore
    public Subdivision getLeastSpecificSubdivision() {
        if (this.subdivisions.isEmpty()) {
            return new Subdivision();
        }
        return this.subdivisions.get(0);
    }
}
