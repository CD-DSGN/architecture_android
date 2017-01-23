package com.grandmagic.readingmate.base;

import com.tamic.novate.BaseApiService;
import com.tamic.novate.Novate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class AppBaseNovate extends Novate {
    /**
     * Mandatory constructor for the Novate
     *
     * @param callFactory
     * @param baseUrl
     * @param headers
     * @param parameters
     * @param apiManager
     * @param converterFactories
     * @param adapterFactories
     * @param callbackExecutor
     * @param validateEagerly
     */
    String baseUrl = "";
    AppBaseNovate(Call.Factory callFactory, String baseUrl, Map<String, String> headers, Map<String, String> parameters, BaseApiService apiManager, List<Converter.Factory> converterFactories, List<CallAdapter.Factory> adapterFactories, Executor callbackExecutor, boolean validateEagerly) {
        super(callFactory, baseUrl, headers, parameters, apiManager, converterFactories, adapterFactories, callbackExecutor, validateEagerly);
    }
}
