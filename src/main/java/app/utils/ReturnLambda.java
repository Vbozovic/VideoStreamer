package app.utils;

public interface ReturnLambda<C,R> {
    R getRes(C controller);
}
