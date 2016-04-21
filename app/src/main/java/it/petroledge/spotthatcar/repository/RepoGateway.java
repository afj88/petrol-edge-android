package it.petroledge.spotthatcar.repository;

/**
 * Created by friz on 10/04/16.
 */
public class RepoGateway {
    private static RepoGateway ourInstance = new RepoGateway();

    public static RepoGateway getInstance() {
        return ourInstance;
    }

    private CarRepo mCarRepo;
    public CarRepo getCarRepo() { return mCarRepo; }

    private RepoGateway() {
        mCarRepo = new CarRepo();
    }
}
