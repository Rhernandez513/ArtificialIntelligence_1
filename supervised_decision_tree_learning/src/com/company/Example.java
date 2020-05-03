package com.company;

import com.company.enums.*;

import java.util.Objects;

public class Example {
    private Alternative alternative;
    private Bar bar;
    private FridayOrSaturday fridayOrSaturday;
    private Hungry hungry;
    private Patrons patrons;
    private Price price;
    private Raining raining;
    private Reservation reservation;
    private Type type;
    private Estimate estimate;
    private boolean willWait;

    @Override
    public String toString() {
        return "Example{" +
                "alternative=" + alternative +
                ", bar=" + bar +
                ", fridayOrSaturday=" + fridayOrSaturday +
                ", hungry=" + hungry +
                ", patrons=" + patrons +
                ", price=" + price +
                ", raining=" + raining +
                ", reservation=" + reservation +
                ", type=" + type +
                ", estimate=" + estimate +
                ", willWait=" + willWait +
                '}';
    }

    public boolean isAlternative() {
        return this.alternative.equals(Alternative.Yes);
    }

    public boolean isBar() {
        return this.bar.equals(Bar.Yes);
    }

    public boolean isFridayOrSaturday() {
        return this.fridayOrSaturday.equals(FridayOrSaturday.Yes);
    }

    public boolean isHungry() {
        return this.hungry.equals(Hungry.Yes);
    }

    public boolean isRaining() {
        return this.raining.equals(Raining.Yes);
    }

    public boolean isReservation() {
        return this.reservation.equals(Reservation.Yes);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Example example = (Example) o;
        return willWait == example.willWait &&
                alternative == example.alternative &&
                bar == example.bar &&
                fridayOrSaturday == example.fridayOrSaturday &&
                hungry == example.hungry &&
                patrons == example.patrons &&
                price == example.price &&
                raining == example.raining &&
                reservation == example.reservation &&
                type == example.type &&
                estimate == example.estimate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternative, bar, fridayOrSaturday, hungry, patrons, price, raining, reservation, type, estimate, willWait);
    }

    public Alternative getAlternative() {
        return alternative;
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public FridayOrSaturday getFridayOrSaturday() {
        return fridayOrSaturday;
    }

    public void setFridayOrSaturday(FridayOrSaturday fridayOrSaturday) {
        this.fridayOrSaturday = fridayOrSaturday;
    }

    public Hungry getHungry() {
        return hungry;
    }

    public void setHungry(Hungry hungry) {
        this.hungry = hungry;
    }

    public Patrons getPatrons() {
        return patrons;
    }

    public void setPatrons(Patrons patrons) {
        this.patrons = patrons;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Raining getRaining() {
        return raining;
    }

    public void setRaining(Raining raining) {
        this.raining = raining;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public boolean willWait() {
        return willWait;
    }

    public void setWillWait(boolean willWait) {
        this.willWait = willWait;
    }
}
