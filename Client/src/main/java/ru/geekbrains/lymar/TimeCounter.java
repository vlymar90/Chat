package ru.geekbrains.lymar;

public class TimeCounter implements Runnable {
    Authorization authorization;

    public TimeCounter(Authorization authorization) {
        this.authorization = authorization;
    }

    @Override
    public void run() {
        try {
            while (authorization.isTime) {
                authorization.getInfoTime().setText("" + authorization.getCounter().getAndDecrement());
                Thread.sleep(1000);
                if (authorization.getCounter().get() == 0) {
                    authorization.service.shutdownNow();
                    System.exit(0);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            authorization.getCounter().set(60);
        }
    }
}
