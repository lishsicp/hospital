package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Entity;
import java.util.Map;

public interface Validator<E extends Entity> {
    Map<String, String> validate(E entity);
}
