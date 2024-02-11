package com.minod.core.service;

public class StatefulService {
    private int price; //상태를 유지하는 필드
    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
//       this.price = price; //여기가 문제! 위에 price 정보를 쓰면 안될 것이다. 한번쓰고 초기화해주는것도 문제 생길 수 있다.
    }

    public int orderNew(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
//       this.price = price; //여기가 문제! 위에 price 정보를 쓰면 안될 것이다. 한번쓰고 초기화해주는것도 문제 생길 수 있다.
        return price;
    }
//    public int getPrice() // 얘도 문제가능성 있음.
//    { return price;
//
//    }
}
