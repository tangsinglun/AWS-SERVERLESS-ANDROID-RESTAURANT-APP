package website.programming.androideatitserver.Model;

import java.util.List;

/**
 * Created by cokel on 3/17/2018.
 */

public class MyResponse {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;
}
