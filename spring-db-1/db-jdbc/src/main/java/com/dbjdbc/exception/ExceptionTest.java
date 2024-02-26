package com.dbjdbc.exception;

public class ExceptionTest {
    static void startInstall() throws SpaceException, MemoryException, InstallException {
        try{
            if(!enoughSpace()) 		// 충분한 설치 공간이 없으면...
                throw new SpaceException("설치할 공간이 부족합니다.");
            if (!enoughMemory())		// 충분한 메모리가 없으면...
                throw new MemoryException("메모리가 부족합니다.");
        } catch (SpaceException se) {
            // SpaceException
            // 여기서 checked예외인 Exception을 unchecked예외인 RuntimeException으로 변경해서 내보내는 것도 가능하다.
            InstallException ie = new InstallException(("SpaceException 설치중 예외발생, InstallException ie 내보내기준비"));
            ie.initCause(se); // InstallException의 원인 예외를 SpaceException으로 지정
            throw ie; // ie를 밖으로 내보낸다.
        }

    } // startInstall메서드의 끝
    static void copyFiles() { /* 파일들을 복사하는 코드를 적는다. */ }
    static void deleteTempFiles() { /* 임시파일들을 삭제하는 코드를 적는다.*/ }
    static boolean enoughSpace()   {
        // 설치하는데 필요한 공간이 있는지 확인하는 코드를 적는다.
        return false;
    }
    static boolean enoughMemory() {
        // 설치하는데 필요한 메모리공간이 있는지 확인하는 코드를 적는다.
        return true;
    }
}
