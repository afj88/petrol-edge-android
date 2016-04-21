package it.petroledge.spotthatcar.manager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import it.petroledge.spotthatcar.App;
import it.petroledge.spotthatcar.entity.CarEntity;
import it.petroledge.spotthatcar.model.CarModel;
import it.petroledge.spotthatcar.model.SizeOfCarModel;
import it.petroledge.spotthatcar.repository.CarRepo;
import it.petroledge.spotthatcar.service.IApis;
import it.petroledge.spotthatcar.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alessandro on 27/03/16.
 */
public class ApiManager {

    private EventBus mBus;
    private IApis mApis;

    public ApiManager(EventBus bus, IApis apis) {
        mBus = bus;
        mApis = apis;
    }

    @Subscribe
    public void onLoadCars(LoadCarsEvent event) {
        // if c'è connessione
        //      chiedi al servizio se ci sono da scaricare dati passando data
        //      if ci sono dati da scaricare //TODO: passare la data per avere differenziale

        CarRepo repo = new CarRepo();
        List<Long> localCars = repo.getCarIds(event.getStartFeedDate(), event.getEndFeedDate());

        mApis.syncCars(localCars).enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {

                if (response.isSuccessful()) {

                    repo.saveCars(response.body());

                    List<CarModel> carModels = repo.getCars(event.getStartFeedDate(), event.getEndFeedDate());

                    List<CarEntity> cars = Stream.of(carModels).map(CarEntity::new).collect(Collectors.toList());

                    LoadedCarsEvent loadedCarsEvent = new LoadedCarsEvent(cars);
                    mBus.post(loadedCarsEvent);

                } else {
                    // error response, no access to resource?
                }

            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                mBus.post(t.getMessage());
            }
        });
        //      altrimenti prendi quelli in cache chiamando il repo repo
    }
}

