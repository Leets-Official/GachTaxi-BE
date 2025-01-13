package com.gachtaxi.global.auth.jwt.util;

import java.util.StringTokenizer;

public abstract class KafkaBeanUtils {

  public static String getBeanName(String topic, String suffix) {
    StringTokenizer stringTokenizer = new StringTokenizer(topic, "-_");

    StringBuilder beanNameBuilder = new StringBuilder();
    beanNameBuilder.append(stringTokenizer.nextToken());

    while (stringTokenizer.hasMoreTokens()) {
      beanNameBuilder.append(getFirstUpperString(stringTokenizer.nextToken()));
    }

    beanNameBuilder.append(suffix);

    return beanNameBuilder.toString();
  }

  public static String getFirstUpperString(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}
