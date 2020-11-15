package com.krillsson.sysapi.dto.memory;

public final class PhysicalMemory {

  private String bankLabel;
  private long capacityBytes;
  private long clockSpeedHertz;

  private String manufacturer;

  private String memoryType;

  public PhysicalMemory(
      String bankLabel,
      long capacityBytes,
      long clockSpeedHertz,
      String manufacturer,
      String memoryType
  ) {
    this.bankLabel = bankLabel;
    this.capacityBytes = capacityBytes;
    this.clockSpeedHertz = clockSpeedHertz;
    this.manufacturer = manufacturer;
    this.memoryType = memoryType;
  }

  public final String getBankLabel() {
    return this.bankLabel;
  }

  public final long getCapacityBytes() {
    return this.capacityBytes;
  }

  public final long getClockSpeedHertz() {
    return this.clockSpeedHertz;
  }

  public final String getManufacturer() {
    return this.manufacturer;
  }

  public final String getMemoryType() {
    return this.memoryType;
  }

  public void setBankLabel(String bankLabel) {
    this.bankLabel = bankLabel;
  }

  public void setCapacityBytes(long capacityBytes) {
    this.capacityBytes = capacityBytes;
  }

  public void setClockSpeedHertz(long clockSpeedHertz) {
    this.clockSpeedHertz = clockSpeedHertz;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public void setMemoryType(String memoryType) {
    this.memoryType = memoryType;
  }
}