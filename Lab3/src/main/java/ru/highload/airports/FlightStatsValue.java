package ru.highload.airports;

import java.io.Serializable;

public class FlightStatsValue implements Serializable {
    private static final float EPS = 1e-9f;

    private float maxDelay;
    private int delayedFlights;
    private int cancelledFlights;
    private int flights;

    public FlightStatsValue(float maxDelay, int delayedFlights, int cancelledFlights, int flights) {
        this.maxDelay = maxDelay;
        this.delayedFlights = delayedFlights;
        this.cancelledFlights = cancelledFlights;
        this.flights = flights;
    }

    public FlightStatsValue(String delay, String cancelled) {
        if (delay.equals("")) {
            this.maxDelay = 0.f;
            this.delayedFlights = 0;
        } else {
            this.maxDelay = Float.parseFloat(delay);
            this.delayedFlights = (maxDelay > EPS ? 1 : 0);
        }

        this.cancelledFlights = (Float.parseFloat(cancelled) > EPS ? 1 : 0);
        this.flights = 1;
    }

    public float getMaxDelay() {
        return maxDelay;
    }

    public int getDelayedFlights() {
        return delayedFlights;
    }

    public int getCancelledFlights() {
        return cancelledFlights;
    }

    public int getFlights() {
        return flights;
    }


    static FlightStatsValue add(FlightStatsValue a, FlightStatsValue b) {
        return new FlightStatsValue(
                Math.max(a.getMaxDelay(), b.getMaxDelay()),
                a.getDelayedFlights() + b.delayedFlights,
                a.getCancelledFlights() + b.getCancelledFlights(),
                a.getFlights() + b.getFlights()
        );
    }

    @Override
    public String toString() {
        return maxDelay + ", " +
                Math.round((float) delayedFlights / flights * 100f) + "%, " +
                Math.round((float) cancelledFlights / flights * 100f) + "% ";
    }
}
