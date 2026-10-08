package com.ferry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class PortData
 * Menyediakan daftar pelabuhan yang dapat dipilih user.
 */
public final class PortData {
    private static final List<String> PORTS = Collections.unmodifiableList(Arrays.asList(
        "Bakauheni, Lampung",
        "Gilimanuk, Bali",
        "Ketapang, Jawa Timur",
        "Merak, Banten"
    ));

    private PortData() {
    }

    /**
     * Mengembalikan daftar pelabuhan yang tersedia.
     */
    public static List<String> getPorts() {
        return PORTS;
    }
}
