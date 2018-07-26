package com.krillsson.sysapi.dto.power;

public class PowerSource {


    private String name;

    private double remainingCapacity;

    private double timeRemaining;

    /**
     * No args constructor for use in serialization
     */
    public PowerSource() {
    }

    /**
     * @param remainingCapacity
     * @param name
     * @param timeRemaining
     */
    public PowerSource(String name, double remainingCapacity, double timeRemaining) {
        super();
        this.name = name;
        this.remainingCapacity = remainingCapacity;
        this.timeRemaining = timeRemaining;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getRemainingCapacity() {
        return remainingCapacity;
    }


    public void setRemainingCapacity(double remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }


    public double getTimeRemaining() {
        return timeRemaining;
    }


    public void setTimeRemaining(double timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

}
