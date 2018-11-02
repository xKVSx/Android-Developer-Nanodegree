package android.csulb.edu.popularmoviesstage1.backgroundtasks;

import java.util.ArrayList;

public interface AsyncTaskCompleteListener<T> {

    public void onTaskComplete (ArrayList<T> result);
    public void onPostTask();
}
