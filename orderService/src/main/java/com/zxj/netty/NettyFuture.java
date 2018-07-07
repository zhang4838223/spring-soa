package com.zxj.netty;

import java.util.concurrent.*;

public class NettyFuture<T> implements Future<T> {

    private static Callable callable = new Callable() {
        public Object call() throws Exception {
            throw new RuntimeException("should never be called!!");
        }
    };

    private NettyFutureTask<T> task = new NettyFutureTask<T>();

    public NettyFuture() {
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return task.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return task.isCancelled();
    }

    public boolean isDone() {
        return task.isDone();
    }

    public T get() throws InterruptedException, ExecutionException {
        return task.get();
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return task.get(timeout, unit);
    }

    public void set(T value) {
        task.setResult(value);
    }

    public void setException(Throwable exception) {
        task.setException(exception);
    }


    private static class NettyFutureTask<T> extends FutureTask<T> {

        private volatile Thread completThread;
        public NettyFutureTask(){
            super(callable);
        }
        public NettyFutureTask(Callable<T> callable) {
            super(callable);
        }

        public NettyFutureTask(Runnable runnable, T result) {
            super(runnable, result);
        }

        public void setResult(T value) {
            set(value);
        }

        public void setException(Throwable throwable) {
            super.setException(throwable);
        }

        @Override
        protected void done() {
            super.isCancelled();
            super.done();
        }
    }
}
