package com.epam.mentoring.rocket.facade.converter;

public interface Converter<S, T> {

    T convert(S source);
}
