package com.dbjdbc.exception;

public class CustomError {

}
class InstallException extends Exception {
    InstallException(String msg){
        super(msg);
    }
}
class SpaceException extends Exception {
    SpaceException(String msg) {
        super(msg);
    }
}

class MemoryException extends Exception {
    MemoryException(String msg) {
        super(msg);
    }
}
