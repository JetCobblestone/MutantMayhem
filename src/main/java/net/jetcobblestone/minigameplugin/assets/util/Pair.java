package net.jetcobblestone.minigameplugin.assets.util;

import lombok.Getter;
import lombok.Setter;

public class Pair<L,R> {
    @Setter @Getter private R right;
    @Setter @Getter private L left;

    public Pair(L left, R right){
        this.right = right;
        this.left = left;
    }

    public static <E,T> Pair<E, T> of(E l, T r) {
        return new Pair<>(l, r);
    }
}
