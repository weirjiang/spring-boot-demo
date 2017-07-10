package redisMq;

public interface Callback {
    public void onMessage(String message);
}