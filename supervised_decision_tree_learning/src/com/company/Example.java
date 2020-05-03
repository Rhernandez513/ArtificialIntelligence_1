package com.company;

import com.company.enums.Patrons;
import com.company.enums.Price;
import com.company.enums.Type;
import com.company.enums.WaitEstimate;

import java.util.Objects;

public class Example {
    private boolean alternative;
    private boolean bar;
    private boolean fridayOrSaturday;
    private boolean hungry;
    private Patrons patrons;
    private Price price;
    private boolean raining;
    private boolean reservation;
    private Type type;
    private WaitEstimate waitEstimate;
    private boolean willWait;

    public boolean isAlternative() {
        return alternative;
    }

    public void setAlternative(boolean alternative) {
        this.alternative = alternative;
    }

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isFridayOrSaturday() {
        return fridayOrSaturday;
    }

    public void setFridayOrSaturday(boolean fridayOrSaturday) {
        this.fridayOrSaturday = fridayOrSaturday;
    }

    public boolean isHungry() {
        return hungry;
    }

    public void setHungry(boolean hungry) {
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

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WaitEstimate getWaitEstimate() {
        return waitEstimate;
    }

    public void setWaitEstimate(WaitEstimate waitEstimate) {
        this.waitEstimate = waitEstimate;
    }

    public boolean willWait() {
        return willWait;
    }

    public void setWillWait(boolean willWait) {
        this.willWait = willWait;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Example that = (Example) o;
        return alternative == that.alternative &&
                bar == that.bar &&
                fridayOrSaturday == that.fridayOrSaturday &&
                hungry == that.hungry &&
                raining == that.raining &&
                reservation == that.reservation &&
                willWait == that.willWait &&
                patrons == that.patrons &&
                price == that.price &&
                type == that.type &&
                waitEstimate == that.waitEstimate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternative, bar, fridayOrSaturday, hungry, patrons, price, raining, reservation, type, waitEstimate, willWait);
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "alternatives=" + alternative +
                ", bar=" + bar +
                ", fridayOrSaturday=" + fridayOrSaturday +
                ", hungry=" + hungry +
                ", patrons=" + patrons +
                ", price=" + price +
                ", raining=" + raining +
                ", reservation=" + reservation +
                ", type=" + type +
                ", waitEstimate=" + waitEstimate +
                ", willWait=" + willWait +
                '}';
    }
}
