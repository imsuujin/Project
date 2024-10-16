package com.shop.exception;


//재고가 부족할 때 발생하는 예외 상황을 나타내기 위해 사용됨.
    public class OutOfStockException extends RuntimeException{

        //부모 생성자한테 받은 message를 보냄
        public OutOfStockException(String message) {
            super(message);
        }
    }
