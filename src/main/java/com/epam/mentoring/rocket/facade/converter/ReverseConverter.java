package com.epam.mentoring.rocket.facade.converter;

public interface ReverseConverter<S, T> {

    S reverseConvert(T target);
}
