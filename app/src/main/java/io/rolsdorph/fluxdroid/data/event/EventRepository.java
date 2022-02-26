package io.rolsdorph.fluxdroid.data.event;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import io.reactivex.rxjava3.core.Completable;
import io.rolsdorph.fluxdroid.data.db.AppDatabase;
import io.rolsdorph.fluxdroid.data.db.ResultType;
import io.rolsdorph.fluxdroid.data.db.SentEvent;

public final class EventRepository {
    private final AppDatabase db;

    public EventRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .enableMultiInstanceInvalidation() // TODO: For our use cases this can probably be replaced with a singleton
                .build();
    }

    public LiveData<Integer> getTotalSuccessCount() {
        return db.sentEventDao().getTotalCountWithResult(ResultType.Success);
    }

    public Completable writeEvent(SentEvent event) {
        return db.sentEventDao().insertEvents(event);
    }

}
