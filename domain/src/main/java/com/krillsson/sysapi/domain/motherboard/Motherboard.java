package com.krillsson.sysapi.domain.motherboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Motherboard
{
    private String name;
    private Map<String, Double> boardTemperatures;
    private Map<String, Double> boardFanRpms;
    private Map<String, Double> boardFanPercents;

    public Motherboard(String name, Map<String, Double> boardTemperatures, Map<String, Double> boardFanRpms, Map<String, Double> boardFanPercents)
    {
        this.name = name;
        this.boardTemperatures = boardTemperatures;
        this.boardFanRpms = boardFanRpms;
        this.boardFanPercents = boardFanPercents;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }
    @JsonProperty
    public Map<String, Double> getBoardTemperatures()
    {
        return boardTemperatures;
    }
    @JsonProperty
    public Map<String, Double> getBoardFanRpms()
    {
        return boardFanRpms;
    }
    @JsonProperty
    public Map<String, Double> getBoardFanPercents()
    {
        return boardFanPercents;
    }
}
