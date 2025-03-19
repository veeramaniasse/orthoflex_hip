package com.simats.orthoflexhip.dataClass;

public class RecentPatientData {
    private boolean status;
    private RecentPatientRecyclerData[] data;

    public boolean getStatus() { return status; }
    public void setStatus(boolean value) { this.status = value; }

    public RecentPatientRecyclerData[] getData() { return data; }
    public void setData(RecentPatientRecyclerData[] value) { this.data = value; }
}

