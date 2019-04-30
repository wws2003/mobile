package hpg.org.samplegithubrepobrowser.util

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

// Util for Rx-async tasks
object TaskControl {

    private val TAG = "TaskControl"


    /**
     * Execute task for result
     *
     * @param task
     * @param resultProcessor
     * @param <T>
    </T> */
    fun <T> executeInBackgroundThenProcessResult(task: Callable<T>, resultProcessor: Consumer<T>): Disposable {
        return executeInBackgroundThenProcessResult(task, resultProcessor, Functions.ON_ERROR_MISSING)
    }

    /**
     * Execute task for result
     *
     * @param task
     * @param resultProcessor
     * @param exceptionHandler
     * @param <T>
    </T> */
    fun <T> executeInBackgroundThenProcessResult(
        task: Callable<T>,
        resultProcessor: Consumer<T>,
        exceptionHandler: Consumer<Throwable>
    ): Disposable {
        return executeInBackgroundThenProcessResult(Functions.emptyConsumer(), task, resultProcessor, exceptionHandler)
    }

    /**
     * Execute task for result
     *
     * @param onSubscribe
     * @param task
     * @param resultProcessor
     * @param exceptionHandler
     * @param <T>
    </T> */
    fun <T> executeInBackgroundThenProcessResult(
        onSubscribe: Consumer<in Disposable>,
        task: Callable<T>,
        resultProcessor: Consumer<T>,
        exceptionHandler: Consumer<Throwable>
    ): Disposable {
        return Observable
            .fromCallable(task)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(onSubscribe)
            .subscribe(resultProcessor, Consumer { throwable ->
                Log.e(TAG, throwable.getLocalizedMessage(), throwable)
                exceptionHandler.accept(throwable)
            })//rxjava
    }
}