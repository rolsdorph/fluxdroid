package io.rolsdorph.fluxdroid;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.core.Completable;
import io.rolsdorph.fluxdroid.db.AppDatabase;
import io.rolsdorph.fluxdroid.db.ResultType;
import io.rolsdorph.fluxdroid.db.SentEvent;

public final class EventRepository {
    private final AppDatabase db;

    public EventRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public LiveData<Integer> getTotalSuccessCount() {
        return db.sentEventDao().getTotalCountWithResult(ResultType.Success);
    }

    public Completable writeEvent(SentEvent event) {
        return db.sentEventDao().insertEvents(event);
    }

}
