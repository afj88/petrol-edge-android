package it.petroledge.spotthatcar.model;

import com.vincentbrison.openlibraries.android.dualcache.lib.SizeOf;

public class SizeOfCarModel implements SizeOf<CarModel> {

    @Override
    public int sizeOf(CarModel object) {
        return 32;
    }
}
