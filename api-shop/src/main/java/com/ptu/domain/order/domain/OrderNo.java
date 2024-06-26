package com.ptu.domain.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderNo implements Serializable {

  @Column(name = "order_number")
  private String number;

  private OrderNo(String number) {
    this.number = number;
  }

  public static OrderNo of(String number) {
    return new OrderNo(number);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderNo orderNo = (OrderNo) o;
    return Objects.equals(number, orderNo.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }
}
