package io.rolsdorph.fluxdroid.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface SentEventDao {
    @Query("SELECT COUNT(*) FROM sent_events WHERE result_type = :resultType")
    LiveData<Integer> getTotalCountWithResult(ResultType resultType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertEvents(SentEvent... events);
}
