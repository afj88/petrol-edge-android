package it.petroledge.spotthatcar.repository;

import android.database.sqlite.SQLiteDatabase;
import android.util.Property;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import it.petroledge.spotthatcar.App;
import it.petroledge.spotthatcar.model.CarModel;
import it.petroledge.spotthatcar.model.SizeOfCarModel;
import it.petroledge.spotthatcar.repository.schema.Car;
import it.petroledge.spotthatcar.repository.schema.CarDao;
import it.petroledge.spotthatcar.repository.schema.DaoMaster;
import it.petroledge.spotthatcar.repository.schema.DaoSession;
import it.petroledge.spotthatcar.util.Constants;

/**
 * Created by friz on 09/04/16.
 */
public class CarRepo extends Repo {

    DualCache<CarModel> cache = new DualCacheBuilder<CarModel>(
            Constants.CACHE_NAME,
            App.VERSION_CODE,
            CarModel.class)
            .useReferenceInRam(Constants.RAM_MAX_SIZE, new SizeOfCarModel())
            .useDefaultSerializerInDisk(Constants.DISK_MAX_SIZE, true);

//    public List<CarModel> getCars() {
//    }

    public List<Long> saveCars(List<CarModel> models) {

        DaoSession daoSession = openSession();
        CarDao carDao = daoSession.getCarDao();

        Map<Long, CarModel> carsMap = Stream.of(models).collect(Collectors.toMap(CarModel::getId, x -> x));

        List<Car> dbCarsList = carDao.queryBuilder().where(CarDao.Properties.Id.in(carsMap.keySet())).list();
        Map<Long, Car> dbCarsMap = Stream.of(dbCarsList).collect(Collectors.toMap(Car::getId, x -> x));

        List<Long> insertedIdList = new ArrayList<>();
        daoSession.runInTx(() -> {
            for (Map.Entry<Long, CarModel> x : carsMap.entrySet()) {
                long id = -1;
                if (dbCarsMap.get(x.getKey()) == null) {
                    id = carDao.insert(new Car(x.getKey(), x.getValue().getFeedDate()));
                    insertedIdList.add(id);
                }
            }
        });
        closeSession(daoSession);

        Map<Long, Car> insertedCarsMap = Stream.of(dbCarsMap.entrySet())
                .filter(x -> insertedIdList.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (CarModel x : models) {
            cache.put(String.valueOf(x.getId()), x);
        }

        return Stream.of(models).map(CarModel::getId).collect(Collectors.toList());
    }

    public List<CarModel> getCars(Date startFeedDate, Date endFeedDate) {
        List<Long> ids = getCarIds(startFeedDate, endFeedDate);

        List<CarModel> carModels = new ArrayList<>();
        for (Long id : ids) {
            CarModel model = cache.get(String.valueOf(id));
            if(model != null)
                carModels.add(model);
        }

        return carModels;
    }

    public List<Long> getCarIds(Date startFeedDate, Date endFeedDate) {
        DaoSession daoSession = openSession();
        CarDao carDao = daoSession.getCarDao();

        List<Car> dbCarList = carDao.queryBuilder().where(CarDao.Properties.Feed_date.between(startFeedDate, endFeedDate)).list();
        List<Long> dbCarIdList = Stream.of(dbCarList).map(Car::getId).collect(Collectors.toList());

        closeSession(daoSession);

        return  dbCarIdList;
    }
}
