package com.price.processor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor(staticName = "of")
public class CurrencyPair {

    String ccyPair;
    Double rate;
}
