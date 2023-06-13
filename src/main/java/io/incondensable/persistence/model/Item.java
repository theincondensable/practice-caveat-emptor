package io.incondensable.persistence.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author abbas
 */
public class Item {

    private Set<Bid> bids = new HashSet<>();

    public Set<Bid> getBids() {
        return Collections.unmodifiableSet(bids);
    }

    public void addBid(Bid bid) {
        if (bid == null)
            throw new NullPointerException("Bid is null");
        if (bid.getItem() != null)
            throw new IllegalArgumentException("The Bid is already assigned to an Item.");

        bids.add(bid);
        bid.setItem(this);
    }

}
