package com.krillsson.sysapi.domain.motherboard;

import oshi.json.hardware.ComputerSystem;

import java.util.Map;

public class Motherboard
{
    private ComputerSystem computerSystem;
    private Map<String, Double> boardTemperatures;
    private Map<String, Double> boardFanRpms;
    private Map<String, Double> boardFanPercents;

    public Motherboard(ComputerSystem computerSystem, Map<String, Double> boardTemperatures, Map<String, Double> boardFanRpms, Map<String, Double> boardFanPercents)
    {
        this.computerSystem = computerSystem;
        this.boardTemperatures = boardTemperatures;
        this.boardFanRpms = boardFanRpms;
        this.boardFanPercents = boardFanPercents;
    }

    public ComputerSystem getComputerSystem()
    {
        return computerSystem;
    }
    public Map<String, Double> getBoardTemperatures()
    {
        return boardTemperatures;
    }
    public Map<String, Double> getBoardFanRpms()
    {
        return boardFanRpms;
    }
    public Map<String, Double> getBoardFanPercents()
    {
        return boardFanPercents;
    }
}
