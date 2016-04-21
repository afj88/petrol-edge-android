package it.petroledge.spotthatcar.manager;

import java.util.List;

import it.petroledge.spotthatcar.entity.CarEntity;

public class LoadedCarsEvent {
    private List<CarEntity> mPhotos;

    public LoadedCarsEvent(List<CarEntity> photos) {
        mPhotos = photos;
    }

    public List<CarEntity> getPhotos() {
        return mPhotos;
    }
}
