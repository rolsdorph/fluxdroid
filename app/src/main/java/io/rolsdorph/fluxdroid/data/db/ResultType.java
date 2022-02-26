package io.rolsdorph.fluxdroid.data.db;

public enum ResultType {
    Success, UnexpectedResponseCode, UnknownError;

    public boolean isSuccess() {
        return this == Success;
    }
}
