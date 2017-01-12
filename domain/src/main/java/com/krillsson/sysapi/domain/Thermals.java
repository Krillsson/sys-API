package com.krillsson.sysapi.domain;

public class Thermals {
    private double[] temperatures;
    private double[] fans;

    public Thermals(double[] temperatures, double[] fans) {
        this.temperatures = temperatures;
        this.fans = fans;
    }

    public double[] getTemperatures() {
        return temperatures;
    }

    public double[] getFans() {
        return fans;
    }

    public void setTemperatures(double[] temperatures) {
        this.temperatures = temperatures;
    }
}
