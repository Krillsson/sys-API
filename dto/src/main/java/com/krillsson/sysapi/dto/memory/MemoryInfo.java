package com.krillsson.sysapi.dto.memory;

import java.util.List;

public class MemoryInfo {

  private long swapTotal;
  private long total;
  private List<PhysicalMemory> physicalMemory;

  public MemoryInfo(long swapTotal, long total, List<PhysicalMemory> physicalMemory) {
    this.swapTotal = swapTotal;
    this.total = total;
    this.physicalMemory = physicalMemory;
  }

  public long getSwapTotal() {
    return this.swapTotal;
  }

  public long getTotal() {
    return this.total;
  }

  public List<PhysicalMemory> getPhysicalMemory() {
    return this.physicalMemory;
  }

  public void setSwapTotal(long swapTotal) {
    this.swapTotal = swapTotal;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public void setPhysicalMemory(List<PhysicalMemory> physicalMemory) {
    this.physicalMemory = physicalMemory;
  }
}