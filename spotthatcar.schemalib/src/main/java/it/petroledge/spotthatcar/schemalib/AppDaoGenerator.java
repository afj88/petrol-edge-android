package it.petroledge.spotthatcar.schemalib;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class AppDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "it.petroledge.spotthatcar.repository.schema");

        Entity car = schema.addEntity("Car");

        car.addIdProperty().primaryKey();
        car.addDateProperty("feed_date").indexDesc("idx_feed_date_desc", false);

        File f = new File(AppDaoGenerator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

        DaoGenerator daoGenerator = new DaoGenerator();


        daoGenerator.generateAll(schema,"/Users/friz/Projects/PetrolEdge/Android/PetrolEdge/app/src/main/java");
    }
}
