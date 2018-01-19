package com.test.jwj.underMoon.network;

public interface IMessageArrived <T> {
    public abstract void OnDataArrived(T t);
}
